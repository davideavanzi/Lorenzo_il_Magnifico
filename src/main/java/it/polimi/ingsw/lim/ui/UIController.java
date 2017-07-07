package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.cards.PurpleCard;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

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
     * All command are stored here.
     */
    static Map<String, Runnable> cmdList;

    /**
     * Map that link a identification string to a method.
     * Only available commands are stored in this hashMap.
     */
    static Map<String, Runnable> availableCmdList;

    /**
     * Map that link a identification string to a description of the command.
     */
    static  Map<String, String> cmdDescr;

    /**
     * If true it indicates my turn.
     */
    private boolean isMyTurn = false;

    /**
     *
     */
    private TemporaryVariables tmpVar;

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
        tmpVar = new TemporaryVariables();
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

    Board getLocalBoard(){
        return localBoard;
    }

    ArrayList<Player> getLocalPlayers() { return localPlayers; }

    boolean getIsMyTurn() { return isMyTurn; }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public TemporaryVariables getTmpVar() {
        return tmpVar;
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

    Player getPlayer(String username) {
        return getLocalPlayers().stream().filter(pl -> pl.getNickname().equals(username)).findFirst().orElse(null);
    }

    void sendProductionOption(ArrayList<Integer> prodChoice) {
        try {
            clientProtocol.productionOption(prodChoice, username);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    void sendOptionalBpPick(boolean bpPayment) {
        try {
            clientProtocol.optionalBpPick(bpPayment, username);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    void sendFavorChoice(ArrayList<Integer> favorChoice) {
        try {
            clientProtocol.favorChoice(favorChoice, username);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    void sendExcommunicationChoice(boolean choice) {
        try {
            clientProtocol.excommunicationChoice(choice, username);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    void sendPlaceFM(String color, ArrayList<String> destination, String servants) {
        try {
            clientProtocol.placeFM(color, destination, servants, username);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    void sendChatMessage(String message) {
        try {
            clientProtocol.sendChatMessageToServer(username, message);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Until the user doesn't log in correctly keep on asking to enter the credential (username and password).
     */
    public String[] sendLoginInfo() {
        String[] loginInformation = clientUI.loginForm();
        this.username = loginInformation[0];
        return loginInformation;
    }

    /**
     *  Connect the client to the server with the previously chosen protocol.
     */
    public void startGame() {
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
                clientUI.printError(e.getMessage());
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
     * This class is used to save the variables sent by the server in the first part of communication,
     * in this way we avoid a continuous data change from server to client and viceversa creating
     * a single packet per command that is sent once.
     */
    public class TemporaryVariables {

        /**
         * Represent the number of favor that the user can choose.
         */
        private int favorAmount;

        /**
         * Getters.
         * @return favorAmount.
         */
        int getFavorAmount() { return favorAmount; }

        /**
         * Setters.
         * @param favorAmount favor's number.
         */
        public void setFavorAmount(int favorAmount) {
            this.favorAmount = favorAmount;
        }

        /**
         * This is a list of card, for each card there is a list of Assets' Arrays.
         * The Assets contains in the first slot the cost, in the second there is the result.
         */
        private ArrayList<ArrayList<Object[]>> options;

        /**
         * Getters.
         * @return options
         */
        public ArrayList<ArrayList<Object[]>> getOptions() {
            return options;
        }

        /**
         * Setters.
         * @param options
         */
        public void setOptions(ArrayList<ArrayList<Object[]>> options) {
            this.options = options;
        }
    }

    /**
     * Constants used by UI.
     */
    class UIConstant {
        //Command
        static final String CHAT = "chat";
        static final String TURN = "showTurn";
        static final String INFO = "showPersonalInfo";

        static final String CARD = "showCard"; //todo chiedere utente(username, title) O torre(colore, floor)
        static final String FAMILY_MEMBER = "putFamilyMember";
        static final String LEADER_CARD = "leaderCard";
        static final String EXCOMMUNICATION = "excommunication";
        static final String CHOOSE_FAVOR = "askForCouncilFavor";
        static final String OPTIONAL_BP_PICK = "purpleCardPayment";
        static final String CHOOSE_PRODUCTION = "chooseProduction";
        static final String SERVANTS_PRODUCTION = "servantsProduction";
        static final String SERVANTS_HARVEST = "servantsHarvest";

        static final String PICK_FROM_TOWER = "pickFromTower";
        //Command description
        static final String CHAT_DESCR = "Broadcast a message to all client in your room";
        static final String TURN_DESCR = "Show the round order in this turn";
        static final String INFO_DESCR = "Show the personal information of a specific player";

        static final String CARD_DESCR = "Show information about a specific card";
        static final String FAMILY_MEMBER_DESCR = "Place a family member on the board";
        static final String LEADER_CARD_DESCR = "Choose if discard or use a leader card";
        static final String EXCOMMUNICATION_DESCR = "Choose if you want take a excommunication";
        static final String CHOOSE_FAVOR_DESCR = "Choose a favor from council";
        static final String OPTIONAL_BP_PICK_DESCR = "You have taken a purple card! How do you want to pay it?";
        static final String CHOOSE_PRODUCTION_DESCR = "Choose what type of production you want activate";
        static final String SERVANTS_PRODUCTION_DESCR = "Choose how many servants do you want to put in the production";
        static final String SERVANTS_HARVEST_DESCR = "Choose how many servants do you want to put in the harvest";
        static final String PICK_FROM_TOWER_DESCR = "From which tower do you want to pick the card";

    }
}