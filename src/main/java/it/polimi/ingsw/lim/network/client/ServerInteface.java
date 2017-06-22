package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;

import java.rmi.RemoteException;

/**
 * Created by nico.
 * This interface is use to communication to the server.
 */
public interface ServerInteface {

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

    void sendChatMessage(String sender, String message) throws ClientNetworkException;
}
