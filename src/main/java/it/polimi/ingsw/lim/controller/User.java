package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
/**
     * Print all the Council Favours Bonus
     */import it.polimi.ingsw.lim.network.server.RMI.RMIUser;
import it.polimi.ingsw.lim.network.server.socket.SocketUser;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Created by Davide on 26/05/2017.
 * This class represent the person playing the game. It has a unique username picked from the db upon login.
 * It has also a reference to the corresponding client.
 * It will be used as a gateway between the room/game controller and the communication services
 */


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RMIUser.class, name = "RMIUser"),

        @JsonSubTypes.Type(value = SocketUser.class, name = "SocketUser"),
})
public abstract class User implements Serializable{

    /**
     * The user's nickname.
     */
    private String username;

    /**
     * A game room.
     */
    @JsonIgnore
    private transient Room room;

    /**
     * A reference to the corresponding player of the game.
     */
    @JsonIgnore
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
     * dummy User constructor
     */
    public User(){}

    public boolean getIsAlive(){
        return this.isAlive;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public Room getRoom() {
        return room;
    }

    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    boolean isAlive() {
        return this.isAlive;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setIsAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * set isAlive to false if the player has disconnected
     */
    @JsonIgnore
    public void hasDied() {
        getLog().log(Level.INFO, () -> "User "+this.getUsername()+" has disconnected.");
        this.isAlive = false;
    }

    /**
     * This method notifies the user that has gained a fast harvest action. It also tells the base strength
     * of the bonus action
     * @param baseStr the action strength
     */
    public abstract void notifyFastHarvest(int baseStr);

    /**
     * This method notifies the user that has gained a fast production action. It also tells the base strength
     * of the bonus action
     * @param baseStr the action strength
     */
    public abstract void notifyFastProduction(int baseStr);

    /**
     * This method notifies the user that has gained a fast tower move.
     */
    public abstract void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount);

    /**
     * This method asks the user which production wants to activate. A production option is an array of two
     * Assets objects: the first is the cost, the second is the result.
     * @param options production options the player can choose from.
     */
    public abstract void askForProductionOptions(ArrayList<ArrayList<Object[]>> options);

    /**
     * This method is called when a player can pick a purple card both paying normal cost and
     * it's optional cost in terms of battle points.
     */
    public abstract void askForOptionalBpPick();

    /**
     * This method asks a user if he wants to take an excommunication.
     */
    public abstract void askForExcommunication();

    /**
     * Ask the user which one of the possible favors wants to receive.
     */
    public abstract void askForCouncilFavor(int favorAmount);
    
    /**
     * Send chat message to client.
     * @param sender
     * @param message
     */
    public abstract void sendChatMessage(String sender, String message);

    /**
     * This method sends a message to the user relative to it's gaming state.
     * @param message the message to send.
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

    /**
     * This method is called by game controller in the beginning of every turn;
     * It notify all client
     * @param isPlaying
     */
    @JsonIgnore
    public abstract void isPlayerTurn(boolean isPlaying);

}
