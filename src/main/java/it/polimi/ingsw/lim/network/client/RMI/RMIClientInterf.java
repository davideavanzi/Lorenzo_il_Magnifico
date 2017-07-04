package it.polimi.ingsw.lim.network.client.RMI;

import com.sun.org.apache.regexp.internal.RE;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Nico.
 */
public interface RMIClientInterf extends Remote {

    void updateClientGame(Board board, ArrayList<Player> players) throws RemoteException;

    int askUserServants(int minimum) throws RemoteException;

    void commandValidator(String command, String message, boolean outcome) throws RemoteException;

    void isUserPlaying(Boolean state) throws RemoteException;

    void receiveChatMessageFromServer(String sender, String message) throws RemoteException;

    void receiveGameMessageFromServer(String message) throws RemoteException;

    String[] askLogin(String errorMsg) throws RemoteException;

    void startListenToInput() throws RemoteException;

    void isAlive() throws RemoteException;
}
