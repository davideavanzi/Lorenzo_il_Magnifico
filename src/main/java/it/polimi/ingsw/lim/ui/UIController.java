package it.polimi.ingsw.lim.ui;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.MainClient;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.ServerInteface;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;

import java.util.Scanner;

/**
 * Created by nico.
 */
public class UIController {
    private static AbsUI clientUI;
    private static ServerInteface clientProtocol;
    private static Scanner userInput = new Scanner(System.in);
    //a copy of the user name is stored here
    private String username;

    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param gui
     */
    public UIController(boolean gui) {
        if(gui) {
            //clientUI = new GUI();
        } else {
            clientUI = new CLI();
        }
    }

    public static AbsUI getClientUI() {
        return clientUI;
    }

    public static void setClientProtocol(ServerInteface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }

    public void testChat() {
        try {
            clientProtocol.sendChatMessage(username, "CIAOOOOO");
        }catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    public void login() {
        String username = clientUI.loginForm();
        this.username = username;
        try {
            clientProtocol.sendLogin(username);
        } catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    /**
     *  If the player want to config the network settings.
     */
    public void setNetworkProtocol() {
        String protocol = clientUI.setNetworkSettings();
        if (protocol.equals("socket")) {
            new Thread(new SocketClient(this)).start();
        } else if (protocol.equals("rmi")) {
            clientProtocol = new RMIClient();
        }
        /* Performing login here broke the socket setup and communication! connection is made automatically
         * as the connection protocol is established. TODO: handle in RMI
        int failedRetry = 0;
        while(failedRetry < 3) {
            try {
                clientProtocol.connect();
                break;
            } catch (ClientNetworkException e) {
                failedRetry++;
                clientUI.printMessageln(e.getMessage());
            }
        } */
    }

    /**
     * The player choose if he want to play with GUI or CLI.
     * @return true if you want to use a GUI, false if you want a CLI.
     */
    public static boolean setUI() {
        System.out.println("Do you want to play with a GUI? (y/n)");
        System.out.print("$ ");
        while (true) {
            String gui = userInput.nextLine().toLowerCase();
            switch (gui) {
                case "no":
                case "n":
                    return false;
                case "yes":
                case "y":
                    return true;
                default:
                    System.out.println("Not a valid choice, enter yes/y if you want to use a GUI, no/n for a CLI");
            }
        }
    }
}
