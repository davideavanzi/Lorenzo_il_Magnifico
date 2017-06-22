package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.socket.SocketServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by nico.
 * This is the server command line interface.
 */
public class MainServer {

    /**
     * Socket Server Port Number.
     */
    private int socketPort = 8989;

    /**
     * RMI Server Port Number.
     */
    private int RMIPort = 1099;

    /**
     * ArrayList of room.
     */
    private static ArrayList<Room> roomList;

    /**
     * Declaration of SocketServer and RMIServer class.
     */
    private SocketServer socketServer;
    private RMIServer rmiServer;

    /**
     * MainServer Constructor.
     */
    private MainServer() {
        createLogFile();
        socketServer = new SocketServer();
        try {
            rmiServer = new RMIServer();
        } catch(RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Could not create RMIServer's instance", e);
        }
        roomList = new ArrayList<>();
    }

    public static User getUserFromUsername (String name) {
        /*
        return roomList.forEach(room -> room.getUsersList().stream()
                .filter(user -> user.getUsername().equals(name)).findFirst().orElse(null)); */
        for (Room room : roomList) {
            for (User user : room.getUsersList())
                if (user.getUsername().equals(name))
                    return user;
        }
        return null;
    }

    /**
     * @return the roomList's ArrayList.
     */
    public static ArrayList<Room> getRoomList() {
        return roomList;
    }

    /**
     * The authenticated user is added to the first available room, if no room is available a new room is created.
     * @param user is the authenticated user.
     * @return the room in which the user was added (the last of the list)
     */
    public static Room addUserToRoom(User user) {
        if(roomList.isEmpty() || !roomList.get(roomList.size()-1).isOpen()) {
            roomList.add(new Room(user));
        } else {
            roomList.get(roomList.size()-1).addUser(user);
        }
        return roomList.get(roomList.size()-1);
    }

    /**
     * Deploy socket server and rmi server.
     */
    private void startServer() {
        socketServer.deployServer(socketPort);
        rmiServer.deployServer(RMIPort);
    }

    /**
     * Server main method.
     * @param args
     */
    public static void main(String[] args) {
        MainServer server = new MainServer();
        server.startServer();
    }
}