package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.*;
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
        //building game
        getLog().info("Setting up game with parsed data");
        //TODO: add players before setting up the game!
        game.setUpGame(4,parsedGame);
    }

}
