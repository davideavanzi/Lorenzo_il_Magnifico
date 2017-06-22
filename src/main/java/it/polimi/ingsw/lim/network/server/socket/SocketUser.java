package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;

/**
 * Created by nico.
 */
public class SocketUser extends User {
    SocketClientHandler sch;

    public SocketUser(String username, SocketClientHandler sch) {
        super(username);
        this.sch = sch;
    }

}
