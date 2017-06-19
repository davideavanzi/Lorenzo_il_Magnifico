package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.Log;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room. It runs on a dedicated thread.
 * The room is created with the first user
 */
public class Room extends UnicastRemoteObject implements Runnable {

    private transient GameController gameController;
    private boolean roomOpen = true; // room open
    private static ArrayList<User> usersList;
    private ArrayList<String> playOrder;

    public Room(String name) throws RemoteException {
        usersList = new ArrayList<>();
        gameController = new GameController();
    }

    public void addUser(User user) {
        usersList.add(user);
        Log.getLog().log(Level.INFO, "Adding user %s to room", user);
    }

    public static List<User> getUsersList() {
        return usersList;
    }

    @Override
    public void run() {

    }
}
