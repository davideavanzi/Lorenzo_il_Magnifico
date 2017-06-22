package it.polimi.ingsw.lim.network.client.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Nico.
 */
public interface RMIClientInterf extends Remote{

    void chatMessageFromServer(String sender, String message) throws RemoteException;
}
