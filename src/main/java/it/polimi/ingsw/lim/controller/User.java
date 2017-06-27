package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davide on 26/05/2017.
 * This class represent the person playing the game. It has a unique username picked from the db upon login.
 * It has also a reference to the corresponding client.
 * It will be used as a gateway between the room/game controller and the communication services
 */
public abstract class User {

    private String username;
    private Room room;
    private Player player;


    /**
     * User constructor
     * @param username of the player
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public abstract void chatMessage(String sender, String message);

    public abstract void sendGameState(Board board, ArrayList<Player> players);

    public abstract int askForServants(int minimumAmount);

    /**
     * This method asks the user which production wants to activate. A production option is an array of two
     * Assets objects: the first is the cost, the second is the result.
     * @param options production options the player can choose from
     * @return the integer corresponding to the option position in the list.
     */
    public abstract int chooseProduction(ArrayList<Assets[]> options);

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * This method sends a generic information message coming from the server
     * @param message
     */
    public abstract void broadcastMessage(String message);

    /**
     * Ask the user which one of the possible favors wants to receive.
     * @param possibleFavors the list of possible favors to choose from.
     * @return an integer between 0 and the possibleFavors size
     */
    public abstract int chooseFavor(List<Assets> possibleFavors);

}
