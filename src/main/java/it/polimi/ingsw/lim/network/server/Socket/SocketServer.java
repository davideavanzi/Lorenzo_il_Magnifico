package it.polimi.ingsw.lim.network.server.Socket;

import static it.polimi.ingsw.lim.Log.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Nico.
 */

public class SocketServer implements Runnable {
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
    public void SocketServerStart(int port) throws IOException {
        serverSck = new ServerSocket(port);
        System.out.println("The server is starting...");
    }

    /**
     * The server waits for a connection with the client, after a thread for the client will be created.
     * Eventually when the server will be stopped the socket gets closed.
     */
    public void run() {
        while(isServerRunning) {
            try {
                System.out.println("Waiting for a client...");
                Socket clientSck = serverSck.accept();
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