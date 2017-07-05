package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by nico.
 * This interface is use to communication to the server.
 */
public interface ServerInterface {

    void excommunicationChoice(boolean choice)throws ClientNetworkException;

    void placeFM(String color, ArrayList<String> destination, String servants, String username) throws ClientNetworkException;

    /**
     * Send a chat message to the server.
     * @param sender the username of the sender
     * @param message the chat message
     * @throws ClientNetworkException
     */
    void sendChatMessageToServer(String sender, String message) throws ClientNetworkException;

    /**
     * Calling this method the client will try to connect to the server.
     * @throws ClientNetworkException
     */
    void connect() throws ClientNetworkException;
}
