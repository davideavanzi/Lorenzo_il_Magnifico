package it.polimi.ingsw.lim.network.client.RMI;

import it.polimi.ingsw.lim.network.server.ClientInterface;

import java.rmi.Remote;

/**
 * Created by Nico.
 */
public interface RMIClientInterf extends ClientInterface {

    // Declaration of method that I'll use for rmi client
    void chatMessage(String sender, String message);

}
