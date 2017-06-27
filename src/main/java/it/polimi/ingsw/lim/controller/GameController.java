package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.network.server.RMI.RMIUser;
import it.polimi.ingsw.lim.parser.Parser;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.ArrayList;
import java.util.Collections;
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


    public static void main(String[] args){

        createLogFile();
        getLog().info("Creating new game instance.");
        GameController contr = new GameController();
        contr.createGame();
        contr.game.addPlayer("CIAONE");
        contr.game.addPlayer("HELLONE");
        contr.game.addPlayer("HOLAONE");

        contr.moveInTower(contr.game.getPlayer("CIAONE").pullFamilyMember(BLACK_COLOR), GREEN_COLOR,1);
        contr.moveInHarvest(contr.game.getPlayer("CIAONE").pullFamilyMember(WHITE_COLOR));
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
        this.game = new Game();
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
     * This methods moves a family member in a tower, checking if the action is legal.
     * @param fm
     * @param towerColor
     * @param floor
     * TODO: do we have to split the legality checks from the actual move?
     * TODO: handle max card number and battle points requirements for green card
     */
    public void moveInTower (FamilyMember fm, String towerColor, int floor) {
        Strengths strength = new Strengths();
        User actor = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname());
        getLog().log(Level.INFO, "Player "+actor.getPlayer().getNickname()+
                " is trying to enter "+towerColor+" tower at floor number "+floor+" with the "
                +fm.getDiceColor()+" family member of value "+this.game.getDice().get(fm.getDiceColor()));
        if(this.game.isTowerMoveAllowed(towerColor, floor, fm, strength)){
            if(this.game.isTowerMoveAffordable(towerColor, floor, fm)){
                int servantsForTowerAction = this.game.servantsForTowerAction(fm, towerColor, floor);
                int servantsDeployed;
                do {
                    servantsDeployed = actor.askForServants(servantsForTowerAction);
                } while (servantsDeployed < servantsForTowerAction || servantsDeployed >
                                this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants());
                Card card = this.game.towerMove(towerColor, floor, fm);
                //Activate before all resources bonus, then actions.
                card.getImmediateEffects().stream().filter(ie -> ie instanceof AssetsEffect)
                        .filter(ie -> ie instanceof AssetsMultipliedEffect)
                        .filter(ie -> ie instanceof CardMultipliedEffect)
                        .filter(ie -> ie instanceof CouncilFavorsEffect)
                        .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
                card.getImmediateEffects().stream().filter(ie -> ie instanceof ActionEffect)
                        .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
                if (card instanceof BlueCard) CardHandler.activateBlueCard((BlueCard)card, actor.getPlayer());
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
    public void moveInHarvest (FamilyMember fm) {
        if(this.game.isHarvestMoveAllowed(fm)){
            Player actor = this.game.getPlayerFromColor(fm.getOwnerColor());
            int servantsForHarvestAction = this.game.servantsForHarvestAction(fm);
            actor.setResources(actor.getResources().add(actor.getDefaultHarvestBonus()));
            int servantsDeployed;
            do {
                servantsDeployed = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname())
                        .askForServants(servantsForHarvestAction);
            } while (servantsDeployed < servantsForHarvestAction ||
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants());
            this.game.harvestMove(fm);
            int actionStrength = game.calcHarvestActionStr(fm, servantsDeployed, 0);
            for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(GREEN_COLOR)) {
                GreenCard activeCard = (GreenCard) card;
                if (activeCard.getActionStrength().getHarvestBonus() <= actionStrength)
                    CardHandler.activateGreenCard(activeCard, game.getPlayerFromColor(fm.getOwnerColor()));
            }
        }
    }

    public void moveInProduction (FamilyMember fm, User actor) {
        if(this.game.isProductionMoveAllowed(fm)){
            Assets bonusAccumulator = new Assets(actor.getPlayer().getDefaultProductionBonus());
            int servantsForProductionAction = this.game.servantsForProductionAction(fm);
            int servantsDeployed;
            do {
                servantsDeployed = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname())
                        .askForServants(servantsForProductionAction);
            } while (servantsDeployed < servantsForProductionAction ||
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants());
            this.game.productionMove(fm);
            int actionStrength = game.calcProductionActionStr(fm, servantsDeployed, 0);
            for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(GREEN_COLOR)) {
                YellowCard activeCard = (YellowCard) card;
                if (activeCard.getActionStrength().getProductionBonus() <= actionStrength)
                    CardHandler.activateYellowCard(activeCard, actor, bonusAccumulator);
            }
            actor.getPlayer().setResources(actor.getPlayer().getResources().add(bonusAccumulator));
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
    }

    public Room getRoomCallback() {
        return roomCallback;
    }
}
