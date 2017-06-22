package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.ui.UIController;

import static it.polimi.ingsw.lim.ui.UIController.setUI;

/**
 * Created by nico.
 */
public class MainClient {

    public static void main(String[] args) {
        System.out.println("Welcome to Lorenzo Il Magnifico!");
        System.out.println();
        UIController client = new UIController(setUI());
        client.setNetworkProtocol();
        client.login();
        client.testChat();
    }
}
