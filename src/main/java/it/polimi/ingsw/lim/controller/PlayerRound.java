package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Log;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Timer;

import static it.polimi.ingsw.lim.Settings.DEPLOYABLE_FM_PER_ROUND;

/**
 * This class represent the single turn phase of a player .
 */
public class PlayerRound implements Round{

    /**
     * The user playing the game
     */
    private User user;
    private Timer timer;
    private int fmToDeploy = DEPLOYABLE_FM_PER_ROUND;


    /**
     * constructor
     * @param user the player who is playing the round
     */
    public PlayerRound(User user, int timerPlayMove) {
        this.user = user;
        Log.getLog().info("player ".concat(this.getUserName()).concat(" can play now"));
        new RoundTimer(timerPlayMove, this);
        Log.getLog().info("player ".concat(this.getUserName()).concat(" has " + timerPlayMove + " second to play"));
    }

    /**
     * dummy constructor
     */
    public PlayerRound(){super();}

    @JsonIgnore
    public String getUserName(){
        return this.user.getUsername();
    }

    public User getUser() {
        return this.user;
    }


    public void setUser(User user){
        this.user = user;
    }

    @Override
    public void setTimer(Timer timer){
        this.timer = timer;
    }

    public void decreaseFmAmount() { this.fmToDeploy--; }

    /**
     * this method is called if the player explicitly says that is turn is ended
     */
    public void playerEnded(){
        this.timer.cancel();
        this.endTurn();
    }

    /**
     * end the turn switching the round
     */
    public void endTurn (){
        Log.getLog().info("turn player ".concat(this.getUserName()).concat(" has ended "));
        this.user.getRoom().switchRound();
    }

    /**
     * timer to check the duration of a player round
     */
    @Override
    public void timerEnded() {
        this.endTurn();
    }

}