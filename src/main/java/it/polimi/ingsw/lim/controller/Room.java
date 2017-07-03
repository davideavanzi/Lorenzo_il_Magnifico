package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.excommunications.Excommunication;
import it.polimi.ingsw.lim.model.excommunications.TurnExcommunication;
import it.polimi.ingsw.lim.network.server.RMI.RMIUser;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
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
    private int turnNumber;
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
     * TODO: handle disconnected players
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
        } else {
            nextUserName = playOrder.get(0); //take the first
        }
        this.round = new PlayerRound(this.getUser(nextUserName));
        Log.getLog().info("player ".concat(round.getUserName()).concat(" now can play ").concat("in room" + this.getUser(nextUserName).getRoom().toString()));
    }

    User getUser(String username) {
        return usersList.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    ArrayList<User> getConnectedUsers() {
        return new ArrayList<>(usersList.stream().filter(user -> user.isAlive()).collect(Collectors.toList()));
    }

    GameController getGameController() {
        return gameController;
    }

    public User getPlayingUser() {
        return getUser(this.round.getUserName());
    }

    /**
     * This method is called upon the end of a turn and handles the creation of the next one.
     * If it is the right time, it also triggers the activation of the excommunication round
     */
    private void startNewTurn(){
        //Send game state to players TODO: there's no need to update this everytime
        ArrayList<Player> players = new ArrayList<>();
        usersList.forEach(user -> players.add(user.getPlayer()));
        getConnectedUsers().forEach(user -> user.sendGameUpdate(this.gameController.getBoard(), players));

        this.playOrder = gameController.getPlayOrder();
        if(this.gameController.getTime()[1] >= TURNS_PER_AGE) {
            excommLock.lock();
            new Thread(new ExcommunicationRound(this,10000,excommLock)).start();
        }
        if (excommLock.isLocked()) excommLock.lock();
        this.gameController.startNewTurn();
        for (String username : this.playOrder)
            if (this.getUser(username).isAlive()) {
                Excommunication excommunications = this.getGameController().getGame().getExcommunicationByAge(2);
                if (excommunications instanceof TurnExcommunication) {
                    for (int i = 0; i < this.playOrder.size(); i++) {
                        if (this.getUser(username).equals(excommunications.getExcommunicated().get(i))){
                            //todo equals is implemented?
                            //todo set the turn to the next player
                        }
                    }
                    this.round = new PlayerRound(this.getUser(username));
                    return;
                }
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