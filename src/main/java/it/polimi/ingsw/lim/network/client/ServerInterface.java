package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.exceptions.LoginFailedException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by nico.
 * This interface is use to communication to the server.
 */
public interface ServerInterface {
    //TODO OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    void fastHarvest(/*devo passare quello che voglio tornare al server dalla CLI*/) throws ClientNetworkException;

    void fastProduction(/*devo passare quello che voglio tornare al server dalla CLI*/) throws ClientNetworkException;

    void fastTowerMove(/*devo passare quello che voglio tornare al server dalla CLI*/) throws ClientNetworkException;

    void productionOption(ArrayList<Integer> prodChoice, String username) throws ClientNetworkException;

    void optionalBpPick(boolean bpPayment, String username) throws ClientNetworkException;

    void favorChoice(ArrayList<Integer> favorChoice, String username) throws ClientNetworkException;

    void excommunicationChoice(boolean choice, String username) throws ClientNetworkException;

    void placeFM(String color, ArrayList<String> destination, String servants, String username) throws ClientNetworkException;

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
