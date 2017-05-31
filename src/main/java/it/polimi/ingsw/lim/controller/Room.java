package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.network.client.RMI.ClientInterf;
import it.polimi.ingsw.lim.network.server.socket.SocketClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room.
 */
public class Room extends UnicastRemoteObject {

    private int roomIndex;
    private ArrayList<User> users;
    private GameController gameController;
    private boolean roomStatus = true; // room open

    public Room(int index) throws RemoteException {
        roomIndex = index;
        users = new ArrayList<>();
    }

    private ArrayList<String> playOrder;



}
