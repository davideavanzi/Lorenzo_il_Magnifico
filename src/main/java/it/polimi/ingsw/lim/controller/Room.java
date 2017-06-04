package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room.
 */
public class Room extends UnicastRemoteObject {

    private String roomName;
    private GameController gameController;
    private boolean roomStatus = true; // room open
    private static ArrayList<User> usersList;

    public Room(String name) throws RemoteException {
        roomName = name;
        usersList = new ArrayList<>();
    }

    public void addUser(User user) {
        this.usersList.add(user);
        Log.getLog().info("Adding user "+user.toString()+" to room");
    }

    private ArrayList<String> playOrder;



    public static ArrayList<User> getUsersList() {
        return usersList;
    }
}
