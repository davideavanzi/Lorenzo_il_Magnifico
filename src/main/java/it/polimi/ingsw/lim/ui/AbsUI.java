package it.polimi.ingsw.lim.ui;

/**
 * Created by nico.
 */
public abstract class AbsUI {

    AbsUI() {}

    public abstract void printBoard();

    public abstract void printChatMessage(String sender, String message);

    public abstract int sendServantsToServer(int minimum);

    public abstract void placeFamilyMember();

    public abstract void cmdManager(String command);

    public abstract void waitForRequest();

    public abstract String[] loginForm();

    /**
     * This method is used for set the network protocol from the selected ui
     */
    public abstract String setNetworkSettings();

    public abstract void printError(String errorMessage);

    /**
     * It's called when the client has a message for the player
     */
    public abstract void printMessageln(String message);

    public abstract void printMessage(String message);
}
