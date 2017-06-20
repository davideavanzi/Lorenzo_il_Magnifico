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

    /**
     * Socket Server Port Number
     */
    private int socketPort = 8989;

    /**
     * RMI Server Port Number
     */
    private int RMIPort = 1099;

    /**
     * ArrayList of room
     */
    private static ArrayList<Room> roomList;

    /**
     * Declaration of SocketServer and RMIServer class
     */
    private SocketServer socketServer;
    private RMIServer rmiServer;

    /**
     * MainServer Constructor
     */
    private MainServer() {
        createLogFile();
        socketServer = new SocketServer();
        rmiServer = new RMIServer();
        roomList = new ArrayList<>();
    }

    /**
     * @return the roomList's ArrayList
     */
    public static ArrayList<Room> getRoomList() {
        return roomList;
    }

    /**
     * Deploy socket server and rmi server
     */
    private void startServer() {
        socketServer.deployServer(socketPort);
        rmiServer.deployServer(RMIPort);
    }

    /**
     * Server main method
     * @param args
     */
    public static void main(String[] args) {
        MainServer server = new MainServer();
        server.startServer();
    }
}