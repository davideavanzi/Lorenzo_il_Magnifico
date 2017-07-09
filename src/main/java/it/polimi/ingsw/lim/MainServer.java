package it.polimi.ingsw.lim;

import static it.polimi.ingsw.lim.Settings.DUMPS_PATH;
import static it.polimi.ingsw.lim.utils.Log.*;

import it.polimi.ingsw.lim.network.server.JDBC;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.controller.Room;
import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.network.server.RMI.RMIServer;
import it.polimi.ingsw.lim.network.server.socket.SocketServer;
import it.polimi.ingsw.lim.parser.Writer;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;


/**
 * This is the server command line interface.
 */
public class MainServer {

    /**
     * MainServer Constructor.
     */
    private MainServer() {
        createLogFile();
        socketServer = new SocketServer();
        try {
            rmiServer = new RMIServer();
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI]: Could not create RMIServer's instance", e);
        }
        roomList = new ArrayList<>();
        System.out.println("[SERVER]: Please, enter 1 to reload saved server status, any other key will erase all the saved file.");
        int decision;
        try {
            Scanner resume = new Scanner(System.in);
            decision = resume.nextInt();
        }
        catch (InputMismatchException e){
            decision = 0;
        }
        if (decision == 1) {
            try {
                for (File file : new File(DUMPS_PATH+"room/").listFiles()) {
                    if (file.getName().contains(".json")) {
                        roomList.add(Writer.readerRoom(file));
                        roomList.get(roomList.size()-1).getUsersList().forEach(user -> user.hasDied());
                        Log.getLog().info("[SERVER]: importing room");
                    }
                }
            } catch (NullPointerException e) {
                Log.getLog().info("[SERVER]: No room file in dumps path");
            }
        } else {
            try {
                for (File file : new File(DUMPS_PATH+"room/").listFiles()) {
                    if (file.getName().contains(".json")) {
                        file.delete();
                    }
                }
                for (File file : new File(DUMPS_PATH+"game/").listFiles()) {
                    if (file.getName().contains(".json")) {
                        file.delete();
                    }
                }
            } catch (NullPointerException e) {
                Log.getLog().info("[SERVER]: No room file in dumps path");
            }
        }

        try {
            jdbc = new JDBC();
        } catch (SQLException e){
            getLog().severe("[SQL]: Some errors in SQL query ");
        } catch (ClassNotFoundException e){
            getLog().severe("[SQL]: Can't locate driver for SQLite ");
        }
    }

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
     * User database.
     */
    private static JDBC jdbc;

    /**
     * Getters.
     * @return the user DB.
     */
    public static JDBC getJDBC(){
        return jdbc;
    }


    /**
     * @return the roomList's ArrayList.
     */
    public static ArrayList<Room> getRoomList() {
        return roomList;
    }

    public static User getUserFromUsername (String name) {
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
     */
    public static boolean isUserAlreadyLoggedIn(String username) {
        for(Room room: roomList) {
            for(User u: room.getUsersList()){
                if(u.getUsername().equals(username) && u.getIsAlive()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The authenticated user is added to the first available room, if no room is available a new room is created.
     * @param user is the authenticated user.
     * @return the room in which the user was added (the last of the list)
     */
    public static Room addUserToRoom(User user) {
        for(Room room: roomList) {
            for(User u: room.getUsersList()){
                if(u.getUsername().equals(user.getUsername()) && !u.getIsAlive()){

                    Log.getLog().info("Player " + u.getUsername()+" has rejoined");
                    room.readdUser(user);
                    return roomList.get(roomList.size()-1);
                }
            }
        }
        if(roomList.isEmpty() || !roomList.get(roomList.size()-1).isOpen()) {
            Random random = new Random();
            roomList.add(new Room(user, random.nextInt(89999999) + 100000000));
            Log.getLog().info("[SERVER]: creating new room. Rooms size " + roomList.size());
        } else {
            Log.getLog().info("[SERVER]: add user to existing room. Rooms size " + roomList.size());
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