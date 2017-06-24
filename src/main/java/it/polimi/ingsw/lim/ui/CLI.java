package it.polimi.ingsw.lim.ui;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import com.vdurmont.emoji.EmojiParser;

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
    ColoredPrinter cp = new ColoredPrinter.Builder(1, false).build();


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
        printMessageln(":bust_in_silhouette:  Enter a username:");
        printMessage("$ ");
        input = userInput.nextLine().toLowerCase();
        return input;
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessageln(String message) {
        System.out.println(EmojiParser.parseToUnicode(message));
    }

    /**
     * Print on stdout a message
     * @param message
     */
    public void printMessage(String message) {
        System.out.print(EmojiParser.parseToUnicode(message));
    }

    public void printChatMessage(String sender, String message) {
        cp.print(EmojiParser.parseToUnicode(":email:  ")+"[CHAT] message from "+sender+" :", Ansi.Attribute.BOLD, Ansi.FColor.WHITE, Ansi.BColor.BLUE);
        cp.print(message+"\n");
    }
}