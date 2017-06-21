package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.network.client.MainClient;
import it.polimi.ingsw.lim.ui.UIController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

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
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(" ")));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equals(LOGIN_SUCCESSFUL)) {
                uiCallback.getClientUI().printMessageln("Login successful!");
            } else if(commandIdentifier.equals(ASK_SERVANTS_AMOUNT)) {

            } else if(commandIdentifier.equals(CHAT)) {
                //The server has received a chat message from the client, it has to deliver it to other room mates.
                uiCallback.getClientUI().printMessageln("[CHAT] message from "+command.get(1)+": "+command.get(2));
            }
        }
    }
}