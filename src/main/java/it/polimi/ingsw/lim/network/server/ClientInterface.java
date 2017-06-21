package it.polimi.ingsw.lim.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Davide on 01/06/2017.
 */
public interface ClientInterface extends Remote {

    public void printToClient(String message) throws RemoteException;

    int askForServants(int minimumAmount) throws RemoteException;

    void chatMessage(String sender, String message) throws RemoteException;
}
