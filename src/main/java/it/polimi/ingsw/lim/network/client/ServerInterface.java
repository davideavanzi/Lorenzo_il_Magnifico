package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Player;
import sun.misc.Cleaner;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This interface is use to communication to the server.
 */
public interface ServerInterface {

    void sendFamilyMemberColor(String leaderIndex) throws ClientNetworkException;

    void sendCopyLeaderForLorenzoMedici(int leaderIndex) throws ClientNetworkException;

    void leaderCardDraft(int leaderIndex) throws ClientNetworkException;

    void leaderCardActivate(int id) throws ClientNetworkException;

    void leaderCardDeploy(int id) throws ClientNetworkException;

    void leaderCardDiscard(int id) throws ClientNetworkException;

    void fastHarvest(int servantsDeployed) throws ClientNetworkException;

    void fastProduction(int servantsDeployed) throws ClientNetworkException;

    void fastTowerMove(int servantsDeployed, String towerColor, int floor) throws ClientNetworkException;

    void productionOption(ArrayList<Integer> prodChoice) throws ClientNetworkException;

    void optionalBpPick(boolean bpPayment) throws ClientNetworkException;

    void favorChoice(ArrayList<Integer> favorChoice) throws ClientNetworkException;

    void excommunicationChoice(boolean choice) throws ClientNetworkException;

    void placeFM(String color, ArrayList<String> destination, String servants) throws ClientNetworkException;

    /**
     * Send a chat message to the server.
     * @param sender the username of the sender
     * @param message the chat message
     * @throws ClientNetworkException
     */
    void sendChatMessageToServer(String sender, String message) throws ClientNetworkException;

    /**
     * Calling this method the client will try to connect to the server.
     * @throws ClientNetworkException
     */
    void connect() throws ClientNetworkException;
}
