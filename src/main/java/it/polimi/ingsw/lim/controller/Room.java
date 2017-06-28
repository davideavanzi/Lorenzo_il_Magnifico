package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Log;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.TimerTask;

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
    private int turnNumber;


    public Room(User user) {
        usersList = new ArrayList<>();
        gameController = new GameController(this);
        usersList.add(user);
        user.setRoom(this);
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

    public void broadcastMessage(String message) {
        usersList.forEach(user -> user.broadcastMessage(message));
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public boolean isFull() {
        return (this.usersList.size() >= MAX_USERS_PER_ROOM);
    }

    public boolean isOpen() { return roomOpen; }

    public void fmPlaced() { this.round.decreaseFmAmount(); }

    /**
     * This method is called when a round has ended and switches the round to the next player.
     */
    public void switchTurn(){
        int size = playOrder.size();
        int i = 0;
        this.turnNumber++;
        if(turnNumber == 4*size){
            turnNumber = 0;
            startNewTurn();
            return;
        }
        String nextUserName;
        for (String userName: playOrder){
            if(userName.equals(round.getUserName())){
                break;
            }
            i++;
        }
        Log.getLog().info("player ".concat(round.getUserName()).concat(" ending round"));
        if(i + 1 < size){
            nextUserName = playOrder.get(i + 1);
        }
        else {
            nextUserName = playOrder.get(0); //take the first
        }
        this.round = new PlayerRound(this.getUser(nextUserName));
        Log.getLog().info("player ".concat(round.getUserName()).concat(" now can play"));
    }

    public User getUser(String username) {
        return usersList.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    public GameController getGameController() {
        return gameController;
    }

    private void startNewTurn(){
        this.playOrder = gameController.getPlayOrder();
        this.gameController.startNewTurn();
        this.round = new PlayerRound(this.getUser(this.playOrder.get(0)));
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