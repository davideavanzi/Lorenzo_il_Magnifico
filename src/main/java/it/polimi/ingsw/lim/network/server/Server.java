package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Nico.
 * The Server Class invoke the main method.
 */
public class Server {

    /**
     * The socket server is create and is put on a thread.
     * @param args
     */
    public static void main(String[] args) {
        try {
            SocketServer socketServer = new SocketServer();
            socketServer.startSocketServer();
            Thread socketServerThread = new Thread(socketServer);
            socketServerThread.start();
        } catch (IOException ioe) {
            getLog().log(Level.SEVERE,"Could not deploy socket server", ioe);
            System.exit(-1);
        }
    }
}
