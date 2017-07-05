package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Player;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Davide on 26/05/2017.
 * This class represents a game room.
 * The room is created with the first user
 */
public class Room {

    private transient GameController gameController;
    private boolean roomOpen = true; // room open
    private ArrayList<User> usersList;
    private ArrayList<String> playOrder;
    private PlayerRound round;
    private int roundNumber;
    private Lock excommLock;

    public Room(User user) {
        usersList = new ArrayList<>();
        gameController = new GameController(this);
        usersList.add(user);
        user.setRoom(this);
        excommLock = new Lock();
        getLog().log(Level.INFO, () -> "Room created, adding "+ user.getUsername() +" to room");
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

    public List<User> getUsersList() {
        return usersList;
    }

    public boolean isFull() {
        return (this.usersList.size() >= MAX_USERS_PER_ROOM);
    }

    public boolean isOpen() { return roomOpen; }

    void fmPlaced() { this.round.decreaseFmAmount(); }

    /**
     * This method is called when a round has ended and switches the round to the next player.
     * TODO: handle disconnected players
     */
    void switchRound(){
        if (playOrder.isEmpty()) {
            startNewTurn();
            return;
        }
        Log.getLog().info("player ".concat(round.getUserName()).concat(" ending round"));
        String nextUserName = playOrder.remove(0);
        this.round = new PlayerRound(this.getUser(nextUserName));
        Log.getLog().info("player ".concat(round.getUserName()).concat(" now can play ")
                .concat("in room" + this.getUser(nextUserName).getRoom().toString()));
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
        ArrayList<Player> players = new ArrayList<>();
        usersList.forEach(user -> players.add(user.getPlayer()));
        getConnectedUsers().forEach(user -> user.sendGameUpdate(this.gameController.getBoard(), players));

        buildTurnOrder();

        if(this.gameController.getTime()[1] >= TURNS_PER_AGE) {
            excommLock.lock();
            new Thread(new ExcommunicationRound(this,10000,excommLock)).start();
        }
        if (excommLock.isLocked()) excommLock.lock();
        this.gameController.startNewTurn();
        for (String username : this.playOrder)
            if (this.getUser(username).isAlive()) {
                this.round = new PlayerRound(this.getUser(username));
                return;
            }
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
                roomCallback.startNewTurn();
                timer.cancel();
            }
        }
    }
}