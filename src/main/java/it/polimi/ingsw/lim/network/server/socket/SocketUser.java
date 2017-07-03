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

    /**
     * Socket Client Handler reference.
     */
    SocketClientHandler sch;

    /**
     * Constructor.
     * @param username
     * @param sch
     */
    SocketUser(String username, SocketClientHandler sch) {
        super(username);
        this.sch = sch;
    }

    /**
     * Send the Game board and the player List to client.
     * @param board the game board.
     * @param players arrayList of connected player.
     */
    @Override
    public void sendGameUpdate(Board board, ArrayList<Player> players) {
        this.sch.sendGameToClient(board, players);
    }

    @Override
    public void isPlayerTurn(boolean isPlaying) {
        this.sch.sendIfUserPlaying(isPlaying);
    }

    /**
     * Send a chat messagge to the client.
     * @param sender
     * @param message
     */
    @Override
    public void sendChatMessage(String sender, String message) {
        this.sch.chatMessageToClient(sender, message);
    }

    @Override
    public void askForServants(int minimumAmount) {
        this.sch.askClientServants(minimumAmount);
    }

    @Override
    public int chooseProduction(ArrayList<Assets[]> options) {
        return 0; //TODO: implement
    }

    public void askProductionOptions(ArrayList<ArrayList<Assets[]>> options) {

    }

    @Override
    public int chooseFavor(List<Assets> possibleFavors) {return 0;}

    @Override
    public void broadcastMessage(String message) {}

    @Override
    public void gameMessage(String message) {}

    @Override
    public int chooseFloor() {
        return 0;
    }

    @Override
    public String chooseTower(HashMap<String, Integer> possibleTowers) {
        return null;
    }

    @Override
    public boolean askForOptionalBpPick(int requirement, int cost) {
        return false;
    }

    @Override
    public boolean askForExcommunication() {
        return false;
    }
}
