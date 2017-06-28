package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by nico.
 * This interface is use to communication to the server.
 */
public interface ServerInterface {

    /**
     * Calling this method the client will try to connect to the server.
     * @throws ClientNetworkException
     */
    void connect() throws ClientNetworkException;

    /**
     * This method is used for send the login information to the server.
     * @param username
     * @throws ClientNetworkException
     */
    void sendLogin(String username) throws ClientNetworkException;

    /**
     * Send a chat message to the server.
     * @param sender the username of the sender
     * @param message the chat message
     * @throws ClientNetworkException
     */
    void chatMessageToServer(String sender, String message) throws ClientNetworkException;
}
