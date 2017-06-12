package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.FamilyMember;
import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.model.Strengths;
import it.polimi.ingsw.lim.parser.Parser;

import java.util.ArrayList;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * Created by Davide on 25/05/2017.
 * This class is the main game controller.
 */
public class GameController {
    private Game game;


    public static void main(String[] args){

        createLogFile();
        getLog().info("Creating new game instance.");
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
        game.getCouncil().addFamilyMember(game.getPlayer("HELLONE").pullFamilyMember(ORANGE_COLOR));
        game.getCouncil().addFamilyMember(game.getPlayer("HOLAONE").pullFamilyMember(ORANGE_COLOR));
        //game.getNewPlayerOrder().forEach(pl -> System.out.println(pl));

        Assets asset1 = new Assets(1,0,1,0,0,0,9);
        Assets asset2 = new Assets(3,0,1,0,0,0,9);
        Assets asset3 = new Assets(1,0,1,0,0,0,9);

        System.out.println("TEST ASSETS EQUALS");
        if (asset1.equals(asset2))
            System.out.println("UNO = DUE");
        if (asset1.equals(asset3))
            System.out.println("UNO = TRE");
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

    public ArrayList<String> getPlayOrder() {
        return  game.getNewPlayerOrder();
    }

    /**
     * This methods moves a family member in a tower, checking if the action is legal.
     * @param fm
     * @param towerColor
     * @param floor
     * TODO: do we have to split the checkings from the actual move?
     */
    public void moveInTower (FamilyMember fm, String towerColor, int floor) {
        Strengths strength = new Strengths();
        if(this.game.isTowerMoveAllowed(towerColor, floor, fm, strength)){
            if(this.game.isTowerMoveAffordable(towerColor, floor, fm)){
                //move is affordable, ask the client in case more servants are needed
                if (this.game.servantsForTowerAction(fm, towerColor, floor) > 0);
                    //ask player!
            }

        }
    }



}
