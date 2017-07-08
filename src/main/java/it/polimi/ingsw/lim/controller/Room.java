package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.exceptions.InvalidTimerException;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.parser.Parser;
import it.polimi.ingsw.lim.parser.Writer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import java.io.File;
import java.io.IOException;
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
    private int timerPlayMove;
    private int timerStartingGame;

    public Room(User user, int id) {
        usersList = new ArrayList<>();
        gameController = new GameController(this);
        usersList.add(user);
        user.setRoom(this);
        excommLock = new Lock();
        getLog().log(Level.INFO, () -> "Room created, adding "+ user.getUsername() +" to room");
        this.id = id;
        try {
            timerPlayMove = Parser.parseTimerPlayMove(CONFIGS_PATH+"default/");
            timerStartingGame = Parser.parseTimerStartGame(CONFIGS_PATH+"default/");
        } catch (InvalidTimerException | IOException e) {
            timerPlayMove = 60;
            timerStartingGame = 60;
            e.printStackTrace();        }

    }

    public boolean getRoomOpen() {
        return roomOpen;
    }

    public int getTimerPlayMove(){
        return timerPlayMove;
    }

    public int getTimerStartingGame() {
        return timerStartingGame;
    }

    public void setTimerPlayMove(int timerPlayMove) {
        this.timerPlayMove = timerPlayMove;
    }

    public void setTimerStartingGame(int timerStartingGame) {
        this.timerStartingGame = timerStartingGame;
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
        gameController = new GameController(this);
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

    @JsonIgnore
    public void setExcommLock(Lock excommLock){
        this.excommLock = excommLock;
    }

    public void setRoomOpen(boolean roomOpen){
        this.roomOpen = roomOpen;
    }

    @JsonIgnore
    public Lock getExcommLock(){
        return excommLock;
    }

    public void readdUser(User user){
        user.setRoom(this);
        getLog().log(Level.INFO, () -> "REadding "+ user.getUsername() +" to existing room");
        user.setIsAlive(true);

        User userToReplace = usersList.stream()
                .filter(oldUser -> oldUser.getUsername().equals(user.getUsername())).findFirst().orElse(null);
        user.setPlayer(userToReplace.getPlayer());
        System.out.println(user.getPlayer().getNickname());
        usersList.set(usersList.indexOf(userToReplace), user);

        if(getNumAlive() == 2) {
            new TimerEnd(timerStartingGame, this);
        }//todo timer solo se ci sono tutti o solo 2
    }

    private int getNumAlive(){
        int i = 0;
        for(User u: usersList){
            if(u.isAlive()){
                i++;
            }
        }
        return i;
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
            new TimerEnd(timerStartingGame, this);//todo make configurable time
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
        Log.getLog().info("player ".concat(playOrder.get(0)).concat(" ending round"));
        playOrder.remove(0);
        if (playOrder.isEmpty()) {
            if(this.getGameController().getGame().getAge() == AGES_NUMBER &&
                    this.getGameController().getGame().getTurn() == TURNS_PER_AGE){
                endGame();
            }
            startNewTurn();
            Log.getLog().info("[WRITER]: saving game info");
            Writer.gameWriter(this.gameController.getGame(), id);
            Writer.roomWriter(this, id);
            return;
        }
        for (int i = 0; i < playOrder.size(); i++)
            if (this.getUser(playOrder.get(i)).isAlive()) {
                this.round = new PlayerRound(this.getUser(playOrder.get(i)), timerPlayMove);
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

    private void notifyGameStart() {
        usersList.forEach(user -> user.notifyGameStart());
    }

    private void notifyTurnStart(String username) {
        getUser(username).isPlayerRound(true);
        usersList.stream().filter(user -> !user.getUsername().equals(username))
                .forEach(user -> user.isPlayerRound(false));
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
        for (int i = 0; i < playOrder.size(); i++)
            if (this.getUser(playOrder.get(i)).isAlive()) {
                this.round = new PlayerRound(this.getUser(playOrder.get(i)), timerPlayMove);
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
        Log.getLog().info("The game has ended. Ranking will be built now.");
        gameController.applyEndGameExcomm();
        ArrayList<Player> ranking = gameController.buildRanking();
        usersList.forEach(user -> user.notifyEndGame(ranking));
        try {
            for (File file : new File("src/main/gameData/configs/writer/room/").listFiles()) {
                if (file.getName().contains(((Integer)id).toString())) {
                    file.delete();
                }
            }
            for (File file : new File("src/main/gameData/configs/writer/game/").listFiles()) {
                if (file.getName().contains(((Integer)id).toString())) {
                    file.delete();
                }
            }
        } catch (NullPointerException e) {
            Log.getLog().info("[ROOM]: No room/game file in src/main/gameData/configs/writer/room/ with id: ".concat(((Integer)id).toString()));
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
                Writer.gameWriter(roomCallback.getGameController().getGame(), id);
                Writer.roomWriter(roomCallback, id);
                roomCallback.notifyGameStart();
                roomCallback.startNewTurn();
                Writer.gameWriter(roomCallback.getGameController().getGame(), id);
                Writer.roomWriter(roomCallback, id);
                timer.cancel();
            }
        }
    }
}