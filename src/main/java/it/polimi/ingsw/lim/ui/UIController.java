package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.InvalidInputException;
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
    private static Board localBoard;
    private static ArrayList<Player> localPlayers;


    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param ui
     */
    public UIController(String ui) {
        if(ui.equals("gui")) {
            //clientUI = new GUI();
        } else if (ui.equals("morse")){
            //clientUI = new MorseLI();
        } else {
            clientUI = new CLI();
        }
    }

    public AbsUI getClientUI() {
        return clientUI;
    }

    public void updateBoard(Board board) {
        localBoard = board;
    }

    public void updatePlayers(ArrayList<Player> players) {
        localPlayers = players;
    }

    public static void setClientProtocol(ServerInterface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }

    public void inputHandler() {
        UIController.clientUI.waitUserInput();
    }

    public static void inputParser(String input) {
        ArrayList<String> commandInput = new ArrayList<>(Arrays.asList(input.split(SPACE)));
        String command = commandInput.get(0);
        try {
            if (commandInput.size() < 2)
                throw new InvalidInputException("[INPUT_PARSER]: Command parameters not found");

                switch (command) {
                    case CHAT:
                        chat(input.split(SPACE, 2)[1]);
                        break;
                    case SHOW:
                        showCommand(commandInput);
                        break;
                    default:
                        throw new InvalidInputException(("[INPUT_PARSER]: command not found: ").concat(command));
                }
        } catch (InvalidInputException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }
/*
    public static void inputParserMod(String input) {
        ArrayList<String> parameters = new ArrayList<>(Arrays.asList(input.split(SPACE)));
        String commandID = parameters.remove(0);
        HashMap<String, ArrayList<String>> command = new HashMap<>();
        command.put(CHAT, parameters);
        command.put(TURN, parameters);
        command.put(SHOW, parameters);
        command.put(HELP, null);
    }
*/
    public static void showCommand(ArrayList<String> command) {
        String parameter = command.get(1);
        String username = command.get(2);
        try {
            switch (parameter) {
                case TURN:
                    turnOrder();
                    break;
                case INFO:
                    if (lookForPlayer() == null)
                        throw new InvalidInputException(("[INPUT_PARSER]: Username not found in your room :").concat(username));

                    break;
                case BOARD:

                    break;
                case HELP:
                    help();
                    break;
                default:
                    throw new InvalidInputException(("[INPUT_PARSER]: show command parameters not found: ").concat(parameter));
            }
        } catch (InvalidInputException e) {
            clientUI.printMessageln(e.getMessage());
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

    private static Player lookForPlayer() {
        for (Player pl : localPlayers)
            if (username.equalsIgnoreCase(pl.getNickname()))
                return pl;
        return null;
    }

    private static void turnOrder() {
        clientUI.printMessageln("Turn order: ");
        for (Player pl : localPlayers)
            clientUI.printMessageln(pl.getNickname());
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
    public static String setUI() {
        System.out.println("Choose your user interface: GUI, MORSE or CLI (default)");
        System.out.print("$ ");
        return userInput.nextLine().toLowerCase();
    }

    class UIConstant {

        protected static final String SPACE = " ";

        protected static final String CHAT = "chat";
        protected static final String SHOW = "show";
        protected static final String TURN = "turn";
        protected static final String INFO = "info";
        protected static final String BOARD = "board";
        protected static final String HELP = "help";


        protected static final String HELP_CHAT = "Usage: chat [MESSAGE].\nBroadcast a message to all client in the room";
        protected static final String HELP_TURN = "Usage: turn\nShow which user is playing";
        protected static final String HELP_SHOW = "Usage: show [turn,board,help]\n" +
                                                  "Show the turn order, the game board or this help message\n" +
                                                  "       show [info] [username]\n" +
                                                  "Show personal information";
    }
}
