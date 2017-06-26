package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Strengths;
import it.polimi.ingsw.lim.model.Tower;

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

    public abstract void waitUserInput();

    public abstract void printChatMessage(String sender, String message);

    public abstract void printStrengths(Strengths strengths, String username);

    public abstract void printAssets(Assets resource, String username);

    public abstract void printTower(String color, Tower tower);

    public abstract void printPlayerCards();
}
