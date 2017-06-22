package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    void login(String username, RMIClientInterf rci) throws RemoteException;

    void chatMessageFromClient(String sender, String message) throws RemoteException;
}