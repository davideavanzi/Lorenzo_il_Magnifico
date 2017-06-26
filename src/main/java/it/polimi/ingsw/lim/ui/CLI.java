package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Strengths;
import it.polimi.ingsw.lim.model.Tower;

import java.util.Scanner;

/**
 * Created by nico.
 * This is the client commandInput line interface
 */
public class CLI extends AbsUI {
    Scanner userInput = new Scanner(System.in);
    String input;

    public void printStrengths(Strengths strengths, String username) {
        if (strengths != null)
            printMessageln(username.concat("'s strenghts: "));
            //Todo print user strength
    }

    public void printAssets(Assets resource, String username) {
        if (resource != null) {
            printMessageln(username.concat("'s resources: "));
            //Todo use emoji for resource??
        }
    }

    public void printTower(String color, Tower tower) {
        //Todo print the tower with a tui
    }

    public void printPlayerCards() {

    }

    public void printChatMessage(String sender, String message) {
        printMessageln("[CHAT] message from "+sender+": "+message);
    }

    public void waitUserInput() {
        while(true) {
            input = userInput.nextLine().toLowerCase().trim();
            UIController.inputParser(input);
        }
    }

    public String loginForm() {
        printMessageln("Enter a username:");
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