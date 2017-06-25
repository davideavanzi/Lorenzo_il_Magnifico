package it.polimi.ingsw.lim.network.server.socket;

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

    ClientCommandHandler(SocketClientHandler handlerCallback) {
        this.handlerCallback = handlerCallback;
    }

    SocketUser user;

    /**
     * A callback to the client handler
     */
    private SocketClientHandler handlerCallback;

    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<>(Arrays.asList(((String) obj).split(SPLITTER_REGEX)));
            String commandIdentifier = command.get(0);
            getLog().log(Level.INFO,() -> "[C-H] Handling command: "+obj);
            if (commandIdentifier.equals(LOGIN)) {
                this.user = new SocketUser(command.get(1), handlerCallback);
                addUserToRoom(this.user);
            } else if(commandIdentifier.equals(ANSWER_SERVANTS_AMOUNT)) {

            } else if (commandIdentifier.equals(CHAT)) {
                //The server has received a chat message from the client, it has to deliver it to other room mates.
                this.user.getRoom().chatMessageToRoom(command.get(1), command.get(2));
                getLog().log(Level.INFO, () -> "[C-H] message from "+command.get(1)+": "+command.get(2));
            } else {
                getLog().log(Level.SEVERE, () ->"[C-H] invalid message indentifier: "+commandIdentifier);
            }
        }

    }
}
