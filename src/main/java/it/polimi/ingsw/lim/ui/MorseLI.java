package it.polimi.ingsw.lim.ui;

//import it.polimi.ingsw.lim.utils.MorseParser;

import java.util.Scanner;

/**
 * Created by Davide on 23/06/2017.
 */
public abstract class MorseLI extends AbsUI {

    Scanner userInput = new Scanner(System.in);
    String input;

    public void waitUserInput() {
        while(true) {
            input = userInput.nextLine().toLowerCase().trim();
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
        printMessageln("Enter a username: :bust_in_silhouette:");
        printMessage("$ ");
        input = userInput.nextLine().toLowerCase();
        return input;
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessageln(String message) {

        //System.out.println(MorseParser.encode(message));
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessage(String message) {
        //System.out.print(MorseParser.encode(message));
    }

    @Override
    public void printChatMessage(String sender, String message) {
        //System.out.println(MorseParser.encode("[CHAT] Message from "+sender+" : "+message));
    }
}


