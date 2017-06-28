package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailException;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.SocketConstants.*;
import static it.polimi.ingsw.lim.network.server.MainServer.addUserToRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created by nico.
 */
class ClientCommandHandler {

    /**
     * Link every SocketClientHandler to the corrispondent Client Command Handler.
     * @param handlerCallback
     */
    ClientCommandHandler(SocketClientHandler handlerCallback) {
        this.handlerCallback = handlerCallback;
    }

    /**
     * User's reference.
     */
    SocketUser user;

    /**
     * A callback to the client handler
     */
    private SocketClientHandler handlerCallback;

    /**
     * Parse the input object for calling the corrispondent method.
     * @param obj
     */
    void requestHandler(Object obj) throws ClientNetworkException {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            getLog().log(Level.INFO,() -> "[C-H] Handling command: "+obj);
            if (commandIdentifier.equals(LOGIN)) {
                try {
                    handlerCallback.login(command.get(1), command.get(2), handlerCallback);
                }
                catch (LoginFailException e){
                    throw new ClientNetworkException("[SOCKET]: Login failed", e);
                }
            } else if(commandIdentifier.equals(TURN_ORDER)) {

            } else if (commandIdentifier.equals(CHAT)) {
                this.user.getRoom().chatMessageToRoom(command.get(1), command.get(2));
            } else {
                getLog().log(Level.SEVERE, () ->"[C-H] invalid message indentifier: "+commandIdentifier);
            }
        }

    }
}
