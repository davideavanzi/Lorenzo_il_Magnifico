package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.Tower;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Nico.
 */
public interface RMIClientInterf extends Remote{

    void chatMessageFromServer(String sender, String message) throws RemoteException;

    void updateClientGame(Board board) throws RemoteException;
}
