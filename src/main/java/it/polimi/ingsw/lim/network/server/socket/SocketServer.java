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

    /**
     * The server socket
     */
    private ServerSocket serverSck;

    /**
     * This boolean set the server state
     */
    private static Boolean isServerRunning = true;

    /**
     * Empty constructor
     */
    public SocketServer() {}

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
     * This method is called when a socket should close.
     * @param serverSocket
     */
    private void closeSocket(ServerSocket serverSocket) {
        try {
            serverSck.close();
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not close socket", ioe);
        }
    }

    /**
     * This method is use for starting the socket server
     * @throws IOException if could not deploy the socket
     */
    public void deployServer(int port) {
        try {
            serverSck = new ServerSocket(port);
            new clientConnectionRequestHandler().start();
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "[SOCKET]: Could not deploy socket server", e);
        } finally {
            closeSocket(serverSck);
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
                    getLog().log(Level.INFO, "[SOCKET]: Waiting for clients");
                    Socket clientSck = serverSck.accept();
                    getLog().log(Level.INFO, "[SOCKET]: New client connected");
                    new Thread(new SocketClientHandler(clientSck)).start();
                } catch (IOException ioe) {
                    getLog().log(Level.SEVERE, "[SOCKET]: Could not create a new thread", ioe);
                }
            }
        }
    }
}