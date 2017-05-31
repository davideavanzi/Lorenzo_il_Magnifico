package it.polimi.ingsw.lim.network.server.RMI;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    public void login(String name) throws RemoteException;

}