package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import it.polimi.ingsw.lim.parser.Parser;
import it.polimi.ingsw.lim.parser.Writer;

import java.io.IOException;
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
    private ArrayList<ArrayList<Assets[]>> currentProductionOptions;
    private Assets currentProductionAccumulator;
    private Strengths fastActionStr;
    private Assets optPickDiscount;
    private User fastActor;
    private PendingTowerMove pendingTowerMove;

    /**
     * constructor
     * @param roomCallback the room that the game controller belong to
     */
    public GameController(Room roomCallback) { this.roomCallback = roomCallback; }

    /**
     * dummy constructor
     */
    public GameController() { game = new Game(); }


    public Game getGame(){
        return this.game;
    }

    public void setGame(Game game){
        this.game = game;
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
        if(!restartGame(roomCallback)){
            this.game = new Game(this);
            roomCallback.getUsersList().forEach(user ->
                    user.setPlayer(game.addPlayer(user.getUsername())));
            Collections.shuffle(game.getPlayers());
            String defaultPath = "default/";
            //TODO: handle exception in a proper place
            Parser parsedGame = new Parser();
            try {
                parsedGame.parser(CONFIGS_PATH + defaultPath);
            } catch (Exception e) {
                getLog().severe("PARSER ERROR:\n" + e.getMessage());
            }
            getLog().info("Validating game data with current settings.");
            //TODO: how to call validating method?
            if (validateParsedData(parsedGame))
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
    }

    /**
     * this method try to resume a game from a file
     * @param roomCallback the room that the game controller belong to
     * @return true if the game is successfully restored false otherwise
     */
    public boolean restartGame(Room roomCallback) {
        try {
            this.game = new Game();
            this.game = Writer.gameReader(roomCallback.getId());
            return true;
        }catch (IOException e){
            return false;
        }
    }

    /**
     * This methods moves a family member in a tower, checking if the action is legal.
     * @param fm
     * @param towerColor
     * @param floor
     * TODO: do we have to split the legality checks from the actual move?
     * TODO: handle max card number and battle points requirements for green card
     */
    public void moveInTower (FamilyMember fm, String towerColor, int floor, int servantsDeployed)
            throws BadRequestException {
        User actor = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname());
        getLog().log(Level.INFO, "Player "+actor.getPlayer().getNickname()+
                " is trying to enter "+towerColor+" tower at floor number "+floor+" with the "
                +fm.getDiceColor()+" family member of value "+this.game.getFmStrength(fm));
        if(this.game.isTowerMoveAllowed(towerColor, floor, fm)){
            if(this.game.isTowerMoveAffordable(towerColor, floor, fm)){
                Card card = this.game.getTower(towerColor).getFloor(floor).getCardSlot();
                boolean cardAffordable = this.game.isCardAffordable(card, actor.getPlayer(), towerColor, null);
                boolean purpleAffordable = card instanceof PurpleCard &&
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
                        this.pendingTowerMove = new PendingTowerMove(towerColor, floor, fm, servantsDeployed, actor);
                        actor.askForOptionalBpPick();
                    } else {
                        //in this case only one of the two paying methods is available
                        if (purpleAffordable) useBp = true;
                        this.game.towerMove(towerColor, floor, fm, servantsDeployed, useBp);
                        activatePickedCard(card, actor);
                        roomCallback.broadcastMessage
                                ("Player "+actor.getUsername()+" has picked a card from the "+towerColor+" tower");
                        roomCallback.fmPlaced();
                    }
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
     * This method continues a tower move interrupted to ask the actor whether he wanted to pay the ordinary cost or
     * with his battle points
     * @param useBp the decision of the user, it's true if the he wants to pay in battle points
     */
    public void confirmTowerMove(boolean useBp){
        Card card = this.game.towerMove(pendingTowerMove.tower,
                pendingTowerMove.floor,
                pendingTowerMove.fm,
                pendingTowerMove.servantsDeployed, useBp);
        activatePickedCard(card, pendingTowerMove.actor);
        roomCallback.broadcastMessage
                ("Player "+pendingTowerMove.actor.getUsername()+" has picked a card from the "+pendingTowerMove.tower+" tower");
        roomCallback.fmPlaced();
    }

    /**
     * this method activate the immediate effect of a card piked by user
     * @param card the card to be activated
     * @param actor the user who picked the card
     */
    public void activatePickedCard(Card card, User actor){
        //Activate before all resources bonus, then actions.
        card.getImmediateEffects().stream().filter(ie -> ie instanceof AssetsEffect
                || ie instanceof AssetsMultipliedEffect || ie instanceof CardMultipliedEffect
                || ie instanceof CouncilFavorsEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor, game));
        card.getImmediateEffects().stream().filter(ie -> ie instanceof ActionEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor, game));
        if (card instanceof BlueCard) CardHandler.activateBlueCard((BlueCard)card, actor.getPlayer());
    }

    /**
     * This method performs the actual harvest move.
     * It does not require a reference to the remote user as the harvest has not costs to choose.
     * @param fm the family member deployed for the action
     */
    public void moveInHarvest (FamilyMember fm, int servantsDeployed) throws BadRequestException {
        if(this.game.isHarvestMoveAllowed(fm)){ //If
            Player actor = this.game.getPlayerFromColor(fm.getOwnerColor());
            int servantsForHarvestAction = this.game.servantsForHarvestAction(fm, 0);
            this.game.giveAssetsToPlayer(actor.getDefaultHarvestBonus(), actor);
            if (servantsDeployed < servantsForHarvestAction || //TODO: do better
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants()) {

                return;
            }
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

    /**
     * this method is called when a player go to production with a family member, and (optional) using servants
     * @param fm the family member put in production
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if //todo
     */
    public void moveInProduction (FamilyMember fm, int servantsDeployed) throws BadRequestException {
        User actor = roomCallback.getPlayingUser(); //only the playing user can perform the action
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
                //don't ask user, complete directly production skipping excomm malus (already given)
                actor.getPlayer().setResources(actor.getPlayer().getResources().add(currentProductionAccumulator));
            } else {
                actor.askProductionOptions(currentProductionOptions);
            }
        }
    }

    /**
     * this method is called when a player go to market with a family member, and (optional) using servants
     * @param fm the family member put in the market
     * @param marketSlot is the position in the market in which the user intend to move to
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if //todo
     */
    public void moveInMarket(FamilyMember fm, int marketSlot, int servantsDeployed) throws BadRequestException {

    }

    /**
     * this method is called when a player go to council with a family member, and (optional) using servants
     * @param fm the family member put in the council
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if //todo
     */
    public void moveInCouncil(FamilyMember fm, int servantsDeployed) throws BadRequestException {

    }

    /**
     * //todo
     * @param choices
     * @throws BadRequestException
     */
    public void confirmProduction(ArrayList<Integer> choices) throws BadRequestException {
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


    public void performFastHarvest(int servantsDeployed) throws BadRequestException {
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


    public void performFastProduction(int servantsDeployed, User actor) throws BadRequestException {
        int servantsForProductionAction = this.game.servantsForProductionAction(null, fastActionStr.getHarvestBonus());
        if (servantsForProductionAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForProductionAction) {
            //TODO: tell user bad entry
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
    public void performFastTowerMove(int servantsDeployed, String towerColor, int floor, User actor)
            throws BadRequestException {
        int servantsForTowerAction = this.game.servantsForFastTowerAction(fastActionStr
                .getTowerStrength(towerColor), towerColor, floor, actor.getPlayer());
        if (servantsForTowerAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForTowerAction) {
            //TODO: tell user bad entry
            return;
        }
        if (!this.game.isFastTowerMoveAllowed(towerColor, floor,actor.getPlayer(), optPickDiscount) ||
                !fastActor.getUsername().equals(actor.getUsername())) {
            actor.gameError("Fast action not valid");
            return;
        }
        Card card = this.game.getTower(towerColor).getFloor(floor).getCardSlot();
        boolean cardAffordable = this.game.isCardAffordable(card, actor.getPlayer(), towerColor, optPickDiscount);
        boolean purpleAffordable = card instanceof PurpleCard &&
                this.game.isPurpleCardAffordable((PurpleCard)card, actor.getPlayer());
        if (cardAffordable || purpleAffordable) {
            boolean useBp = false;
            if (cardAffordable && purpleAffordable) {
                //let the client choose and save the action state.
                this.pendingTowerMove = new PendingTowerMove(towerColor, floor, servantsDeployed, actor);
                actor.askForOptionalBpPick();
            } else {
                //in this case only one of the two paying methods is available
                if (purpleAffordable) useBp = true;
                this.game.fastTowerMove(towerColor, floor, servantsDeployed, useBp, actor.getPlayer(), optPickDiscount);
                activatePickedCard(card, actor);
                roomCallback.broadcastMessage
                        ("Player "+actor.getUsername()+" has picked a card from the "+towerColor+" tower");
            }
        } else {
            getLog().log(Level.INFO, "The card accessed through fast action is not affordable");
        }
    }

    //------------------------------ COUNCIL

    //TODO:USELESS?
    public void giveCouncilFavors(int amount) {
        roomCallback.getPlayingUser().chooseFavor(amount);
    }

    public void performCfActivation(ArrayList<Integer> choices) throws BadRequestException {
        try {
            this.game.giveFavors(roomCallback.getPlayingUser().getPlayer(), choices);
        } catch (ControllerException e) {
            throw new BadRequestException("Wrong favors choices", e);
        }

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

    public ArrayList<String> buildRanking() {
        game.getPlayers().forEach(player -> game.calcEndGameBonus(player));
        return new ArrayList<>();
    }

    public void applyEndGameExcomm() {
        ArrayList<Player> playersToExcomm = new ArrayList<>
                (game.getPlayers().stream().filter(player -> game.isNotExcommunicable(player))
                        .collect(Collectors.toList()));
        playersToExcomm.forEach(player -> game.excommunicatePlayer(player));
    }

    public void applyExcommunication(ArrayList<Player> toExcommunicate) {
        //Excommunicate given players, reset fp of players that won't be excommunicated
    }

    private class PendingTowerMove {
        String tower;
        int floor;
        FamilyMember fm;
        int servantsDeployed;
        User actor;

        PendingTowerMove(String tower, int floor, FamilyMember fm, int servantsDeployed, User actor) {
            this.tower = tower;
            this.floor = floor;
            this.fm = fm;
            this.servantsDeployed = servantsDeployed;
            this.actor = actor;
        }

        PendingTowerMove(String tower, int floor, int servantsDeployed, User actor) {
            this.tower = tower;
            this.floor = floor;
            this.servantsDeployed = servantsDeployed;
            this.actor = actor;
        }
    }
}
