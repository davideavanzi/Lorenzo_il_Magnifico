package it.polimi.ingsw.lim.network.server.socket;

import it.polimi.ingsw.lim.controller.User;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Board;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.cards.PurpleCard;
import org.codehaus.jackson.annotate.JsonIgnore;

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
    @JsonIgnore
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

    SocketUser(){
        super();
    }

    @Override
    public void notifyFastHarvest(int baseStr) {
        this.sch.askClientForFastHarvest(baseStr);
    }

    @Override
    public void notifyFastProduction(int baseStr) {
        this.sch.askClientForFastProduction(baseStr);
    }

    @Override
    public void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {
        this.sch.askClientForFastTowerMove(baseStr, optionalPickDiscount);
    }

    @Override
    public void askForProductionOptions(ArrayList<ArrayList<Object[]>> options) {
        this.sch.askClientForProductionOption(options);
    }

    @Override
    public void askForOptionalBpPick() {
        this.sch.askClientForOptionalBpPick();
    }

    @Override
    public void askForExcommunication() {
        this.sch.askClientForExcommunication();
    }

    @Override
    public void askForCouncilFavor(int favorAmount) {
        this.sch.askClientForFavor(favorAmount);
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
    public void gameMessage(String message) {
        this.sch.gameMessageToClient(message);
    }

    @Override
    public void broadcastMessage(String message) {

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
}

