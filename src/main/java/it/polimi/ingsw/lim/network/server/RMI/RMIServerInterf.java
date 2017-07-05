package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.*;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    void excommunicationChoice(boolean choice) throws RemoteException;

    void moveInCouncil(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInHarvest(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInProduction(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInMarket(String fmColor, int marketSlot, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInTower(String fmColor, String twrColor, int floor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void receiveChatMessageFromClient(String sender, String message) throws RemoteException;

    void initializeConnection(RMIClientInterf rci) throws RemoteException;
}