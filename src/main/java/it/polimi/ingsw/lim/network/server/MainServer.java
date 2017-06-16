package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.socket.SocketServer;

import java.util.ArrayList;

/**
 * Created by nico.
 * This is the server command line interface
 */
public class MainServer {
    private int socketPort = 8989;
    private int RMIPort = 1099;
    private boolean isServerStarted = false;
    private static ArrayList<Room> roomList;
    private SocketServer socketServer;
    private RMIServer rmiServer;

    private MainServer() {
        createLogFile();
        socketServer = new SocketServer();
        rmiServer = new RMIServer();
        roomList = new ArrayList<>();

        //TODO: Load configuration
    }

    /**
     * @return the roomList's ArrayList
     */
    public static ArrayList<Room> getRoomList() {
        return roomList;
    }

    /**
     * Start the server
     * Deploy a socket server
     * Then deploy rmi server
     */
    private void startServer() {
        socketServer.startServer(socketPort);
        rmiServer.startServer(RMIPort);
    }

    public static void main(String[] args) {
        MainServer server = new MainServer();
        server.startServer();
    }
}
