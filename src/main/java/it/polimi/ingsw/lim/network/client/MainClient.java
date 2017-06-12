package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;
import it.polimi.ingsw.lim.network.ui.AbsUI;

import java.util.Scanner;

/**
 * Created by nico.
 */
public class MainClient {
    private AbsClient clientProtocol;
    private AbsUI uiType;

    private static Scanner userInput = new Scanner(System.in);

    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param gui
     */
    MainClient(boolean gui) {
        if(gui) {
            //uiType = new GUI();
        } else {
            uiType = new CLI();
        }
    }

    /**
     * The player choose if he want to play with GUI or CLI
     * @return true if you want to use a GUI, false if you want a CLI
     */
    private static boolean setUI() {
        System.out.println("Do you want to play with a GUI? (y/n)");
        System.out.print("$");
        while (true) {
            String gui = userInput.nextLine().toLowerCase();
            switch (gui) {
                case "no":
                case "n":
                    return false;
                case "yes":
                case "y":
                    return true;
                default:
                    System.out.println("Not a valid choice, enter yes/y if you want to use a GUI, no/n for a CLI");
                    System.out.print("$");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Lorenzo Il Magnifico!");
        System.out.println();
        MainClient client = new MainClient(setUI());
    }
}
