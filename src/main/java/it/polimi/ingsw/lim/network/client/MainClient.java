package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.ui.UIController;

import static it.polimi.ingsw.lim.ui.UIController.setUI;

/**
 * Created by nico.
 */
public class MainClient {

    /**
     * This is the launcher of the Game.
     * @param args
     */
    public static void main(String[] args) {
        splashScreen();
        UIController client = new UIController(setUI());
        client.startGame();
    }

    private static void splashScreen() {
        System.out.println("Welcome to:\n" +
                "     _                            _____      __     __)                           \n" +
                " ___/__)                         (, /  /)   (, /|  /|               ,  /) ,       \n" +
                "(, /   _____   _ __   _   ___      /  //      / | / |  _   _  __      //    _  ___\n" +
                "  /   (_)/ (__(/_/ (_'_)_(_)   ___/__(/_   ) /  |/  |_(_(_(_/_/ (__(_/(__(_(__(_) \n" +
                " (_____             .-/      (__ /        (_/   '        .-/        /)            \n" +
                "        )          (_/                                  (_/        (/            ");
        System.out.println();
    }

}
