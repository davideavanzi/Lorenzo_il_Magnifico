package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.ServerInterface;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 */
public class UIController {
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
     * Updated game board.
     */
    private static Board localBoard;
    /**
     * List of player in the same Game, sort by turn order.
     */
    private static ArrayList<Player> localPlayers;
    /**
     * User's nickname.
     */
    private String username;
    /**
     * If true it indicates my turn.
     */
    private boolean isMyTurn = false;

    /**
     * It store the temporary variables of socket communication
     */
    private TemporaryVariables tmpVar;

    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     */
    public UIController(/*String ui*/) {
        /*if(ui.equals("gui")) {
            //clientUI = new GUI();
        } else {
            clientUI = new CLI(this);
        }*/
        clientUI = new CLI(this);
        tmpVar = new TemporaryVariables();
    }

    /**
     * Calling this method the UI controller is link with the client socket.
     * @param clientProtocol
     */
    public static void setClientProtocol(ServerInterface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }

    /**
     * GUI not implemented yet.
     * @return the chosen User Interface
     */
    public static String setUI() {
        System.out.println("Choose your user interface: GUI or CLI (default)");
        return userInput.nextLine().toLowerCase();
    }

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

    public TemporaryVariables getTmpVar() {
        return tmpVar;
    }

    /**
     * Every turn the board is send to all client.
     * @param board
     */
    public void updateBoard(Board board) {
        System.out.println("BOARD RECEIVED!");
        localBoard = board;
    }

    /**
     * Every turn the player's list is send to all client.
     * @param players
     */
    public void updatePlayers(ArrayList<Player> players) {
        localPlayers = players;
        getClientUI().printGameBoard();
    }

    Player getPlayer(String username) {
        return getLocalPlayers().stream().filter(pl -> pl.getNickname().equals(username)).findFirst().orElse(null);
    }

    /**
     * Send to server a bonus harvest action.
     * @param servantDeployes num of servants deployed
     */
    void sendFastHarvest(int servantDeployes) {
        try {
            clientProtocol.fastHarvest(servantDeployes);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server a bonus production action.
     * @param servantDeployes num of servants deployed
     */
    void sendFastProduction(int servantDeployes) {
        try {
            clientProtocol.fastProduction(servantDeployes);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server a bonus tower action.
     * @param servantsDeployed num of servants deployed
     * @param towerColor the tower's color
     * @param floor the tower's floor
     */
    void sendFastTowerMove (int servantsDeployed, String towerColor, int floor) {
        try {
            clientProtocol.fastTowerMove(servantsDeployed, towerColor, floor);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server the production that the player want activate.
     * @param prodChoice ArrayList contain the index of the production to activate
     */
    void sendProductionOption(ArrayList<Integer> prodChoice) {
        try {
            clientProtocol.productionOption(prodChoice);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server the optional battle point payment for purple card.
     * @param bpPayment if the player want to pay with BP is set to true
     */
    void sendOptionalBpPick(boolean bpPayment) {
        try {
            clientProtocol.optionalBpPick(bpPayment);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server the council favor choice.
     * @param favorChoice
     */
    void sendFavorChoice(ArrayList<Integer> favorChoice) {
        try {
            clientProtocol.favorChoice(favorChoice);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send to server the player's excommunication choice.
     * @param choice boolean
     */
    void sendExcommunicationChoice(boolean choice) {
        try {
            clientProtocol.excommunicationChoice(choice);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send place familiar member command to server.
     * @param color familiar color
     * @param destination where put the familiar
     * @param servants the servants' number used to deploy familiar
     */
    void sendPlaceFM(String color, ArrayList<String> destination, String servants) {
        try {
            clientProtocol.placeFM(color, destination, servants);
        } catch (ClientNetworkException e) {
            clientUI.printError(e.getMessage());
        }
    }

    /**
     * Send chat message to server with the selected protocol.
     * @param message
     */
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
        getClientUI().printMessageln("Hi player!");
        String protocol = clientUI.setNetworkSettings();
        if ("socket".equals(protocol) ){
            new Thread(new SocketClient(this)).start();
        } else if ("rmi".equals(protocol)) {
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
         * This is a list of card, in each card there is a list of Assets' Arrays.
         * The Assets contains in the first slot the cost(of production), in the second there is the result of the production.
         */
        private ArrayList<ArrayList<Object[]>> optionsProd;
        /**
         * This HashMap contains the information about a tower (color and floor's number)
         */
        private HashMap<String, Integer> tower;
        /**
         * It contains the specific assets of the discount.
         */
        private Assets optionalPickDiscount;
        /**
         * It store the minimum number of servants that the player must deploy for a specific production.
         */
        private int minServantsProd;
        /**
         * It store the minimum number of servants that the player must deploy for a specific harvest.
         */
        private int minServantsHarv;
        /**
         * The list of copyable leader by Lorenzo De Medici.
         */
        private ArrayList<String> copyableLeaders;

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

        public ArrayList<ArrayList<Object[]>> getOptionsProd() {
            return optionsProd;
        }

        public void setOptionsProd(ArrayList<ArrayList<Object[]>> optionsProd) {
            this.optionsProd = optionsProd;
        }

        public HashMap<String, Integer> getTower() {
            return tower;
        }

        public void setTower(HashMap<String, Integer> tower) {
            this.tower = tower;
        }

        public Assets getAssets() {
            return optionalPickDiscount;
        }

        public void setAssets(Assets optionalPickDiscount) {
            this.optionalPickDiscount = optionalPickDiscount;
        }

        public int getMinServantsProd() {
            return minServantsProd;
        }

        public void setMinServantsProd(int minServantsProd) {
            this.minServantsProd = minServantsProd;
        }

        public int getMinServantsHarv() {
            return minServantsHarv;
        }

        public void setMinServantsHarv(int minServantsHarv) {
            this.minServantsHarv = minServantsHarv;
        }

        public ArrayList<String> getCopyableLeaders() {
            return copyableLeaders;
        }

        public void setCopyableLeaders(ArrayList<String> copyableLeaders) {
            this.copyableLeaders = copyableLeaders;
        }
    }

    /**
     * Constants used by UI.
     */
    class UIConstant {

        /**
         * Command
         */
        static final String CHAT = "chat";
        static final String TURN = "showTurn";
        static final String INFO = "showPersonalInfo";
        static final String CARD = "showCard";
        static final String BOARD = "showBoard";
        static final String ALL_PLAYER_INFO = "showAllPlayerInfo";
        static final String FAMILY_MEMBER = "putFamilyMember";
        static final String LEADER_CARD = "leaderCard";
        static final String EXCOMMUNICATION = "excommunication";
        static final String CHOOSE_FAVOR = "askForCouncilFavor";
        static final String OPTIONAL_BP_PICK = "purpleCardPayment";
        static final String CHOOSE_PRODUCTION = "chooseProduction";
        static final String SERVANTS_PRODUCTION = "servantsProduction";
        static final String SERVANTS_HARVEST = "servantsHarvest";
        static final String PICK_FROM_TOWER = "pickFromTower";

        /**
         * Command description
         */
        static final String CHAT_DESCR = "Broadcast a message to all client in your room";
        static final String TURN_DESCR = "Show the round order in this turn";
        static final String INFO_DESCR = "Show the personal information of a specific player";
        static final String CARD_DESCR = "Show information about a specific card";
        static final String BOARD_DESCR = "Show the game board";
        static final String ALL_PLAYER_INFO_DESCR = "Show the personal board of all board";
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