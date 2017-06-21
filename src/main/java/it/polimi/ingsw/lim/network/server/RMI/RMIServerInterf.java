package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.ClientInterface;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends ClientInterface {

    void login(String username, ClientInterface client) throws RemoteException;

    void chatMessageFromUser(String message) throws RemoteException;

}