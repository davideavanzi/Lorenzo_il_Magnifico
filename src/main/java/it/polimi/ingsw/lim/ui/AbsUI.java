package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.Lock;

/**
 * Created by nico.
 */
public abstract class AbsUI {

    public abstract Lock getLock();

    public abstract void commandAdder(String commandr);

    public abstract void commandManager(String command, String message, boolean outcome);

    public abstract void waitForRequest();

    public abstract String[] loginForm();

    /**
     * This method is used for set the network protocol from the selected ui
     */
    public abstract String setNetworkSettings();

    public abstract void printChatMessage(String sender, String message);

    public abstract void printGameMessageln(String message);

    public abstract void printGameMessage(String message);

    public abstract void printError(String errorMessage);

    /**
     * It's called when the client has a message for the player
     */
    public abstract void printMessageln(String message);

    public abstract void printMessage(String message);
}
