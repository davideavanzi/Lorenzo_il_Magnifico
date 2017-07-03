package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import it.polimi.ingsw.lim.parser.Parser;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import javax.jws.soap.SOAPBinding;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * Created by Davide on 25/05/2017.
 * This class is the main game controller.
 */
public class GameController {
    private Game game;
    private Room roomCallback;

    public GameController(Room roomCallback) { this.roomCallback = roomCallback; }

    public GameController() {  }

    private ArrayList<ArrayList<Assets[]>> currentProductionOptions;
    private Assets currentProductionAccumulator;
    private Strengths fastActionStr;
    private Assets optPickDiscount;
    private User fastActor;

    public static void main(String[] args){

        createLogFile();
        getLog().info("Creating new game instance.");
        GameController contr = new GameController();
        contr.createGame();
        contr.game.addPlayer("CIAONE");
        contr.game.addPlayer("HELLONE");
        contr.game.addPlayer("HOLAONE");

        contr.moveInTower(contr.game.getPlayer("CIAONE").pullFamilyMember(BLACK_COLOR), GREEN_COLOR,1,1);
        System.out.println("SIZE FM: "+contr.game.getPlayer("CIAONE").getFamilyMember().size());
        //contr.moveInHarvest(contr.game.getPlayer("CIAONE").pullFamilyMember(WHITE_COLOR));
        /*
        Game game = new Game();
        String defaultPath = "default/";
        getLog().log(Level.INFO, () -> "Parsing game data from path: " + CONFIGS_PATH+defaultPath);

        //TODO: handle exception in a proper place
        Parser parsedGame = new Parser();
        try {
            parsedGame.parser(CONFIGS_PATH+defaultPath);
        } catch (Exception e) {
            getLog().severe("PARSER ERROR:\n"+e.getMessage());
        }

        getLog().info("Validating game data with current settings.");
        //TODO: how to call validating method?
        if(validateParsedData(parsedGame))
            getLog().severe("[ERROR] - parsed data are incorrect");


        //building game
        getLog().info("Setting up game with parsed data");
        //TODO: add players before setting up the game!;
        game.addPlayer("CIAONE");
        game.addPlayer("HELLONE");
        game.addPlayer("HOLAONE");
        try {
            game.setUpGame(parsedGame);
        } catch (GameSetupException e) {
            getLog().severe(e.getMessage());
        }
        game.setUpTurn();
        game.getTower("GREEN").getFloor(1).getCard().printCard();

        moveInTower(game.getPlayer("CIAONE").pullFamilyMember(BLACK_COLOR), GREEN_COLOR,1);

        System.out.println("Carte verdi in ciaone: "+game.getPlayer("CIAONE").getCardsOfColor(GREEN_COLOR).size());

        System.out.println("La torre verde al piano uno ha la carta? "+game.getTower(GREEN_COLOR).getFloor(1).hasCard());
        System.out.println("DICE COLORS:");
        DICE_COLORS.forEach(color -> System.out.println(color+": "+game.getDice().get(color)));
        System.out.println(game.isHarvestMoveAllowed(new FamilyMember(ORANGE_COLOR, GREEN_COLOR)));

        */
    }

    /**
     * This method checks if all parsed data are ok with current game settings.
     * - Development cards number for each color and age = tower_height * turns_per_age (??)
     * - At least one excommunication for every age from first_excomm_fp to ages_number
     * - Tower bonuses consistent with tower heights.
     * - Market bonuses consistent.
     * - Council bonuses consistent.
     * - More controls?
     * - Faith track bonuses corresponding to faith track length
     * TODO: do we have to check if every card is valid?
     */
    private static boolean validateParsedData (Parser parsedGame){
        for (int age = 1; age <= AGES_NUMBER; age++)
            for (String color : DEFAULT_TOWERS_COLORS)
                return !(parsedGame.getCard(age, color).size() == TURNS_PER_AGE * TOWER_HEIGHT);
        for (String color : DEFAULT_TOWERS_COLORS)
            return !(parsedGame.getTowerbonuses(color).length == TOWER_HEIGHT);
        //TODO: implement
        return true;
    }

