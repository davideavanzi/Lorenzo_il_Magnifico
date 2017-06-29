package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.exceptions.InvalidInputException;
import it.polimi.ingsw.lim.model.Player;

import java.util.HashMap;
import java.util.Scanner;

import static it.polimi.ingsw.lim.ui.UIController.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.INFO;
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.TURN;

/**
 * Created by nico.
 * This is the client commandInput line interface
 */
public class CLI extends AbsUI {

    /**
     * Scanner for stdin.
     */
    private Scanner userInput = new Scanner(System.in);

    /**
     * This contain the input command.
     */
    private String input;

    private UIController uiCallback;

    private Lock lock;

    protected CLI(UIController uiCallback) {
        lock = new Lock();
        lock.lock();
        this.uiCallback = uiCallback;
        initializeCommandLine();
    }

    public Lock getLock() {
        return lock;
    }

    /**
     * Simply print on stdout a string.
     */
    public void turnForm() {
        printMessageln("Turn order: ");
    }

    /**
     * Print to stdout a chat message.
     * @param sender the sender's username.
     * @param message the chat message.
     */
    public void printChatMessage(String sender, String message) {
        printMessageln("[CHAT]: message from "+sender+": "+message);
    }

    private void personalInformation() {

    }

    /**
     * Print the currently turn order.
     */
    private void turnOrder() {
        turnForm();
        for (Player pl : uiCallback.getLocalPlayers())
            printMessageln(pl.getNickname());
    }

    /**
     * Send a chat message to the server.
     */
    private void chat() {
        printMessage("Enter a chat message: ");
        userInput.nextLine();
        input = userInput.nextLine().trim();
        uiCallback.sendChatMessage(input);
        lock.unlock();

    }

    private void cmdManager(String command) throws InvalidInputException {
        if (cmdDescr.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not found: ").concat(command));
        if (availableCmdList.get(command) == null)
            throw new InvalidInputException(("[COMMAND_LINE]: Command not available: ").concat(command));
        availableCmdList.get(command).run();
    }

    private void printCmd() {
        printMessageln("Available Command:");
        printMessageln("");
        availableCmdList.keySet().forEach(command -> System.out.printf("%-30s%s%n", command, cmdDescr.get(command)));
        printMessage("");
    }

    public void waitForRequest() {
        while (true) {
            printCmd();
            printMessage("Enter a command: ");
            input = userInput.next().toLowerCase().trim();
            try {
                cmdManager(input);
            } catch (InvalidInputException e) {
                printMessageln(e.getMessage());
            }
            lock.lock();
        }
    }

    private void initializeCommandLine() {
        cmdDescr = new HashMap<>();
        availableCmdList = new HashMap<>();

        //Description HashMap
        cmdDescr.put(CHAT, CHAT_DESCR);
        cmdDescr.put(TURN, TURN_DESCR);
        cmdDescr.put(INFO, INFO_DESCR);
        cmdDescr.put(FAMILY_MEMBER, FAMILY_MEMBER_DESCR);
        cmdDescr.put(LEADER_CARD, LEADER_CARD_DESCR);
        cmdDescr.put(CHOOSE_FAVOR, CHOOSE_FAVOR_DESCR);
        cmdDescr.put(CHOOSE_TOWER,CHOOSE_TOWER_DESCR);
        cmdDescr.put(CHOOSE_FLOOR, CHOOSE_FLOOR_DESCR);
        cmdDescr.put(CHOOSE_PRODUCTION, CHOOSE_PRODUCTION_DESCR);

        //Command HashMap
        availableCmdList.put(CHAT, () -> chat());
        availableCmdList.put(TURN, () -> turnOrder());
        availableCmdList.put(INFO, () -> personalInformation());

        if(true/*isMyTurn*/) {
            //availableCmdList.put(FAMILY_MEMBER, () -> );
            //availableCmdList.put(LEADER_CARD, () -> );
        }
    }

    /**
     * Enter the login information.
     * @return the login information.
     */
    public String loginForm(String command) {
        printMessageln("Enter a ".concat(command).concat(": "));
        printMessage("$ ");
        input = userInput.nextLine().toLowerCase().trim();
        return input;
    }

    /**
     * Choose the connection protocol and connect to the server
     */
    public String setNetworkSettings() {
        printMessage("Please select the network protocol: (socket/rmi): ");
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