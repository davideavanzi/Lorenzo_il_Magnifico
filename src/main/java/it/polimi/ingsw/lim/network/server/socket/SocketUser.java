package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import java.util.ArrayList;

/**
 * Created by nico.
 */
public class SocketUser extends User {
    SocketClientHandler sch;

    public SocketUser(String username, SocketClientHandler sch) {
        super(username);
        this.sch = sch;
    }

    @Override
    public void chatMessage(String sender, String message) {
        this.sch.chatMessageToClient(sender, message);
    }

    @Override
    public void sendGameState(Board board) {
        //TODO: implement
    }
}