    /**
     * This method creates an empty instance of the game.
     * Room must have players before instantiating game instance
     */
    public void createGame() {
        this.game = new Game(this);
        roomCallback.getUsersList().forEach(user ->
            user.setPlayer(game.addPlayer(user.getUsername())));
        Collections.shuffle(game.getPlayers());
        String defaultPath = "default/";
        //TODO: handle exception in a proper place
        Parser parsedGame = new Parser();
        try {
            parsedGame.parser(CONFIGS_PATH+defaultPath);
        } catch (Exception e) {
            getLog().severe("PARSER ERROR:\n"+e.getMessage());
        }
        getLog().info("Validating game data with current settings.");
        //TODO: how to call validating method?
        if(validateParsedData(parsedGame))
            getLog().severe("[ERROR] - parsed data are incorrect");
        //building game
        getLog().info("Setting up game with parsed data");
        try {
            game.setUpGame(parsedGame);
        } catch (GameSetupException e) {
            getLog().severe(e.getMessage());
        }
        game.setUpTurn();
    }

    /**
     * This method is called from the game upon a new age, it asks whether a player wants to keep
     */
    public void handleExcommunications() {
        //this.roomCallback.getUsersList().forEach(user -> if(this.game.));
    }

    /**
     * This methods moves a family member in a tower, checking if the action is legal.
     * @param fm
     * @param towerColor
     * @param floor
     * TODO: do we have to split the legality checks from the actual move?
     * TODO: handle max card number and battle points requirements for green card
     */
    public void moveInTower (FamilyMember fm, String towerColor, int floor, int servantsDeployed) {
        Strengths strength = new Strengths();
        User actor = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname());
        getLog().log(Level.INFO, "Player "+actor.getPlayer().getNickname()+
                " is trying to enter "+towerColor+" tower at floor number "+floor+" with the "
                +fm.getDiceColor()+" family member of value "+this.game.getDice().get(fm.getDiceColor()));
        if(this.game.isTowerMoveAllowed(towerColor, floor, fm, strength)){
            if(this.game.isTowerMoveAffordable(towerColor, floor, fm)){
                Card card = this.game.getTower(towerColor).getFloor(floor).getCard();
                boolean cardAffordable = this.game.isCardAffordable(card, actor.getPlayer(), towerColor);
                boolean purpleAffordable = card instanceof PurpleCard ||
                        this.game.isPurpleCardAffordable((PurpleCard)card, actor.getPlayer());
                if (cardAffordable || purpleAffordable) {
                    boolean useBp = false;
                    int servantsForTowerAction = this.game.servantsForTowerAction(fm, towerColor, floor);

                    if (servantsDeployed < servantsForTowerAction || servantsDeployed >
                            actor.getPlayer().getResources().getServants()) {
                        actor.gameMessage("You did not set the right amount of servants to deploy to perform the action");
                        return;
                    }

                    if (cardAffordable && purpleAffordable) {
                            //let the client choose and save the action state.
                            useBp = actor.askForOptionalBpPick(((PurpleCard) card).getOptionalBpRequirement(),
                                    ((PurpleCard) card).getOptionalBpCost());

                        } else {
                            //in this case only one of the two paying methods is available
                            if (purpleAffordable) useBp = true;
                            this.game.towerMove(towerColor, floor, fm, servantsDeployed, useBp);
                        }
                    //Activate before all resources bonus, then actions.
                    card.getImmediateEffects().stream().filter(ie -> ie instanceof AssetsEffect
                            || ie instanceof AssetsMultipliedEffect || ie instanceof CardMultipliedEffect
                            || ie instanceof CouncilFavorsEffect)
                            .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
                    card.getImmediateEffects().stream().filter(ie -> ie instanceof ActionEffect)
                            .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
                    if (card instanceof BlueCard) CardHandler.activateBlueCard((BlueCard)card, actor.getPlayer());
                    roomCallback.broadcastMessage
                            ("Player "+actor.getUsername()+" has picked a card from the "+towerColor+" tower");
                    roomCallback.fmPlaced();
                } else {
                    getLog().log(Level.INFO, "But the card is not affordable");
                }
            } else {
                getLog().log(Level.INFO, "But the move is not affordable");
            }
        } else {
            getLog().log(Level.INFO, "But the move is not allowed");
        }
    }

    /**
     * This method performs the actual harvest move.
     * It does not require a reference to the remote user as the harvest has not costs to choose.
     * @param fm the family member deployed for the action
     */
    public void moveInHarvest (FamilyMember fm, int servantsDeployed) {
        if(this.game.isHarvestMoveAllowed(fm)){ //If
            Player actor = this.game.getPlayerFromColor(fm.getOwnerColor());
            int servantsForHarvestAction = this.game.servantsForHarvestAction(fm, 0);
            this.game.giveAssetsToPlayer(actor.getDefaultHarvestBonus(), actor);

            if (servantsDeployed < servantsForHarvestAction || //TODO: do better
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants()) return;
            this.game.harvestMove(fm);
            int actionStrength = game.calcHarvestActionStr(fm, servantsDeployed, 0);
            for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(GREEN_COLOR)) {
                GreenCard activeCard = (GreenCard) card;
                if (activeCard.getActionStrength().getHarvestBonus() <= actionStrength)
                    CardHandler.activateGreenCard(activeCard, game.getPlayerFromColor(fm.getOwnerColor()));
            }
            roomCallback.broadcastMessage
                    ("Player "+actor.getNickname()+" performed an harvest action of value: "+actionStrength);
            roomCallback.fmPlaced();
        }
    }

    public void moveInProduction (FamilyMember fm, User actor, int servantsDeployed) {
        if(this.game.isProductionMoveAllowed(fm)){
            int servantsForProductionAction = this.game.servantsForProductionAction(fm, 0);

            if (servantsDeployed < servantsForProductionAction ||
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants());

            this.currentProductionAccumulator = new Assets(actor.getPlayer().getDefaultProductionBonus());
            int actionStrength = game.calcProductionActionStr(fm, servantsDeployed, 0);
            for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(YELLOW_COLOR)) {
                YellowCard activeCard = (YellowCard) card;
                if (activeCard.getActionStrength().getProductionBonus() <= actionStrength)
                    CardHandler.activateYellowCard(activeCard, actor, this);
            }
            if (this.currentProductionOptions.size() == 0) {
                //don't ask user, complete directly production skipping excomm malus
                actor.getPlayer().setResources(actor.getPlayer().getResources().add(currentProductionAccumulator));
            } else {
                actor.askProductionOptions(currentProductionOptions);
            }
        }
    }

    public void confirmProduction(ArrayList<Integer> choices) {
        if (choices.size() != currentProductionOptions.size()) {
            getLog().log(Level.SEVERE, "Wrong amount of player production choices!");
            return;
        }
        currentProductionOptions.forEach(option -> {
                if (option.get(choices.indexOf(option))[0].isGreaterOrEqual(
                        roomCallback.getPlayingUser().getPlayer().getResources())){
                    this.game.removeAssetsFromPlayer(option.get(choices.indexOf(option))[0],
                            roomCallback.getPlayingUser().getPlayer());
                    currentProductionAccumulator.add(option.get(choices.indexOf(option))[1]);}
        });
        roomCallback.broadcastMessage
                ("Player XXX performed a production action");
        roomCallback.fmPlaced();
    }

    /**
     * this method adds the available options of a yellow card to choose
     * @param options
     */
    void addProductionOptions(ArrayList<Assets[]> options) {
        this.currentProductionOptions.add(options);
    }

    void addBonusToAccumulator(Assets bonus) {
        this.currentProductionAccumulator.add(this.game.apllyExcommMalus(bonus,
                roomCallback.getPlayingUser().getPlayer()));
    }

    //FAST ACTIONS:

    void beginFastProduction(Strengths str, User actor) {
        this.fastActionStr = str;
        this.fastActor = actor;
        actor.notifyFastProduction(str.getProductionBonus());
    }

    void beginFastHarvest(Strengths str, User actor) {
        this.fastActionStr = str;
        this.fastActor = actor;
        actor.notifyFastHarvest(str.getProductionBonus());
    }

    /**
     * Tells the user that can perform a fast tower move, telling him all towers that the bonus
     * allows to enter.
     * @param str
     * @param optPickDiscount
     * @param actor
     */
    void beginFastTowerMove(Strengths str, Assets optPickDiscount, User actor) {
        this.fastActionStr = str;
        this.optPickDiscount = optPickDiscount;
        this.fastActor = actor;
        Map<String, Integer> activeBonuses = str.getTowerStrength().entrySet().stream()
                .filter(option -> option.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        HashMap<String, Integer> availableTowers = new HashMap<>(activeBonuses);
        actor.notifyFastTowerMove(availableTowers, optPickDiscount);
    }


    public void performFastHarvest(int servantsDeployed) {
        int servantsForHarvestAction = this.game.servantsForHarvestAction(null, fastActionStr.getHarvestBonus());
        if (servantsForHarvestAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForHarvestAction) {
            //TODO: tell user bad entry
            return;
        }
        this.game.giveAssetsToPlayer(fastActor.getPlayer().getDefaultHarvestBonus(), fastActor.getPlayer());
        int actionStrength = game.calcHarvestActionStr(null, servantsDeployed, fastActionStr.getHarvestBonus());
        for (Card card: fastActor.getPlayer().getCardsOfColor(GREEN_COLOR)) {
            GreenCard activeCard = (GreenCard) card;
            if (activeCard.getActionStrength().getHarvestBonus() <= actionStrength)
                CardHandler.activateGreenCard(activeCard, fastActor.getPlayer());
        }
    }


    public void performFastProduction(int servantsDeployed, User actor) {
        if (servantsDeployed > actor.getPlayer().getResources().getServants()) {
            getLog().log(Level.SEVERE, "Trying to deploy too many servants!");
            return;
        }
        this.currentProductionAccumulator = new Assets(actor.getPlayer().getDefaultProductionBonus());
        int actionStrength = game.calcProductionActionStr(null, servantsDeployed, fastActionStr.getProductionBonus());
        for (Card card: actor.getPlayer().getCardsOfColor(YELLOW_COLOR)) {
            YellowCard activeCard = (YellowCard) card;
            if (activeCard.getActionStrength().getProductionBonus() <= actionStrength)
                CardHandler.activateYellowCard(activeCard, actor, this);
        }
        if (this.currentProductionOptions.size() == 0) {
            //don't ask user, complete directly production skipping excomm malus
            actor.getPlayer().setResources(actor.getPlayer().getResources().add(currentProductionAccumulator));
        } else {
            actor.askProductionOptions(currentProductionOptions);
        }
    }

    public ArrayList<Player> getActualPlayingOrder() {
        return this.game.getPlayers();
    }

    /**
     * This method handles the whole logic to perform a fast tower action, activated by an immediate effect
     * @param
     * @param actor
     */
    public void performFastTowerMove(int servantsDeployed, String towerColor, int floor, User actor) {

        if (this.game.isFastTowerMoveAllowed(towerColor, floor,actor.getPlayer()));
        Card pickedCard = this.game.getTower(towerColor).getFloor(floor).pullCard();
        actor.getPlayer().addCard(pickedCard, towerColor);
        pickedCard.getImmediateEffects().stream().filter(ie -> ie instanceof AssetsEffect
                || ie instanceof AssetsMultipliedEffect || ie instanceof CardMultipliedEffect
                || ie instanceof CouncilFavorsEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
        pickedCard.getImmediateEffects().stream().filter(ie -> ie instanceof ActionEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
        if (pickedCard instanceof BlueCard) CardHandler.activateBlueCard((BlueCard)pickedCard, actor.getPlayer());
    }

    public void moveInMarket(FamilyMember fm, User actor) {

    }


    /**
     * This method has to be called at the beginning of the game,
     * otherwise it generates an order that might be incorrect
     * @return
     */
    public ArrayList<String> getPlayOrder() {
        return game.getNewPlayerOrder();
    }

    public void startNewTurn(){
        this.game.newTurn();
        this.game.setUpTurn();
    }

    public int[] getTime() {
        return new int[] {this.game.getAge(), this.game.getTurn()};
    }

    public Board getBoard() {
        return this.game.getBoard();
    }

    //TODO: do we need it?
    public Room getRoomCallback() {
        return roomCallback;
    }
}
