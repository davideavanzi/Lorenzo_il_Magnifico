package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Player;

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
    private static ArrayList<User> usersList;
    private ArrayList<String> playOrder;
    private PlayerTurn turn;

    public Room(User user) {
        usersList = new ArrayList<>();
        gameController = new GameController();
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
    }

    public void chatMessageToRoom(String sender, String message) {
        usersList.forEach(user -> user.chatMessage(sender, message));
    }

    public static List<User> getUsersList() {
        return usersList;
    }

    public boolean isFull() {
        return (this.usersList.size() >= MAX_USERS_PER_ROOM);
    }

    public boolean isOpen() { return roomOpen; }

    private void notifyEndTurn(){
        //todo switch turn to next player
    }

    private class TimerEndTurn{
        private Timer timer;
        public TimerEndTurn(int seconds, Room roomCallback){
            timer = new Timer();
            timer.schedule(new endTimer(roomCallback), seconds * 1000 /*by default ms (1s = 1000ms)*/);
        }
        private class endTimer extends TimerTask{
            private Room roomCallback;
            private endTimer(Room roomCallback) {
                this.roomCallback = roomCallback;
            }
            @Override
            public void run(){
                //todo when timer end
                roomCallback.notifyEndTurn();
                timer.cancel();
            }
        }
    }

}