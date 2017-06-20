package it.polimi.ingsw.lim.network.server.socket;
import static it.polimi.ingsw.lim.Log.*;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.network.SocketConstants.*;
import static it.polimi.ingsw.lim.network.server.MainServer.addUserToRoom;

import java.io.IOException;
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

    /**
     * The room in which the user is playing
     */
    private Room room;

    /**
     * A callback to the client handler
     */
    private SocketClientHandler handlerCallback;

    void requestHandler(Object obj) {
        if(obj instanceof String) {
            ArrayList<String> command = new ArrayList<String>(Arrays.asList(((String) obj).split(" ")));
            String commandIdentifier = command.get(0);
            if (commandIdentifier.equals(LOGIN)) {
                //TODO: username must not be null
                this. room = addUserToRoom(new User(command.get(1), handlerCallback));
                System.out.println("added to room");
            } else if(commandIdentifier.equals(ANSWER_SERVANTS_AMOUNT)) {

            } else if (commandIdentifier.equals(CHAT)) {
                //The server has received a chat message from the client, it has to deliver it to other room mates.
                room.chatMessage(command.get(1), command.get(2));
                getLog().log(Level.INFO, () -> "[CHAT] message from "+command.get(1)+": "+command.get(2));
            }
        }
    }
}
