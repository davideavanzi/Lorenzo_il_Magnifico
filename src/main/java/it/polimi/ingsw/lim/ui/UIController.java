package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;

import java.util.ArrayList;
import java.util.Map;
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
    private String username;

    /**
     * Updated game board.
     */
    private static Board localBoard;

    /**
     * List of player in the same Game, sort by turn order.
     */
    private static ArrayList<Player> localPlayers;

    /**
     * Map that link a identification string to a method.
     * Only available commands are in this hashMap.
     */
    static Map<String, Runnable> availableCmdList;

    /**
     * Map that link a identification string to a description of the command.
     */
    static Map<String, String> cmdDescr;

    /**
     * If true it indicates my turn.
     */
    private boolean isMyTurn = false;

    /**
     * It's show if I'm logged.
     */
    private boolean amILogged = false;

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
            clientUI = new CLI(this);
        }
    }

    public Board getLocalBoard(){
        return localBoard;
    }

    /**
     * @return the abstract UI of client.
     */
    public AbsUI getClientUI() {
        return clientUI;
    }

    public String getUsername() {
        return username;
    }

    static ArrayList<Player> getLocalPlayers() { return localPlayers; }

    public boolean getIsMyTurn() { return isMyTurn; }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public void setAmILogged(boolean amILogged) { this.amILogged = amILogged; }

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

    void sendChatMessage(String message) {
        try {
            clientProtocol.chatMessageToServer(username, message);
        } catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    public void manageCmd(String command) {
        clientUI.cmdManager(command);
    }

    public void startGame() {
        clientUI.waitForRequest();
    }

    /**
     * Until the user doesn't log in correctly keep on asking to enter the credential (username and password).
     */
    public void login() {
        while (!amILogged) {
            String username = clientUI.loginForm(USERNAME);
            String password = clientUI.loginForm(PASSWORD);
            this.username = username;
            try {
                clientProtocol.sendLogin(username, password);
                setAmILogged(LOGIN_SUCCESSFUL);
            } catch (ClientNetworkException | LoginFailedException e) {
                clientUI.printMessageln(e.getMessage());
            }
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

        //Command
        static final String CHAT = "chat";
        static final String TURN = "showTurn";
        static final String INFO = "showPersonalInfo";

        static final String FAMILY_MEMBER = "putFamilyMember";
        static final String LEADER_CARD = "chooseLeaderCard";
        static final String EXCOMMUNICATION = "excommunication";
        static final String CHOOSE_FAVOR = "chooseFavor";
        static final String CHOOSE_TOWER = "chooseTower";
        static final String CHOOSE_FLOOR = "chooseFloor";
        static final String CHOOSE_PRODUCTION = "chooseProduction";

        //Command description
        static final String CHAT_DESCR = "Broadcast a message to all client in the room";
        static final String TURN_DESCR = "Show the turn order";
        static final String INFO_DESCR = "Show the personal information of a specific player";

        static final String FAMILY_MEMBER_DESCR = "Place a family member on the board";
        static final String LEADER_CARD_DESCR = "Choose and use a leader card";
        static final String EXCOMMUNICATION_DESCR = "Choose if you want take a excommunication";
        static final String CHOOSE_FAVOR_DESCR = "Choose a favor from council";
        static final String CHOOSE_TOWER_DESCR = "Choose a tower to pick a card from";
        static final String CHOOSE_FLOOR_DESCR = "Choose the tower's floor ";
        static final String CHOOSE_PRODUCTION_DESCR = "Choose what type of production you want activate";

        static final String USERNAME = "username";
        static final String PASSWORD = "password";

        static final boolean LOGIN_SUCCESSFUL = true;
    }
}