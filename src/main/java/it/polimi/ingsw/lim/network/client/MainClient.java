package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;
import it.polimi.ingsw.lim.network.ui.AbsUI;

import java.util.Scanner;

import static it.polimi.ingsw.lim.network.client.UIController.setUI;

/**
 * Created by nico.
 */
public class MainClient {

    public static void main(String[] args) {
        System.out.println("Welcome to Lorenzo Il Magnifico!");
        System.out.println();
        UIController client = new UIController(setUI());
        client.setNetworkProtocol(client);
        client.login();
        client.testChat();
    }

}
