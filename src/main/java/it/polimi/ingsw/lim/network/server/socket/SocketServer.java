package it.polimi.ingsw.lim.network.server.socket;

import static it.polimi.ingsw.lim.Log.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by Nico.
 */

public class SocketServer {
    private ServerSocket serverSck;

    private static Boolean isServerRunning = true;

    public SocketServer() {

    }

    /**
     * @return the state of the socket server
     */
    public static boolean getIsServerRunning() {
        return isServerRunning;
    }

    /**
     * Set the state of the socket server
     *
     * @param state
     */
    public static void setIsServerRunning(boolean state) {
        isServerRunning = state;
    }

    /**
     * This method is use for starting the socket server
     *
     * @throws IOException
     */
    public void startServer(int port) {
        try {
            serverSck = new ServerSocket(port);
            new clientConnectionRequestHandler().start();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Could not deploy socket server", e);
        }
    }

    private class clientConnectionRequestHandler extends Thread {

        /**
         * The server waits for a connection with the client, after a thread for the client will be created.
         * Eventually when the server will be stopped the socket gets closed.
         */
        public void run() {
            while (isServerRunning) {
                try {
                    Socket clientSck = serverSck.accept();
                    new Thread(new SocketClientHandler(clientSck)).start();
                } catch (IOException ioe) {
                    getLog().log(Level.SEVERE, "Could not create a new thread", ioe);
                }
            }

            try {
                serverSck.close();
            } catch (IOException ioe) {
                getLog().log(Level.SEVERE, "Could not close socket", ioe);
            }
        }
    }
}