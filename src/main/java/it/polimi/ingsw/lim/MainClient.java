package it.polimi.ingsw.lim;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import it.polimi.ingsw.lim.ui.UIController;


public class MainClient {

    /**
     * This is the launcher of the Game.
     * @param args
     */
    public static void main(String[] args) {
        splashScreen();
        UIController client = new UIController();
        client.connect();
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
