package it.polimi.ingsw.lim.network.client.socket;

import it.polimi.ingsw.lim.network.client.MainClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.network.SocketConstants.*;
import static it.polimi.ingsw.lim.network.client.MainClient.getClientUI;

/**
 * Created by ava on 20/06/17.
 */
class ServerCommandHandler {

    ServerCommandHandler(SocketClient handlerCallback, MainClient uiCallback) {
        this.clientCallback = handlerCallback;
        this.uiCallback = uiCallback;
    }

    private  SocketClient clientCallback;
    //TODO: do we need this?
    private MainClient uiCallback;

    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<String>(Arrays.asList(((String) obj).split(" ")));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equals(LOGIN_SUCCESSFUL)) {
                getClientUI().printMessageln("Login successful!");
            } else if(commandIdentifier.equals(ASK_SERVANTS_AMOUNT)) {

            } else if (commandIdentifier.equals(CHAT)) {
                //The server has received a chat message from the client, it has to deliver it to other room mates.
                getClientUI().printMessageln("[CHAT] message from "+command.get(1)+": "+command.get(2));
            }
        }
    }
}