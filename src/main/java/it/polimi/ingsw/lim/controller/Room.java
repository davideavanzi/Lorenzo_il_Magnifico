package it.polimi.ingsw.lim.controller;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room.
 * The room is created with the first user
 */
public class Room {

    private transient GameController gameController;
    private boolean roomOpen = true; // room open
    private static ArrayList<User> usersList;
    private ArrayList<String> playOrder;

    public Room(User user) {
        usersList = new ArrayList<>();
        gameController = new GameController();
        usersList.add(user);
    }

    public void addUser(User user) {
        usersList.add(user);
        getLog().log(Level.INFO, "Adding user %s to room", user);
    }

    public static List<User> getUsersList() {
        return usersList;
    }

    public boolean isFull() {
        return (this.usersList.size() >= MAX_USERS_PER_ROOM);
    }

    public boolean isOpen() { return roomOpen; }
}
