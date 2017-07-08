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
    public void chooseLeaderToCopy(ArrayList<String> copyableLeaders) {
        if (this.getIsAlive()) this.sch.askClientToChooseLeaderToCopy(copyableLeaders);
    }

    @Override
    public void notifyFastHarvest(int baseStr) {
        if (this.getIsAlive()) this.sch.askClientForFastHarvest(baseStr);
    }

    @Override
    public void notifyFastProduction(int baseStr) {
        if (this.getIsAlive()) this.sch.askClientForFastProduction(baseStr);
    }

    @Override
    public void notifyFastTowerMove(HashMap<String, Integer> baseStr, Assets optionalPickDiscount) {
        if (this.getIsAlive()) this.sch.askClientForFastTowerMove(baseStr, optionalPickDiscount);
    }

    @Override
    public void askForProductionOptions(ArrayList<ArrayList<Object[]>> options) {
        if (this.getIsAlive()) this.sch.askClientForProductionOption(options);
    }

    @Override
    public void askForOptionalBpPick() {
        if (this.getIsAlive()) this.sch.askClientForOptionalBpPick();
    }

    @Override
    public void askForExcommunication() {
        if (this.getIsAlive()) this.sch.askClientForExcommunication();
    }

    @Override
    public void askForCouncilFavor(int favorAmount) {
        if (this.getIsAlive()) this.sch.askClientForFavor(favorAmount);
    }

    /**
     * Send a chat messagge to the client.
     * @param sender
     * @param message
     */
    @Override
    public void sendChatMessage(String sender, String message) {
        if (this.getIsAlive()) this.sch.chatMessageToClient(sender, message);
    }

    @Override
    public void gameMessage(String message) {
        if (this.getIsAlive()) this.sch.gameMessageToClient(message);
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
        if (this.getIsAlive()) this.sch.sendGameToClient(board, players);
    }

    @Override
    public void isPlayerRound(boolean isPlaying) {
        if (this.getIsAlive()) this.sch.sendIfUserPlaying(isPlaying);
        this.sch.sendIfUserPlaying(isPlaying);
    }

    @Override
    public void notifyGameStart() {
        this.sch.sendNoficationStartGame();
    }

    @Override
    public void notifyEndGame(ArrayList<Player> players){
        //todo implementare
    }

    @Override
    public void askFmToBoost(){
        //todo implementare
    }

}

