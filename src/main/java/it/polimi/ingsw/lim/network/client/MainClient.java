package it.polimi.ingsw.lim.network.client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import it.polimi.ingsw.lim.ui.UIController;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static it.polimi.ingsw.lim.ui.UIController.setUI;


public class MainClient {

    /**
     * This is the launcher of the Game.
     * @param args
     */
    public static void main(String[] args) {
        splashScreen();
        UIController client = new UIController(setUI());
        client.startGame();
        client.getClientUI().waitForRequest();
    }

    private static void splashScreen() {
        ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();
        cp.println("              WELCOME TO:\n" +
                "     _                            _____      __     __)                           \n" +
                " ___/__)                         (, /  /)   (, /|  /|               ,  /) ,       \n" +
                "(, /   _____   _ __   _   ___      /  //      / | / |  _   _  __      //    _  ___\n" +
                "  /   (_)/ (__(/_/ (_'_)_(_)   ___/__(/_   ) /  |/  |_(_(_(_/_/ (__(_/(__(_(__(_) \n" +
                " (_____             .-/      (__ /        (_/   '        .-/        /)            \n" +
                "        )          (_/                                  (_/        (/            ",
                Ansi.Attribute.BOLD, Ansi.FColor.YELLOW, Ansi.BColor.RED);
        cp.clear();
        System.out.println();
    }

}
