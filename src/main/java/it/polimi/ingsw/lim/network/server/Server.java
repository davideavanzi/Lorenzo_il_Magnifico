package it.polimi.ingsw.lim.network.server;

import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.Socket.SocketServer;

import static it.polimi.ingsw.lim.Log.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by Nico.
 * The Server Class invoke the main method.
 */

public class Server {
    private static int socketPort = 8989;
    private static int RMIPort = 9090;

    private static boolean socketEnable = true;
    private static boolean RMIEnable = true;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        // Deploy socket server
        try {
            SocketServer socketServer = new SocketServer();
            socketServer.SocketServerStart();
            Thread socketServerThread = new Thread(socketServer);
            socketServerThread.start();
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not deploy socket server", ioe);
            System.exit(-1);
        }

        // Deploy RMI server
        try {
            RMIServer rmiServer = new RMIServer();
            rmiServer.RMIServerStart(RMIPort);
        } catch (RemoteException re) {
            getLog().log(Level.SEVERE, "Could not deploy RMI server", re);
        }
    }
}
