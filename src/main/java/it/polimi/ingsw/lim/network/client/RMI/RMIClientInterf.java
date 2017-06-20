package it.polimi.ingsw.lim.network.client.RMI;

import java.rmi.Remote;

/**
 * Created by Nico.
 */
public interface RMIClientInterf {

    // Declaration of method that I'll use for rmi client
    void chatMessage(String sender, String message);

}
