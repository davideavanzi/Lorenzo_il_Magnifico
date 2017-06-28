package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.model.Board;

import java.util.Scanner;

/**
 * Created by nico.
 * This is the client commandInput line interface
 */
public class CLI extends AbsUI {

    /**
     * Scanner for stdin.
     */
    Scanner userInput = new Scanner(System.in);

    /**
     * This contain the input command.
     */
    String input;

    public void printBoard(Board board) {

    }

    /**
     * Print to stdout a chat message.
     * @param sender the sender's username.
     * @param message the chat message.
     */
    public void printChatMessage(String sender, String message) {
        printMessageln("[CHAT] message from "+sender+": "+message);
    }

    /**
     * Waiting for user input, then passing it to a input parser
     */
    public void waitUserInput() {
        while(true) {
            input = userInput.nextLine().toLowerCase().trim();
            UIController.inputParser(input);
        }
    }

    /**
     * Enter the login information.
     * @return the login information.
     */
    public String loginForm(String command) {
        printMessageln("Enter a ".concat(command).concat(": "));
        printMessage("$ ");
        input = userInput.nextLine().toLowerCase();
        return input;
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