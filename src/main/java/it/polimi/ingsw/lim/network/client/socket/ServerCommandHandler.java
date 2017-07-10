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
import static it.polimi.ingsw.lim.ui.UIController.UIConstant.*;

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
            } else if (commandID.equals(START_GAME)) {
                uiCallback.getClientUI().notifyStartGame();
            } else if (commandID.equals(CHOOSE_LEADER_DRAFT)) {
                uiCallback.getClientUI().commandAdder(CHOOSE_LEADER_DRAFT_CMD);
                uiCallback.getTmpVar().setLeaderOptions((ArrayList<Integer>)command[1]);
            } else if (commandID.equals(TURN)) {
                uiCallback.getClientUI().notifyStartRound((Boolean)command[1]);
            } else if (commandID.equals(CHAT)) {
                uiCallback.getClientUI().printChatMessage((String)command[1], (String)command[2]);
            } else if (commandID.equals(GAME_MSG)) {
                uiCallback.getClientUI().printGameMessage((String)command[1]);
            } else if (commandID.equals(EXCOMMUNICATION)) {
                uiCallback.getClientUI().commandAdder(EXCOMMUNICATION_CMD);
            } else if (commandID.equals(CHOOSE_FAVOR)) {
                uiCallback.getClientUI().commandAdder(CHOOSE_FAVOR_CMD);
                uiCallback.getTmpVar().setFavorAmount((Integer)command[1]);
            } else if (commandID.equals(OPTIONAL_BP_PICK)) {
                uiCallback.getClientUI().commandAdder(OPTIONAL_BP_PICK_CMD);
            } else if (commandID.equals(CHOOSE_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(CHOOSE_PRODUCTION_CMD);
                uiCallback.getTmpVar().setOptionsProd((ArrayList<ArrayList<Object[]>>)command[1]);
            } else if (commandID.equals(PICK_FROM_TOWER)) {
                uiCallback.getClientUI().commandAdder(PICK_FROM_TOWER_CMD);
                uiCallback.getTmpVar().setTower((HashMap<String,Integer>)command[1]);
                uiCallback.getTmpVar().setAssets((Assets)command[1]);
            } else if (commandID.equals(SERVANTS_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(SERVANTS_PRODUCTION_CMD);
                uiCallback.getTmpVar().setMinServantsProd((Integer)command[1]);
            } else if (commandID.equals(SERVANTS_HARVEST)) {
                uiCallback.getClientUI().commandAdder(SERVANTS_HARVEST_CMD);
                uiCallback.getTmpVar().setMinServantsHarv((Integer)command[1]);
            } else if (commandID.equals(CMD_VALIDATOR)) {
                uiCallback.getClientUI().commandManager((String)command[1], (String)command[2], (Boolean)command[3]);
            } else if (commandID.equals(BOARD)) {
                uiCallback.updateBoard((Board)command[1]);
            } else if (commandID.equals(PLAYERS)) {
                uiCallback.updatePlayers((ArrayList<Player>)command[1]);
            } else if (commandID.equals(LORENZO_MEDICI)) {
                uiCallback.getClientUI().commandAdder(LORENZO_MEDICI_CMD);
                uiCallback.getTmpVar().setCopyableLeaders((ArrayList<String>)command[1]);
            } else if (commandID.equals(LUDOVICO_MONTEFELTRO)) {
                uiCallback.getClientUI().commandAdder(LORENZO_MONTEFELTRO_CMD);
            } else if (commandID.equals(END_GAME)) {
                uiCallback.getClientUI().endGameMessage((ArrayList<Player>)command[1]);
            } else {
                uiCallback.getClientUI().printError("Command not found: ".concat(commandID));
            }
        }

        /*else if (obj instanceof Board) {
            Board board = (Board)obj;
            uiCallback.updateBoard(board);
        } else {
            ArrayList<Player> players = (ArrayList<Player>)obj;
            uiCallback.updatePlayers(players);
        }*/
    }
}