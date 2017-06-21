package it.polimi.ingsw.lim.network.client;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.ui.cli.CLI;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;
import it.polimi.ingsw.lim.network.ui.AbsUI;
import it.polimi.ingsw.lim.network.client.ui.AbsUI;

import java.util.Scanner;

/**
 * Created by nico.
 */
public class UIController {
    private static AbsUI clientUI;
    private static ServerInteface clientProtocol;
    private static Scanner userInput = new Scanner(System.in);

    /**
     * The first thing to do is create a user interface, then the player must choose
     * the network protocol
     * @param gui
     */
    UIController(boolean gui) {
        if(gui) {
            //clientUI = new GUI();
        } else {
            clientUI = new CLI();
        }
    }

    void login() {
        String username = clientUI.loginForm();
        try {
            clientProtocol.sendLogin(username);
        } catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    /**
     *  If the player want to config the network settings.
     */
    void setNetworkProtocol(UIController controller) {
        String protocol = clientUI.setNetworkSettings();
        if (protocol.equals("socket")) {
            clientProtocol = new SocketClient(controller);
        } else if (protocol.equals("rmi")) {
            clientProtocol = new RMIClient();
        }

        int failedRetry = 0;
        while(failedRetry < 3) {
            try {
                clientProtocol.connect();
                break;
            } catch (ClientNetworkException e) {
                failedRetry++;
                clientUI.printMessageln(e.getMessage());
            }
        }
    }

    /**
     * The player choose if he want to play with GUI or CLI.
     * @return true if you want to use a GUI, false if you want a CLI.
     */
    static boolean setUI() {
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

    public void testChat() {
        try {
            clientProtocol.sendChatMessage(username, "CIAOOOOO");
        }catch (ClientNetworkException e) {
            clientUI.printMessageln(e.getMessage());
        }
    }

    public static void setClientProtocol(ServerInteface clientProtocol) {
        UIController.clientProtocol = clientProtocol;
    }
}
