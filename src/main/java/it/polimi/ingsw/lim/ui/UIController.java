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
    /**
     * The abstract UI of the client.
     */
    private static AbsUI clientUI;

    /**
     * The interface for communicate with server.
     */
    private static ServerInterface clientProtocol;

    /**
     * Scanner for stdin.
     */
    private static Scanner userInput = new Scanner(System.in);

    /**
     * User's nickname.
     */
    private static String username;

    /**
     * Updated game board.
     */
    private static Board localBoard;

    /**
     * List of player in the same Game, sort by turn order.
     */
    private static ArrayList<Player> localPlayers;


    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param ui this String represent the User Interface chosen.
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

    /**
     * @return the abstract UI of client.
     */
    public AbsUI getClientUI() {
        return clientUI;
    }

    /**
     * Every turn the board is send to all client.
     * @param board
     */
    public void updateBoard(Board board) {
        localBoard = board;
    }

    /**
     * Every turn the player's list is send to all client.
     * @param players
     */
    public void updatePlayers(ArrayList<Player> players) {
        localPlayers = players;
    }

    /**
     * Calling this method the UI controller is link with the client socket.
     * @param clientProtocol
     */
    public static void setClientProtocol(ServerInterface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }

    public void inputHandler() {
        UIController.clientUI.waitUserInput();
    }

    /**
     * The input's parser.
     * @param input the command
     */
    static void inputParser(String input) {
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

    /**
     * The show command parameters manager.
     * @param command
     */
    private static void showCommand(ArrayList<String> command) {
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

    /**
     * The help message.
     */
    private static void help() {
        clientUI.printMessageln("Command List:");
        clientUI.printMessageln(CHAT);
        clientUI.printMessageln(HELP_CHAT);
        clientUI.printMessageln(TURN);
        clientUI.printMessageln(HELP_TURN);
        clientUI.printMessageln(SHOW);
        clientUI.printMessageln(HELP_SHOW);
    }

    /**
     * @return the player if it found in the player's ArrayList.
     * @return null if it could not found the player.
     */
    private static Player lookForPlayer() {
        for (Player pl : localPlayers)
            if (username.equalsIgnoreCase(pl.getNickname()))
                return pl;
        return null;
    }

    /**
     * Print the currently turn order.
     */
    private static void turnOrder() {
        clientUI.printMessageln("Turn order: ");
        for (Player pl : localPlayers)
            clientUI.printMessageln(pl.getNickname());
    }

    /**
     * Send a chat message to the server.
     * @param message the chat message
     */
    private static void chat(String message) {
        try {
            clientProtocol.chatMessageToServer(username, message);
        }catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    /**
     * It required a username and a password for the authentication.
     */
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
     *  Connect the client to the server with the previously chosen protocol.
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
     * @return the chosen User Interface.
     */
    public static String setUI() {
        System.out.println("Choose your user interface: GUI, MORSE or CLI (default)");
        System.out.print("$ ");
        return userInput.nextLine().toLowerCase();
    }

    /**
     * Constants used by UI controller.
     */
    class UIConstant {

        static final String SPACE = " ";

        protected static final String CHAT = "chat";
        static final String SHOW = "show";
        static final String TURN = "turn";
        protected static final String INFO = "info";
        static final String BOARD = "board";
        static final String HELP = "help";


        static final String HELP_CHAT = "Usage: chat [MESSAGE].\nBroadcast a message to all client in the room";
        static final String HELP_TURN = "Usage: turn\nShow which user is playing";
        static final String HELP_SHOW = "Usage: show [turn,board,help]\n" +
                                        "Show the turn order, the game board or this help message\n" +
                                        "       show [info] [username]\n" +
                                        "Show personal information";
    }
}
