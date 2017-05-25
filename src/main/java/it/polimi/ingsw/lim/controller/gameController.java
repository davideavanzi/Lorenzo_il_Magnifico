package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.parser.Parser;

/**
 * Created by Davide on 25/05/2017.
 * This class is the main game controller.
 */
public class gameController {
    private Game game;

    public static void main(String[] args){
        Game game = new Game();
        Parser gameParser = new Parser();
        gameParser.parser("src/main/gameData/configs/default/");
        /**
         * TEST PRINTS
         */
        System.out.println("LUNGHEZZA ARRAY:"+gameParser.getTowerbonuses("GREEN").length);
        gameParser.getTowerbonuses("GREEN")[1].printAssets();

        //building game
        game.setUpGame(4,gameParser);
    }

}
