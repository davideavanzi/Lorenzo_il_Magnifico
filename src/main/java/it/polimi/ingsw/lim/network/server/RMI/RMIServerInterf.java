package it.polimi.ingsw.lim.network.server.RMI;

import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;

import java.rmi.*;
import java.util.ArrayList;

/**
 *
 */
public interface RMIServerInterf extends Remote {

    void fastHarvest(int servantsDeployed, String username, RMIClientInterf rmiClient) throws RemoteException;

    void fastProduction(int servantsDeployed, String username, RMIClientInterf rmiClient) throws RemoteException;

    void fastTowerMove(int servantsDeployed, String towerColor, int floor, String username, RMIClientInterf rmiClient) throws RemoteException;

    void productionOption(ArrayList<Integer> prodChoice, String username, RMIClientInterf rci) throws RemoteException;

    void optionalBpPick(boolean bpPayment, String username, RMIClientInterf rci) throws RemoteException;

    void favorChoice(ArrayList<Integer> favorChoice, String username, RMIClientInterf rci) throws RemoteException;

    void excommunicationChoice(boolean choice, String username, RMIClientInterf rci) throws RemoteException;

    void copyLeaderAbility(int leaderindex, String username, RMIClientInterf rci) throws RemoteException;

    void familyMemberColorAbility(String fmColor, String username, RMIClientInterf rci) throws RemoteException;

    void draftLeaderCard(int leaderIndex, String username, RMIClientInterf rci) throws RemoteException;

    void activateLeaderCard(int id, String username, RMIClientInterf rci) throws RemoteException;

    void deployLeaderCard(int id, String username, RMIClientInterf rci) throws RemoteException;

    void discardLeaderCard(int id, String username, RMIClientInterf rci) throws RemoteException;

    void moveInCouncil(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInHarvest(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInProduction(String fmColor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInMarket(String fmColor, int marketSlot, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void moveInTower(String fmColor, String twrColor, int floor, int servants, String username, RMIClientInterf rci) throws RemoteException;

    void receiveChatMessageFromClient(String sender, String message) throws RemoteException;

    void initializeConnection(RMIClientInterf rci) throws RemoteException;
}