package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void sendGameUpdate(Board board, ArrayList<Player> players) {
        this.sch.sendGameToClient(board, players);
    }

    @Override
    public void sendChatMessage(String sender, String message) {
        this.sch.chatMessageToClient(sender, message);
    }

    @Override
    public int askForServants(int minimumAmount) {
        return 0;
    }

    @Override
    public int chooseProduction(ArrayList<Assets[]> options) {
        return 0; //TODO: implement
    }

    @Override
    public int chooseFavor(List<Assets> possibleFavors) {
        return 0;
    }

    @Override
    public void broadcastMessage(String message) {
    }

    @Override
    public void gameMessage(String message) {
    }

    @Override
    public int chooseFloor() {
        return 0;
    }

    @Override
    public String chooseTower(HashMap<String, Integer> possibleTowers) {
        return null;
    }
}
