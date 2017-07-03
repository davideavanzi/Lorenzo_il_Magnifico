package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.*;
import java.util.ArrayList;

/**
 * Created by nico.
 */
public interface RMIServerInterf extends Remote {

    void moveInCouncil(String fmColor, int servants, String username) throws RemoteException;

    void moveInHarvest(String fmColor, int servants, String username) throws RemoteException;

    void moveInProduction(String fmColor, int servants, String username) throws RemoteException;

    void moveInMarket(String fmColor, int marketSlot, int servants, String username) throws RemoteException;

    void moveInTower(String fmColor, String twrColor, int floor, int servants, String username) throws RemoteException;

    void chatMessageFromClient(String sender, String message) throws RemoteException;

    void initializeConnection(RMIClientInterf rci) throws RemoteException;
}