package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;

/**
 * Created by nico.
 */
public class UIController {
    private static AbsUI clientUI;
    private static ServerInterface clientProtocol;
    private static Scanner userInput = new Scanner(System.in);
    //a copy of the user name is stored here
    private static String username;
<<<<<<< HEAD
=======
    Lock lock = new Lock();
    private static Board localBoard;
    private static ArrayList<Player> localPlayers;
>>>>>>> 1108a337f37cd579aa445fe69fb707bdbe65751d

    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param gui
     */
    public UIController(boolean gui) {
        if(gui) {
            //clientUI = new GUI();
        } else {
            clientUI = new CLI();
        }
    }

    public AbsUI getClientUI() {
        return clientUI;
    }

    public static void setClientProtocol(ServerInterface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }

    public void inputHandler() {
        UIController.clientUI.waitUserInput();
    }

    static void inputParser(String input) {
        ArrayList<String> commandInput = new ArrayList<>(Arrays.asList(input.split(" ")));
        String command = commandInput.get(0);
        switch (command) {
            case CHAT:
                chat(input.split(SPACE, 2)[1]);
                break;
            case TURN:

                break;
            case SHOW:
                if(commandInput.size() == 3) {
                    manageShowCommand(commandInput);
                } else {
                    clientUI.printMessageln(HELP_CHAT);
                }
                break;
            case HELP:
                help();
                break;
            default:
                clientUI.printMessageln("Command not found: "+command);
        }
    }

    private static void help() {
        clientUI.printMessageln("Command List:");
        clientUI.printMessageln(CHAT);
        clientUI.printMessageln(HELP_CHAT);
        clientUI.printMessageln(TURN);
        clientUI.printMessageln(HELP_TURN);
        clientUI.printMessageln(SHOW);
        clientUI.printMessageln(HELP_SHOW);
    }

    private static void manageShowCommand(ArrayList<String> commandInput) {
        switch (commandInput.get(1)) {
            case STRENGTH:
                
                break;
            case ASSETS:

                break;
            case TOWER:

                break;
            case CARD:

                break;
            case LEADER:

                break;
            case PERSONAL_BOARD:

                break;
            default:
                clientUI.printMessageln("Invalid show command's parameter");
        }
    }

    private static void chat(String message) {
        try {
            clientProtocol.chatMessageToServer(username, message);
        }catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    public void login() {
        String username = clientUI.loginForm();
        this.username = username;
        try {
            clientProtocol.sendLogin(username);
        } catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    /**
     *  If the player want to config the network settings.
     */
    public void setNetworkProtocol() {
        String protocol = clientUI.setNetworkSettings();
        if (protocol.equals("socket")) {
            new Thread(new SocketClient(this)).start();
        } else if (protocol.equals("rmi")) {
            clientProtocol = new RMIClient(this);
        }

        int failedRetry = 0;
        while(failedRetry < 3) {
            try {
                clientProtocol.connect();
                break;
            } catch (ClientNetworkException e) {
                failedRetry++;
                clientUI.printMessageln(e.getMessage());
            }
        }
    }

    /**
     * The player choose if he want to play with GUI or CLI.
     * @return true if you want to use a GUI, false if you want a CLI.
     */
    public static boolean setUI() {
        System.out.println("Do you want to play with a GUI? (y/n)");
        System.out.print("$ ");
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
            }
        }
    }

<<<<<<< HEAD
    class UIConstant {

        protected static final String SPACE = " ";

        protected static final String CHAT = "chat";
        protected static final String TURN = "turn";
        protected static final String SHOW = "show";
        protected static final String HELP = "help";

        protected static final String STRENGTH = "strength";
        protected static final String ASSETS = "assets";
        protected static final String TOWER = "tower";
        protected static final String CARD = "card";
        protected static final String LEADER = "leader";
        protected static final String PERSONAL_BOARD = "personal-board";

        protected static final String HELP_CHAT = "Usage: chat [MESSAGE].\nBroadcast a message to all client in the room";
        protected static final String HELP_TURN = "Usage: turn.\nShow which user is playing";
        protected static final String HELP_SHOW = "Usage: show [strength,assets,tower,card,leader,personal-board] [username]\nShow information about a specific user";
=======
    public void updateGame(Board board, ArrayList<Player> players) {
        localBoard = board;
        localPlayers = players;
>>>>>>> 1108a337f37cd579aa445fe69fb707bdbe65751d
    }
}
