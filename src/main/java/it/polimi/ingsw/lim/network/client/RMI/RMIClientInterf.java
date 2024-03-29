package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nico.
 */
public interface RMIClientInterf extends Remote {

    void endGameNotification(ArrayList<Player> scoreboard) throws RemoteException;

    void commandValidator(String command, String message, boolean outcome) throws RemoteException;

    void askPlayerForFastHarvest(int basestr) throws RemoteException;

    void askPlayerForFastProduction(int baseStr) throws RemoteException;

    void askPlayerForFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) throws RemoteException;

    void askPlayerProductionOptions(ArrayList<ArrayList<Object[]>> options) throws RemoteException;

    void askPlayerForBpPick() throws RemoteException;

    void askPlayerForFavor(int amount) throws RemoteException;

    void askPlayerForExcommunication() throws RemoteException;

    void startLeaderCardDraft(ArrayList<Integer> leaderOptions) throws RemoteException;

    void askPlayerFmToBoost() throws RemoteException;

    void askPlayerToChooseLeaderToCopy(ArrayList<String> copyableLeaders) throws RemoteException;

    void isUserPlaying(Boolean state) throws RemoteException;

    void receiveChatMessageFromServer(String sender, String message) throws RemoteException;

    void receiveGameMessageFromServer(String message) throws RemoteException;

    String[] askLogin(String errorMsg) throws RemoteException;

    void startListenToInput() throws RemoteException;

    void updateClientGame(Board board, ArrayList<Player> players) throws RemoteException;

    void sendToClientStartGameNotification() throws RemoteException;

    void isAlive() throws RemoteException;
}
