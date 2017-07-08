package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.ui.UIController;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

/**
 *
 */
class ServerCommandHandler {

    /**
     * Socket client's reference.
     */
    private SocketClient clientCallback;

    /**
     * UI controller's reference.
     */
    private UIController uiCallback;

    Object[] command;

    /**
     * Constructor.
     * @param clientCallback
     * @param uiCallback
     */
    ServerCommandHandler(SocketClient clientCallback, UIController uiCallback) {
        this.clientCallback = clientCallback;
        this.uiCallback = uiCallback;
    }

    /**
     * Parse the input object for calling the correspondent method.
     * @param obj
     */
    void requestHandler(Object obj) throws LoginFailedException {
        if (obj instanceof Object[]) {
            command = (Object[]) obj;
            String commandID = (String) command[0];
            if (commandID.equals(LOGIN_REQUEST)) {
                String[] loginInfo = uiCallback.sendLoginInfo();
                try {
                    clientCallback.login(loginInfo[0], loginInfo[1]); //login(username, password);
                } catch (ClientNetworkException e) {
                    uiCallback.getClientUI().printError(e.getMessage());
                }
            } else if (commandID.equals(LOGIN_SUCCESSFUL)) {
                uiCallback.getClientUI().getLock().unlock();
            } else if (commandID.equals(LOGIN_FAILED)) {
                uiCallback.getClientUI().printError((String)command[1]);
            } else if (commandID.equals(TURN)) {
                uiCallback.setIsMyTurn((Boolean)command[1]);
            } else if (commandID.equals(CHAT)) {
                uiCallback.getClientUI().printChatMessage((String)command[1], (String)command[2]);
            } else if (commandID.equals(GAME_MSG)) {
                uiCallback.getClientUI().printGameMessage((String)command[1]);
            } else if (commandID.equals(EXCOMMUNICATION)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(CHOOSE_FAVOR)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setFavorAmount((Integer)command[1]);
            } else if (commandID.equals(OPTIONAL_BP_PICK)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(CHOOSE_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setOptionsProd((ArrayList<ArrayList<Object[]>>)command[1]);
            } else if (commandID.equals(PICK_FROM_TOWER)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setTower((HashMap<String,Integer>)command[1]);
                uiCallback.getTmpVar().setAssets((Assets)command[1]);
            } else if (commandID.equals(SERVANTS_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setMinServantsProd((Integer)command[1]);
            } else if (commandID.equals(SERVANTS_HARVEST)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setMinServantsHarv((Integer)command[1]);
            } else if (commandID.equals(CMD_VALIDATOR)) {
                uiCallback.getClientUI().commandManager((String)command[1], (String)command[2], (Boolean)command[3]);
            } else if (commandID.equals(BOARD)) {
                uiCallback.updateBoard((Board)command[1]);
            } else if (commandID.equals(PLAYERS)) {
                uiCallback.updatePlayers((ArrayList<Player>)command[1]);
            } else {
                System.out.println(commandID);
            }
        }

        /*else if (obj instanceof Board) {
            System.out.println("Entro in Board");
            Board board = (Board)obj;
            uiCallback.updateBoard(board);
        } else {
            System.out.println("Entro in Arraylistplayer");
            ArrayList<Player> players = (ArrayList<Player>)obj;
            uiCallback.updatePlayers(players);
        }*/
    }
}