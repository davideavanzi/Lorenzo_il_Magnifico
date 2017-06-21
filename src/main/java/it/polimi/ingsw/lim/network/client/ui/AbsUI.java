package it.polimi.ingsw.lim.network.client.ui;

/**
 * Created by nico.
 */
public abstract class AbsUI {

    public AbsUI() {

    }

    /**
     * This method is used for set the network protocol from the selected ui
     */
    public abstract String setNetworkSettings();


    public abstract String loginForm();

    /**
     * It's called when the client has a message for the player
     */
    public abstract void printMessageln(String message);
    public abstract void printMessage(String message);
}
