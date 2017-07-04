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

    @Override
    public void notifyFastHarvest(int baseStr) {

    }

    @Override
    public void notifyFastProduction(int baseStr) {

    }

    @Override
    public void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {

    }

    @Override
    public void askProductionOptions(ArrayList<ArrayList<Assets[]>> options) {

    }

    @Override
    public void askForOptionalBpPick(int requirement, int cost) {

    }

    @Override
    public void askForExcommunication() {
        this.sch.askClientForExcommunication();
    }

    @Override
    public void chooseFavor(List<Assets> possibleFavors) {

    }


    @Override
    public void isLegalCommand(String command, String message, boolean outcome) {
        this.sch.commandValidator(command, message, outcome);
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

    @Override
    public void gameError(String message) {

    }
}

