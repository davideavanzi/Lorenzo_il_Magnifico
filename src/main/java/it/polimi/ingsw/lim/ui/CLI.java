package it.polimi.ingsw.lim.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by nico.
 * This is the client commandInput line interface
 */
public class CLI extends AbsUI {
    Scanner userInput = new Scanner(System.in);
    String input;

    public void waitUserInput() {
        while(true) {
            input = userInput.nextLine().toLowerCase();
            UIController.inputParser(input);
        }
    }

    /**
     * Choose the connection protocol and connect to the server
     */
    public String setNetworkSettings() {
        printMessageln("Please select the network protocol: (socket/rmi) ");
        while (true) {
            printMessage("$ ");
            input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "socket":
                case "s":
                    return "socket";
                case "rmi":
                case "r":
                    return "rmi";
                default:
                    printMessageln("Not a valid choice!");
            }
        }
    }

    public String loginForm() {
        printMessageln("Enter a username:");
        printMessage("$ ");
        input = userInput.nextLine().toLowerCase();
        return input;
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessageln(String message) {
        System.out.println(message);
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessage(String message) {
        System.out.print(message);
    }
}