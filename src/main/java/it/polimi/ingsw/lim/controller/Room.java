package it.polimi.ingsw.lim.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/** * Created by Davide on 26/05/2017.
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

    public static ArrayList<User> getUsersList() {
        return usersList;
    }
}
