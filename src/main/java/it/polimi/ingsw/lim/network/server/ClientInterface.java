package it.polimi.ingsw.lim.network.server;

/**
 * Created by Davide on 01/06/2017.
 */
public interface ClientInterface {

    public void printToClient(String message);

    int askForServants(int minimumAmount);

    void chatMessage(String sender, String message);
}
