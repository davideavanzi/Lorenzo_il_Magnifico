package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.controller.rounds.DraftRound;
import it.polimi.ingsw.lim.controller.rounds.ExcommunicationRound;
import it.polimi.ingsw.lim.controller.rounds.PlayerRound;
import it.polimi.ingsw.lim.exceptions.InvalidTimerException;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.parser.Parser;
import it.polimi.ingsw.lim.parser.Writer;
import it.polimi.ingsw.lim.utils.Lock;
import it.polimi.ingsw.lim.utils.Log;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.utils.Log.getLog;

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
    @JsonIgnore
    private transient Lock draftLock;
    private ArrayList<User> usersList;
    private ArrayList<String> playOrder;
    @JsonIgnore
    private transient PlayerRound round;
    private int id;
    @JsonIgnore
    private transient ExcommunicationRound excommunicationRound;
    @JsonIgnore
    private transient DraftRound draftRound;
    private int timerPlayMove;
    private int timerStartingGame;

    public Room(User user, int id) {
        usersList = new ArrayList<>();
        gameController = new GameController(this);
        usersList.add(user);
        user.setRoom(this);
        excommLock = new Lock();
        draftLock = new Lock();
        getLog().log(Level.INFO, () -> "Room created, adding "+ user.getUsername() +" to room");
        this.id = id;
        try {
            timerPlayMove = Parser.parseTimerPlayMove(CONFIGS_PATH+"default/");
            timerStartingGame = Parser.parseTimerStartGame(CONFIGS_PATH+"default/");
        } catch (InvalidTimerException | IOException e) {
            getLog().log(Level.SEVERE, "Error loading timers from file. Setting timers to default values");
            timerPlayMove = DEFAULT_PLAYER_ROUND_TIMER;
            timerStartingGame = DEFAULT_ROOM_LOCK_TIMER;
        }

    }

    public Room(){
        usersList = new ArrayList<>();
        playOrder = new ArrayList<>();
        excommLock = new Lock();
        gameController = new GameController(this);

    }

    public boolean getRoomOpen() {
        return roomOpen;
    }

    public void setRoomOpen(boolean roomOpen){
        this.roomOpen = roomOpen;
    }

    public int getTimerPlayMove(){
        return timerPlayMove;
    }

    public void setTimerPlayMove(int timerPlayMove) {
        this.timerPlayMove = timerPlayMove;
    }

    public int getTimerStartingGame() {
        return timerStartingGame;
    }

    public void setTimerStartingGame(int timerStartingGame) {
        this.timerStartingGame = timerStartingGame;
    }

    public ArrayList<String> getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(ArrayList<String> playOrder){
        this.playOrder = playOrder;
    }

    @JsonIgnore
    public PlayerRound getRound() {
        return round;
    }

    @JsonIgnore
    public void setRound(PlayerRound round){
        this.round = round;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){this.id = id;}

    @JsonIgnore
    public ExcommunicationRound getExcommunicationRound() {
        return excommunicationRound;
    }

    @JsonIgnore
    public void setExcommunicationRound(ExcommunicationRound excommunicationRound) {
        this.excommunicationRound = excommunicationRound;
    }

    @JsonIgnore
    public DraftRound getDraftRound() {
        return draftRound;
    }

    @JsonIgnore
    public void setDraftRound(DraftRound round) { this.draftRound = round; }

    @JsonIgnore
    public Lock getDraftLock(){
        return draftLock;
    }

    @JsonIgnore
    public Lock getExcommLock(){
        return excommLock;
    }

    @JsonIgnore
    public void setExcommLock(Lock excommLock){
        this.excommLock = excommLock;
    }

    public void readdUser(User user){
        user.setRoom(this);
        getLog().log(Level.INFO, () -> "REadding "+ user.getUsername() +" to existing room");
        user.setIsAlive(true);

        User userToReplace = usersList.stream()
                .filter(oldUser -> oldUser.getUsername().equals(user.getUsername())).findFirst().orElse(null);
        user.setPlayer(userToReplace.getPlayer());
        usersList.set(usersList.indexOf(userToReplace), user);

        if(getNumAlive() == 2) {
            new TimerEnd(timerStartingGame, this);
        }
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

    public void addUser(User user) {
        usersList.add(user);
        user.setRoom(this);
        getLog().log(Level.INFO, () -> "Adding "+ user.getUsername() +" to existing room");
        if (usersList.size() == MAX_USERS_PER_ROOM) {
            roomOpen = false;
            getLog().log(Level.INFO, () -> "The room is now full");
        }
        if(this.usersList.size() == 2){
            new TimerEnd(timerStartingGame, this);
        }
    }

    public void chatMessageToRoom(String sender, String message) {
        usersList.forEach(user -> user.sendChatMessage(sender, message));
    }

    private void sendGameUpdate() {
        ArrayList<Player> players = new ArrayList<>();
        usersList.forEach(user -> players.add(user.getPlayer()));
        getConnectedUsers().forEach(user -> user.sendGameUpdate(this.gameController.getBoard(), players));
    }

    void broadcastMessage(String message) {
        usersList.forEach(user -> user.gameMessage(message));
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public boolean isFull() {
        return this.usersList.size() >= MAX_USERS_PER_ROOM;
    }

    @JsonIgnore
    public boolean isOpen() { return roomOpen; }

    void fmPlaced() { this.round.decreaseFmAmount(); }

    /**
     * This method is called when a round has ended and switches the round to the next player.
     */
    public void switchRound(){
        Log.getLog().info("player ".concat(playOrder.get(0)).concat(" ending round"));
        playOrder.remove(0);
        if (playOrder.isEmpty()) {
            if(this.gameController.getTime()[0] == AGES_NUMBER &&
                    this.gameController.getTime()[1] == TURNS_PER_AGE){
                endGame();
                return;
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
                notifyRoundStart(playOrder.get(i));
                Log.getLog().info("player ".concat(round.getUserName()).concat(" now can play ")
                        .concat("in room" + this.getUser(round.getUserName()).getRoom().toString()));
                Log.getLog().info("[WRITER]: saving game info");
                Writer.gameWriter(this.gameController.getGame(), id);
                Writer.roomWriter(this, id);
                break;
            } else {
                playOrder.remove(i);
                i--;
            }
        sendGameUpdate();
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

    private void notifyRoundStart(String username) {
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
     * If the game has not started yed, it creates a draft round, then starts the first turn.
     * If it is the right time, it also triggers the activation of the excommunication round
     */
    private void startNewTurn(){
        if (this.gameController.getTime()[1] == 0) {
            draftLock.lock();
            new Thread(new DraftRound(this, DEFAULT_DRAFT_ROUND_TIMER)).start();
            draftLock.lock();
        }
        buildTurnOrder();
        if(this.gameController.getTime()[1] >= TURNS_PER_AGE) {
            excommLock.lock();
            new Thread(new ExcommunicationRound(this,DEFAULT_EXCOMM_ROUND_TIMER)).start();
        }
        if (excommLock.isLocked())
            excommLock.lock();
        this.gameController.startNewTurn();
        for (int i = 0; i < playOrder.size(); i++)
            if (this.getUser(playOrder.get(i)).isAlive()) {
                this.round = new PlayerRound(this.getUser(playOrder.get(i)), timerPlayMove);
                notifyRoundStart(playOrder.get(i));
                break;
            } else {
                playOrder.remove(i);
                i--;
            }
        sendGameUpdate();
    }

    /**
     * This method handles the game end and builds the ranking based on victory points and final scoring
     */
    void endGame(){
        Log.getLog().info("The game has ended. Ranking will be built now.");
        gameController.applyEndGameExcomm();
        ArrayList<Player> ranking = gameController.buildRanking();
        usersList.forEach(user -> user.notifyEndGame(ranking));
        ranking.forEach(Player -> System.out.println("end game " + Player.getNickname()));
        try {
            for (File file : new File(DUMPS_PATH+ "room/").listFiles()) {
                if (file.getName().contains(((Integer)id).toString())) {
                    file.delete();
                }
            }
            for (File file : new File(DUMPS_PATH+ "game/").listFiles()) {
                if (file.getName().contains(((Integer)id).toString())) {
                    file.delete();
                }
            }
        } catch (NullPointerException e) {
            Log.getLog().severe("[ROOM]: No room/game file in dumps path with id: ".concat(((Integer)id).toString()));
        }
    }

    public void closeRoom(){
        this.roomOpen = false;
    }

    private class TimerEnd{
        private Timer timer;
        private TimerEnd(int seconds, Room roomCallback){
            timer = new Timer();
            timer.schedule(new RoomTimer(roomCallback), (long) (seconds * 1000) /*by default ms (1s = 1000ms)*/);
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