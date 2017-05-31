package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;

import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.socket.SocketServer;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by nico.
 * This is the server command line interface
 */
class ServerCLI {
    private static boolean isServerStarted = false;

    private static int socketPort = 8989;
    private static int RMIPort = 1099;

    private static boolean socketEnable = true;
    private static boolean RMIEnable = true;

    public ServerCLI() {
        createLogFile();
        //TODO: Load configuration
    }

    /**
     * Set the server port for socket or rmi
     * @param protocol socket or rmi
     * @param newPort port number
     */
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
     * Enable socket or rmi protocol
     * @param protocol socket or rmi
     */
    private void enableProtocol(String protocol) {
       if(protocol.equalsIgnoreCase("socket")) {
           socketEnable = true;
       } else if(protocol.equalsIgnoreCase("rmi")) {
           RMIEnable = true;
       } else {
           System.out.println("Unknown protocol selected");
       }
    }

    /**
     * Disable socket or rmi protocol
     * @param protocol socket or rmi
     */
    private void disableProtocol(String protocol) {
        if(protocol.equalsIgnoreCase("socket")) {
            socketEnable = false;
        } else if(protocol.equalsIgnoreCase("rmi")) {
            RMIEnable = false;
        } else {
            System.out.println("Unknown protocol selected");
        }
    }

    /**
     * Start the server.
     * If socket is enable, deploy a socket server and put it on a thread;
     * If rmi is enable, deploy rmi server
     */
    private void startServer() {
        if(socketEnable) {
            try {
                SocketServer socketServer = new SocketServer();
                socketServer.SocketServerStart(socketPort);
                Thread socketServerThread = new Thread(socketServer);
                socketServerThread.start();
            } catch (IOException ioe) {
                getLog().log(Level.SEVERE, "Could not deploy socket server", ioe);
            }
        }
        if(RMIEnable) {
            try {
                RMIServer rmiServer = new RMIServer();
                rmiServer.RMIServerStart(RMIPort);
            } catch (RemoteException re) {
                getLog().log(Level.SEVERE, "Could not deploy RMI server", re);
            }
        }
    }

    /**
     * Start a cli for server configuration
     */
    private void startCLI() {
        Scanner input = new Scanner(System.in);
        String[] command;

        System.out.println("Welcome to the Lorenzo's Server CLI!");
        System.out.println();
        System.out.println("Command:");
        System.out.println("set-port <protocol> <port>");
        System.out.println("enable   <protocol>");
        System.out.println("disable  <protocol>");
        System.out.println("start");
        System.out.println();

        while(!isServerStarted) {
            System.out.print("$ ");
            command = input.nextLine().split(" ");
            switch (command[0]) {
                case "set-port":
                    setPort(command[1], command[2]);
                    break;
                case "enable":
                    enableProtocol(command[1]);
                    break;
                case "disable":
                    disableProtocol(command[1]);
                    break;
                case "start":
                    startServer();
                    isServerStarted = true;
                    break;
                default:
                    System.out.println("Command not found: "+command[0]);
            }
        }
    }

    public static void main(String[] args) {
        ServerCLI cli = new ServerCLI();
        cli.startCLI();
    }
}
