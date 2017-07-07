package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.ui.UIController;

import java.util.ArrayList;

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
        if (obj instanceof String[]) {
            Object[] cmd = (Object[])obj;
            String[] command = (String[])cmd;
            String commandID = (String)cmd[0];
            if (commandID.equals(LOGIN_REQUEST)) {
                String[] loginInfo = uiCallback.sendLoginInfo();
                try {
                    clientCallback.login(loginInfo[0], loginInfo[1]); //login(username, password);
                } catch (ClientNetworkException e) {
                    uiCallback.getClientUI().printError(e.getMessage());
                }
            } else if (commandID.equals(LOGIN_SUCCESSFUL)) {
                uiCallback.getClientUI().waitForRequest();
            } else if (commandID.equals(LOGIN_FAILED)) {
                uiCallback.getClientUI().printError(command[1]);
            } else if (commandID.equals(TURN)) {
                uiCallback.setIsMyTurn(Boolean.valueOf(command[1]));
            } else if (commandID.equals(CHAT)) {
                uiCallback.getClientUI().printChatMessage(command[1], command[2]);
            } else if (commandID.equals(GAME_MSG)) {
                uiCallback.getClientUI().printGameMessage(command[1]);
            } else if (commandID.equals(EXCOMMUNICATION)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(CHOOSE_FAVOR)) {
                uiCallback.getClientUI().commandAdder(commandID);
                uiCallback.getTmpVar().setFavorAmount(Integer.parseInt(command[1]));
            } else if (commandID.equals(OPTIONAL_BP_PICK)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(CHOOSE_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(PICK_FROM_TOWER)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(SERVANTS_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(SERVANTS_HARVEST)) {
                uiCallback.getClientUI().commandAdder(commandID);
            } else if (commandID.equals(CMD_VALIDATOR)) {
                uiCallback.getClientUI().commandManager(command[1], command[2], Boolean.valueOf(command[3]));
            }
        } else if (obj instanceof Board) {
            Board board = (Board)obj;
            uiCallback.updateBoard(board);
        } else if (obj instanceof Player) {
            ArrayList<Player> players = new ArrayList<>(); //TODO: how can i convert obj in an arrayList of player
            uiCallback.updatePlayers(players);
        }
    }
}