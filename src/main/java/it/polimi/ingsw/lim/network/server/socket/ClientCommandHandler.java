package it.polimi.ingsw.lim.network.server.socket;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.ServerConstants.*;

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
     * A callback to the client handler
     */
    private SocketClientHandler handlerCallback;

    /**
     * Parse the input object for calling the corrispondent method.
     * @param obj
     */
    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            getLog().log(Level.INFO,() -> "[C-H]: Handling command: "+obj);
            if(commandIdentifier.equals("")) {

            } else if (commandIdentifier.equals(CHAT)) {
                handlerCallback.getUser().getRoom().chatMessageToRoom(command.get(1), command.get(2));
            } else {
                getLog().log(Level.SEVERE, () ->"[C-H]: invalid message indentifier: "+commandIdentifier);
            }
        }

    }
}
