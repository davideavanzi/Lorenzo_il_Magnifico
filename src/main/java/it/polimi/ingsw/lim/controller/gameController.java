package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.parser.Parser;

import java.io.IOException;

import static it.polimi.ingsw.lim.Log.*;
import static it.polimi.ingsw.lim.Settings.CONFIGS_PATH;

/**
 * Created by Davide on 25/05/2017.
 * This class is the main game controller.
 */
public class gameController {
    private Game game;

    public static void main(String[] args){
        try {
            createLogFile();
        }catch (IOException e){
           //TODO: handle
        }
        getLog().info("Creating new game instance.");
        Game game = new Game();
        String defaultPath = "default/";
        getLog().info("Parsing game data from path: "+CONFIGS_PATH+defaultPath);

        Parser parsedGame = Parser.parser(CONFIGS_PATH+defaultPath);
        getLog().info("Validating game data with current settings.");
        //TODO: how to call validating method?

        //building game
        getLog().info("Setting up game with parsed data");
        //TODO: add players before setting up the game!
        game.setUpGame(4,parsedGame);
    }

    /**
     * This method checks if all parsed data are ok with current game settings.
     * - Development cards number for each color and age = tower_height * turns_per_age
     * - At least one excommunication for every age from first_excomm_fp to ages_number
     * - Tower bonuses consistent with tower heights.
     * - Market bonuses consistent.
     * - Council bonuses consistent.
     * - More controls?
     * - Faith track bonuses corresponding to faith track length
     * do we have to check if every card is valid?
     */

    private static boolean validateParsedData(){
        //TODO: implement here
        return true;
    }

}
