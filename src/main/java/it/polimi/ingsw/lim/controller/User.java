package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

import java.rmi.RemoteException;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Created by Davide on 26/05/2017.
 * This class represent the person playing the game. It has a unique username picked from the db upon login.
 * It has also a reference to the corresponding client.
 * It will be used as a gateway between the room/game controller and the communication services
 */
public abstract class User {

    /**
     * The user's nickname.
     */
    private String username;

    /**
     * A game room.
     */
    private Room room;

    /**
     * A reference to the corresponding player of the game.
     */
    private Player player;

    /**
     * A boolean where is stored the connection status
     */
    private boolean isAlive = true;

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the correspondent player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the players.
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void hasDied() {
        getLog().log(Level.INFO, () -> "User "+this.getUsername()+" has disconnected.");
        this.isAlive = false;
    }

    boolean isAlive() {
        return this.isAlive;
    }

    /**
     * This method notifies the user that has gained a fast harvest action. It also tells the base strength
     * of the bonus action
     * @param baseStr the action strength
     */
    public abstract void notifyFastHarvest(int baseStr);

    public abstract void notifyFastProduction(int baseStr);

    public abstract void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount);

    /**
     * This method asks the user which production wants to activate. A production option is an array of two
     * Assets objects: the first is the cost, the second is the result.
     * @param options production options the player can choose from
     */
    public abstract void askProductionOptions(ArrayList<ArrayList<Assets[]>> options);

    /**
     * This method is called when a player can pick a purple card both paying normal cost and
     * it's optional cost in terms of battle points.
     * @param requirement the amount of battle points required to pay this way
     * @param cost the amount of battle points that the player will pay
     * @return a boolean that indicates if the player wants to pay with his battle points
     */

    /**
     * This method tells the user that can pick a purple card in both ways, then asks him if he wants to use his
     * battle points
     * @param requirement
     * @param cost
     */
    public abstract void askForOptionalBpPick(int requirement, int cost);

    /**
     * This method asks a user if he wants to take an excommunication
     * @return a boolean that indicates if the player wants the excommunication
     */
    public abstract void askForExcommunication();

    /**
     * Ask the user which one of the possible favors wants to receive.
     * @param possibleFavors the list of possible favors to choose from.
     * @return an integer between 0 and the possibleFavors size
     */
    public abstract void chooseFavor(List<Assets> possibleFavors);
    
    /**
     * Send chat message to client.
     * @param sender
     * @param message
     */
    public abstract void sendChatMessage(String sender, String message);

    public abstract void gameError(String message);

    /**
     * This method sends a message to the user relative to it's gaming state.
     * @param message the message to send
     */
    public abstract void gameMessage(String message);

    /**
     * This method sends a generic information message coming from the server
     * @param message
     */
    public abstract void broadcastMessage(String message);

    /**
     * Calling this method the server will send the updated board and the list of the connected user to the client.
     * @param board the game board
     * @param players arrayList of connected player
     */
    public abstract void sendGameUpdate(Board board, ArrayList<Player> players);

    public abstract void isPlayerTurn(boolean isPlaying);
}
