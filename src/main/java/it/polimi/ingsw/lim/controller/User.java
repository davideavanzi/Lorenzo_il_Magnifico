package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.network.client.RMI.RMIClientInterf;
import it.polimi.ingsw.lim.network.server.socket.SocketClientHandler;

/**
 * Created by Davide on 26/05/2017.
 * This class represent the person playing the game. It has a unique username picked from the db upon login.
 * It has also a reference to the corresponding client.
 */
public class User {

    private String username;
    private String password;
    private SocketClientHandler sch;
    private RMIClientInterf rci;

    /**
     * Socket user constructor
     * @param sch
     */
    public User(SocketClientHandler sch) {
        this.sch = sch;
    }

    /**
     * RMI user constructor
     * @param rci
     */
    public User(RMIClientInterf rci) {
        this.rci = rci;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
