package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.ClientInterface;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    void createRoom(String roomName, RMIClientInterf rci) throws RemoteException;

}