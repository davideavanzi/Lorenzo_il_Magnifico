package it.polimi.ingsw.lim.network.client.RMI;

import java.rmi.Remote;

/**
 * Created by Nico.
 */
public interface RMIClientInterf {

    void chatMessageFromServer(String sender, String message);
}
