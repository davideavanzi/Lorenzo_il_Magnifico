package it.polimi.ingsw.lim.network.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;

/**
 * Created by nico.
 */
public abstract class AbsUI {

    public AbsUI() {

    }

    /**
     * This method is used for set the network protocol from the selected ui
     */
    public abstract void setNetworkSettings() throws ClientNetworkException;

    /**
     * It's called when the client has a message for the player
     */
    public abstract void printMessage(String message);
}
