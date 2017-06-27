package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.ui.UIController;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.lim.network.SocketConstants.*;

/**
 * Created by ava on 20/06/17.
 */
class ServerCommandHandler {

    private SocketClient clientCallback;
    private UIController uiCallback;

    ServerCommandHandler(SocketClient handlerCallback, UIController uiCallback) {
        this.clientCallback = handlerCallback;
        this.uiCallback = uiCallback;
    }

    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equalsIgnoreCase(LOGIN_SUCCESSFUL)) {
                uiCallback.getClientUI().printMessageln("Login successful!");
            } else if(commandIdentifier.equalsIgnoreCase(TURN_ORDER)) {

            } else if(commandIdentifier.equalsIgnoreCase(CHAT)) {
                //The server has received a chat message from the client, it has to deliver it to other room mates.
                uiCallback.getClientUI().printChatMessage(command.get(1), command.get(2));
            }
        } else if (obj instanceof Board) {
            Board board = (Board)obj;
            uiCallback.updateBoard(board);
        } else if (obj instanceof Player) {//TODO: is it correct?
            ArrayList<Player> players = new ArrayList<>(); //TODO: how can i convert obj in an arrayList of player
            uiCallback.updatePlayers(players);
        }
    }
}