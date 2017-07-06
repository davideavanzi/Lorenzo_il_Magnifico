package it.polimi.ingsw.lim.network.server;

import static it.polimi.ingsw.lim.Log.*;

import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.socket.SocketServer;

import java.rmi.RemoteException;
import java.sql.SQLException;
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
     * ArrayList of connected users.
     */
    private static ArrayList<User> connectedUsers;

    /**
     * Declaration of SocketServer and RMIServer class.
     */
    private SocketServer socketServer;
    private RMIServer rmiServer;

    /**
     * MainServer Constructor.
     */

    private static JDBC jdbc;

    public static JDBC getJDBC(){
        return jdbc;
    }

    private MainServer() {
        createLogFile();
        socketServer = new SocketServer();
        try {
            rmiServer = new RMIServer();
        } catch(RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Could not create RMIServer's instance", e);
        }
        roomList = new ArrayList<>();
        connectedUsers = new ArrayList<>();
        try {
            jdbc = new JDBC();
        } catch (SQLException e){
            getLog().severe("[SQL]: Some errors in SQL query ");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            getLog().severe("[SQL]: Can't locate driver for SQLite ");
            e.printStackTrace();
        }
    }

    /**
     * @return the roomList's ArrayList.
     */
    public static ArrayList<Room> getRoomList() {
        return roomList;
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
     * This method searches a user from the list of connected users, if it is not present, it returns null
     * @param username the user name to search
     * @return the user, if not found null.
     * TODO: is this really useful?
     */
    public static User getConnectedUser(String username) {
        return connectedUsers.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    /**
     * The authenticated user is added to the first available room, if no room is available a new room is created.
     * @param user is the authenticated user.
     * @return the room in which the user was added (the last of the list)
     */
    public static Room addUserToRoom(User user) {
        connectedUsers.add(user);
        if(roomList.isEmpty() || !roomList.get(roomList.size()-1).isOpen()) {
            roomList.add(new Room(user, roomList.size()));
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