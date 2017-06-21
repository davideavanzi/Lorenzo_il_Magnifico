package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.ClientNetworkException;
import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.ClientInterface;
import it.polimi.ingsw.lim.network.server.socket.SocketClientHandler;

import java.rmi.RemoteException;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Created by Davide on 26/05/2017.
 * This class represent the person playing the game. It has a unique username picked from the db upon login.
 * It has also a reference to the corresponding client.
 * It will be used as a gateway between the room/game controller and the communication services
 */
public class User {

    private String username;
    private ClientInterface clientInterf;


    /**
     * User constructor
     * @param username of the player
     * @param clientInterf the reference to the player
     */
    public User(String username, ClientInterface clientInterf) {
        this.username = username;
        this.clientInterf = clientInterf;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public int askForServants(int minimum) {
        try {
            return clientInterf.askForServants(minimum);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI] Connection error asking for servants");
            return -1; //TODO: wut?
        }

    }

    public void chatMessage(String sender, String message) {
        try {
            clientInterf.chatMessage(sender, message);
        } catch (RemoteException e) {
            getLog().log(Level.SEVERE, "[RMI] Connection error Sending message to user");
        }
    }
}
