package it.polimi.ingsw.lim.network.client;

import static it.polimi.ingsw.lim.network.client.MainClient.*;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;
import it.polimi.ingsw.lim.network.ui.AbsUI;

import java.util.Scanner;

/**
 * Created by nico.
 * This is the client command line interface
 */
public class CLI extends AbsUI {
    Scanner userInput = new Scanner(System.in);
    String input;
    boolean exitNow = false;

    /**
     * Choose the connection protocol and connect to the server
     */
    public String setNetworkSettings() throws ClientNetworkException {
        System.out.print("Please select the network protocol: (socket/rmi) ");
        while (true) {
            input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "socket":
                case "s":
                    return "socket";
                case "rmi":
                case "r":
                    return "rmi";
                default:
                    printMessage("Not a valid choice!");
            }
        }
    }

    public String loginForm() {
        printMessage("Enter a username:");
        input = userInput.nextLine().toLowerCase();
        return input;
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessage(String message) {
        System.out.println(message);
    }
}