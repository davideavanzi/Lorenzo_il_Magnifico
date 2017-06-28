package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.exceptions.LoginFailException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    void login(String username, String password, RMIClientInterf rci) throws RemoteException, LoginFailException;

    void chatMessageFromClient(String sender, String message) throws RemoteException;
}