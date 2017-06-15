package it.polimi.ingsw.lim.network.client;

import static it.polimi.ingsw.lim.network.client.MainClient.*;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClient;
import it.polimi.ingsw.lim.network.client.socket.SocketClient;
import it.polimi.ingsw.lim.network.ui.AbsUI;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by nico.
 * This is the client command line interface
 */
public class CLI extends AbsUI {
    Scanner userInput = new Scanner(System.in);
    String input;
    boolean exitNow = false;

    /**
     * Choose the connection protocol and connect to the server
     */
    public void setNetworkSettings() throws ClientNetworkException {
        System.out.print("Please select the network protocol: (socket/rmi) ");
        while (true) {
            input = userInput.nextLine().toLowerCase();
            switch (input) {
                case "socket":
                case "s":
                    clientProtocol = new SocketClient();
                    return;
                case "rmi":
                case"r":
                    clientProtocol = new RMIClient();
                    return;
                default:
                    System.out.println("Not a valid choice!");
            }
        }

         /*do {

         } while (!(protocol.equalsIgnoreCase("socket") || protocol.equalsIgnoreCase("rmi")));

         if (protocol.equalsIgnoreCase("socket")) {
             System.out.println("Connecting to " + address + " port " + socketPort + "...");
             SocketClient socketClient = new SocketClient();
             socketClient.connect(address, socketPort);
         } else {
             System.out.println("Connecting to " + address + " port " + RMIPort + "...");
             try {
                 RMIClient rmiClient = new RMIClient();
                 rmiClient.connectRMI(address, RMIPort);
                 System.out.println("Connesso con rmi");
                 rmiClient.createLobby(message);
             } catch (RemoteException e) {
                 //TODO: SISTEMARE
                 Log.getLog().severe("mlmlmlmlmlml");
                 System.out.println(e.getMessage() + e.getStackTrace());
             }
         }*/
    }
}

/*
/**
     * Set the server's port number for the connection
     * @param protocol socket or rmi
     * @param newPort port number
     *
// TODO: check if the input is a number port
private void setPort(String protocol, String newPort) {
    if(protocol.equalsIgnoreCase("socket")) {
        // Cast String to int
        socketPort = Integer.parseInt(newPort);
    } else if(protocol.equalsIgnoreCase("rmi")) {
        // Cast String to int
        RMIPort = Integer.parseInt(newPort);
    } else {
        System.out.println("Unknown protocol selected");
        System.out.println("Usage:");
        System.out.println("set-port <protocol> <port>");
    }
}

    /**
     * Set the server's address
     * @param ip
     *
    private void setAddress(String ip) {
        address = ip;
    }

    /**
     * Choose the connection protocol and connect to the server
     *
    private void connect() {
        String protocol, message = "Stanza_1";
        do {
            System.out.println("Please select the connection: [socket / rmi]");
            System.out.print("$ ");
            protocol = userInput.nextLine();
        } while(!(protocol.equalsIgnoreCase("socket") || protocol.equalsIgnoreCase("rmi")));

        if (protocol.equalsIgnoreCase("socket")) {
            System.out.println("Connecting to " + address + " port " + socketPort + "...");
            SocketClient socketClient = new SocketClient();
            socketClient.connect(address, socketPort);
        } else {
            System.out.println("Connecting to " + address + " port " + RMIPort + "...");
            try {
                RMIClient rmiClient = new RMIClient();
                rmiClient.connectRMI(address, RMIPort);
                System.out.println("Connesso con rmi");
                rmiClient.createLobby(message);
            } catch (RemoteException e) {
                //TODO: SISTEMARE
                Log.getLog().severe("mlmlmlmlmlml");
                System.out.println(e.getMessage()+e.getStackTrace());
            }
        }
    }


    /**
     * Start a cli for client configuration
     *
    private void startCLI() {

        System.out.println("Command:");
        System.out.println("set-address <server address>, default localhost");
        System.out.println("set-port <protocol> <port>");
        System.out.println("connect");

        while(!isClientConnected) {
            System.out.print("$ ");
            String[] command = userInput.nextLine().split(" ");
            switch(command[0]) {
                case "set-port":
                    setPort(command[1], command[2]);
                    break;
                case "set-address":
                    setAddress(command[1]);
                    break;
                case "connect":
                    connect();
                    isClientConnected = true;
                    break;
                default:
                    System.out.println("Command not found: "+command[0]);
            }
        }


    }
*/