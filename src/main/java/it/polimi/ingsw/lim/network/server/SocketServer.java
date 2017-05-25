package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Nico.
 */

public class SocketServer implements Runnable {
    private static int socketPort = 8924;
    private ServerSocket serverSck = null;
    private static Boolean isServerRunning = true;

    public SocketServer() {}

    /**
     * @return the state of the socket server
     */
    public static boolean getIsServerRunning() {
        return isServerRunning;
    }

    /**
     * Set the state of the socket server
     * @param state
     */
    public static void setIsServerRunning(boolean state) {
        isServerRunning = state;
    }

    /**
     * This method is use for starting the socket server
     * @throws IOException
     */
    public void startSocketServer() throws IOException {
        try {
            serverSck = new ServerSocket(socketPort);
            System.out.println("The server is starting...");
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not deploy socket server on port "+ socketPort, ioe);
        }
    }

    /**
     * The server waits for a connection with the client, after a thread for the client will be created.
     * Eventually when the server will be stopped the socket gets closed.
     */
    public void run() {
        while(isServerRunning) {
            Socket clientSck = null;
            try {
                System.out.println("Waiting for a client...");
                clientSck = serverSck.accept();
                ClientHandler clientSckThread = new ClientHandler(clientSck);
                clientSckThread.start();
            } catch (IOException ioe) {
                getLog().log(Level.SEVERE,"Could not create a new thread", ioe);

            }
        }
        try {
            serverSck.close();
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not close socket", ioe);
        }
    }
}