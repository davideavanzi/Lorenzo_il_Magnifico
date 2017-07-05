package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.ui.UIController;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.lim.network.CommunicationConstants.*;

/**
 * Created by ava on 20/06/17.
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
     * @param handlerCallback
     * @param uiCallback
     */
    ServerCommandHandler(SocketClient handlerCallback, UIController uiCallback) {
        this.clientCallback = handlerCallback;
        this.uiCallback = uiCallback;
    }

    /**
     * Parse the input object for calling the correspondent method.
     * @param obj
     */
    void requestHandler(Object obj) throws LoginFailedException {
        if (obj instanceof String) {
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equalsIgnoreCase(LOGIN_REQUEST)) {
                String[] loginInfo = uiCallback.sendLoginInfo();
                clientCallback.login(loginInfo[0], loginInfo[1]);
            } else if (commandIdentifier.equalsIgnoreCase(LOGIN_SUCCESSFUL)) {
                uiCallback.getClientUI().waitForRequest();
            } else if (commandIdentifier.equalsIgnoreCase(LOGIN_FAILED)) {
                uiCallback.getClientUI().printError(command.get(1));
            } else if (commandIdentifier.equalsIgnoreCase(TURN)) {
                uiCallback.setIsMyTurn(Boolean.valueOf(command.get(1)));
            } else if (commandIdentifier.equalsIgnoreCase(CHAT)) {
                uiCallback.getClientUI().printChatMessage(command.get(1), command.get(2));
            } else if (commandIdentifier.equalsIgnoreCase(GAME_MSG)) {
                uiCallback.getClientUI().printGameMessage(command.get(1));
            } else if (commandIdentifier.equalsIgnoreCase(EXCOMMUNICATION)) {
                uiCallback.getClientUI().commandAdder(command.get(0));
            } else if (commandIdentifier.equalsIgnoreCase(CHOOSE_FAVOR)) {
                uiCallback.getClientUI().commandAdder(command.get(0));
            } else if (commandIdentifier.equalsIgnoreCase(CHOOSE_PRODUCTION)) {
                uiCallback.getClientUI().commandAdder(command.get(0));
            } else if (commandIdentifier.equalsIgnoreCase(CHOOSE_TOWER)) {
                uiCallback.getClientUI().commandAdder(command.get(0));
            } else if (commandIdentifier.equalsIgnoreCase(CMD_VALIDATOR)) {
                uiCallback.getClientUI().commandManager(command.get(1), command.get(2), Boolean.valueOf(command.get(3)));
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