package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;

import java.rmi.RemoteException;

/**
 * Created by nico.
 */
public interface ServerInteface {

    /**
     * Calling this method the client will try to connect to the server
     * @throws ClientNetworkException
     */
    void connect() throws ClientNetworkException;


    void sendLogin(String username) throws ClientNetworkException;
}
