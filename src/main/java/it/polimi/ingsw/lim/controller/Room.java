package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.parser.Writer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room.
 * The room is created with the first user
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class Room implements Serializable{

    @JsonIgnore
    private transient GameController gameController;
    private boolean roomOpen = true; // room open
    @JsonIgnore
    private transient Lock excommLock;
    private ArrayList<User> usersList;
    private ArrayList<String> playOrder;
    @JsonIgnore
    private PlayerRound round;
    private int id;
    @JsonIgnore
    private transient ExcommunicationRound excommunicationRound;

    public Room(User user, int id) {
        usersList = new ArrayList<>();
        gameController = new GameController(this);
        usersList.add(user);
        user.setRoom(this);
        excommLock = new Lock();
        getLog().log(Level.INFO, () -> "Room created, adding "+ user.getUsername() +" to room");
        this.id = id;
    }

    public boolean getRoomOpen() {
        return roomOpen;
    }

    public ArrayList<String> getPlayOrder() {
        return playOrder;
    }

    public PlayerRound getRound() {
        return round;
    }

    public Room(){
        usersList = new ArrayList<>();
        playOrder = new ArrayList<>();
        excommLock = new Lock();
        gameController = new GameController();
    }


    public int getId(){
        return id;
    }

    @JsonIgnore
    public ExcommunicationRound getExcommunicationRound() {
        return excommunicationRound;
    }

    @JsonIgnore
    public void setExcommunicationRound(ExcommunicationRound excommunicationRound) {
        this.excommunicationRound = excommunicationRound;
    }

    public void setExcommLock(Lock excommLock){
        this.excommLock = excommLock;
    }

    public void setRoomOpen(boolean roomOpen){
        this.roomOpen = roomOpen;
    }

    public Lock getExcommLock(){
        return excommLock;
    }

    public void readdUser(User user){
        user.setRoom(this);
        getLog().log(Level.INFO, () -> "REadding "+ user.getUsername() +" to existing room");
        user.setIsAlive(true);
        for(User u: usersList){
            if(u.getUsername().equals(user.getUsername()) && !u.isAlive()){
                System.out.println(u.getUsername());
                user.setPlayer(u.getPlayer());
                usersList.remove(usersList.indexOf(u));
                usersList.add(user);
            }
        }
        new TimerEnd(20, this); //todo timer solo se ci sono tutti o solo 2
    }

    public void setId(int id){this.id = id;}

    public void setUsersList(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public void setPlayOrder(ArrayList<String> playOrder){
        this.playOrder = playOrder;
    }

    @JsonIgnore
    public void setRound(PlayerRound round){
        this.round = round;
    }

    public void addUser(User user) {
        usersList.add(user);
        user.setRoom(this);
        getLog().log(Level.INFO, () -> "Adding "+ user.getUsername() +" to existing room");
        if (usersList.size() == MAX_USERS_PER_ROOM) {
            roomOpen = false;
            getLog().log(Level.INFO, () -> "The room is now full");
        }
        if(this.usersList.size() == 2){
            new TimerEnd(20, this);//todo make configurable time
        }
    }

    public void chatMessageToRoom(String sender, String message) {
        usersList.forEach(user -> user.sendChatMessage(sender, message));
    }

    void broadcastMessage(String message) {
        usersList.forEach(user -> user.broadcastMessage(message));
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public boolean isFull() {
        return (this.usersList.size() >= MAX_USERS_PER_ROOM);
    }

    @JsonIgnore
    public boolean isOpen() { return roomOpen; }

    void fmPlaced() { this.round.decreaseFmAmount(); }

    /**
     * This method is called when a round has ended and switches the round to the next player.
     * TODO: handle disconnected players
     */
    void switchRound(){
        System.out.println("_______________ TUTTIISANTI ________________");
        playOrder.remove(0);
        if (playOrder.isEmpty()) {
            System.out.println("_______________ MADONNATROIA ________________");
            startNewTurn();
            Log.getLog().info("[WRITER]: saving game info");
            Writer.gameWriter(this.gameController.getGame(), id);
            Writer.roomWriter(this, id);
            System.out.println("_______________ MADONNAPUTTANA ________________");
            return;
        }
        Log.getLog().info("player ".concat(round.getUserName()).concat(" ending round"));
        for (int i = 0; i < playOrder.size(); i++)
            if (this.getUser(playOrder.get(i)).isAlive()) {
                this.round = new PlayerRound(this.getUser(playOrder.get(i)));
                Log.getLog().info("player ".concat(round.getUserName()).concat(" now can play ")
                        .concat("in room" + this.getUser(round.getUserName()).getRoom().toString()));
                Log.getLog().info("[WRITER]: saving game info");
                Writer.gameWriter(this.gameController.getGame(), id);
                Writer.roomWriter(this, id);
                return;
            } else {
                playOrder.remove(i);
                i--;
            }
    }

    /**
     * This method builds a new turn order, checking also for excommunicated players that will play their first round
     * at the end of the turn
     */
    private void buildTurnOrder() {
        ArrayList<String> singleSwing = gameController.getPlayOrder();
        ArrayList<String> firstSwing = new ArrayList<>();
        ArrayList<String> lastSwing = new ArrayList<>();
        //filtering a single swing to obtain the first and last swing, not using a lambda to assure order is preserved
        for (String name : singleSwing) {
            if (gameController.getGame().isPlayerRoundExcommunicated(getUser(name).getPlayer()))
                lastSwing.add(name);
            else firstSwing.add(name);
        }
        //building new turn order
        this.playOrder = new ArrayList<>();
        if (firstSwing.isEmpty())
            for (int i = 0; i < ROUNDS_PER_TURN; i++)
                this.playOrder.addAll(singleSwing);
        else {
            this.playOrder.addAll(firstSwing);
            for (int i = 0; i < ROUNDS_PER_TURN-1; i++)
                this.playOrder.addAll(singleSwing);
            this.playOrder.addAll(lastSwing);
        }
    }

    User getUser(String username) {
        return usersList.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    ArrayList<User> getConnectedUsers() {
        return new ArrayList<>(usersList.stream().filter(user -> user.isAlive()).collect(Collectors.toList()));
    }

    @JsonIgnore
    public GameController getGameController() {
        return gameController;
    }

    User getPlayingUser() {
        return getUser(this.round.getUserName());
    }

    /**
     * This method is called upon the end of a turn and handles the creation of the next one.
     * If it is the right time, it also triggers the activation of the excommunication round
     */
    private void startNewTurn(){
        //Send game state to players TODO: there's no need to update this everytime?
        System.out.println("_______________ DIOCANE ________________");
        ArrayList<Player> players = new ArrayList<>();
        usersList.forEach(user -> players.add(user.getPlayer()));
        getConnectedUsers().forEach(user -> user.sendGameUpdate(this.gameController.getBoard(), players));
        System.out.println("________________ * DIOPORCO * _______________");
        buildTurnOrder();
        System.out.println("_______________________________");
        System.out.println(this.playOrder.toString());
        System.out.println("_______________________________");

        if(this.gameController.getTime()[1] >= TURNS_PER_AGE) {
            excommLock.lock();
            new Thread(new ExcommunicationRound(this,10000,excommLock)).start();
        }
        if (excommLock.isLocked()) excommLock.lock();
        this.gameController.startNewTurn();
        for (int i = 0; i < playOrder.size(); i++)
            if (this.getUser(playOrder.get(i)).isAlive()) {
                this.round = new PlayerRound(this.getUser(playOrder.get(i)));
                return;
            } else {
                playOrder.remove(i);
                i--;
            }

    }

    /**
     * This method handles the game end and builds the ranking based on victory points and final scoring
     */
    void endGame(){
        usersList.forEach(user -> user.gameMessage("The game has ended. Ranking will be built now."));
        gameController.applyEndGameExcomm();
        ArrayList<String> ranking = gameController.buildRanking();
    }

    public void closeRoom(){
        this.roomOpen = false;
    }

    private class TimerEnd{
        private Timer timer;
        private TimerEnd(int seconds, Room roomCallback){
            timer = new Timer();
            timer.schedule(new RoomTimer(roomCallback), seconds * 1000 /*by default ms (1s = 1000ms)*/);
        }
        private class RoomTimer extends TimerTask{
            private Room roomCallback;
            private RoomTimer(Room roomCallback) {
                this.roomCallback = roomCallback;
            }
            @Override
            public void run(){
                roomCallback.closeRoom();
                gameController.createGame();
                Writer.gameWriter(roomCallback.getGameController().getGame(), id);
                Writer.roomWriter(roomCallback, id);
                roomCallback.startNewTurn();
                Writer.gameWriter(roomCallback.getGameController().getGame(), id);
                Writer.roomWriter(roomCallback, id);
                timer.cancel();
            }
        }
    }
}