package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Log;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Timer;

import static it.polimi.ingsw.lim.Settings.DEPLOYABLE_FM_PER_ROUND;

/**
 * Created by ava on 16/06/17.
 * This class represent the single turn phase of a player .
 */
public class PlayerRound implements Round {

    /**
     * The user playing the game
     */
    private User user;
    private Timer timer;

    public PlayerRound(User user) {
        this.user = user;
        Log.getLog().info("player ".concat(this.getUserName()).concat(" can play now"));
        new RoundTimer(5, this);
        Log.getLog().info("player ".concat(this.getUserName()).concat(" has 5 second to play"));
    }

    public PlayerRound(){super();}

    public void setUser(User user){
        this.user = user;
    }
    /**
     *
     */
    private int fmToDeploy = DEPLOYABLE_FM_PER_ROUND;

    //Do we need them?
    private int bonusTowerAction = 0;
    private int bonusHarvestAction = 0;
    private int bonusProductionAction = 0;

    @JsonIgnore
    public String getUserName(){
        return this.user.getUsername();
    }

    public User getUser() {
        return this.user;
    }

    public void decreaseFmAmount() { this.fmToDeploy--; }

    @Override
    public void timerEnded() {
        this.endTurn();
    }

    public void playerEnded(){
        this.timer.cancel();
        this.endTurn();
    }

    public void endTurn (){
        Log.getLog().info("turn player ".concat(this.getUserName()).concat(" has ended "));
        this.user.getRoom().switchRound();
    }


    public void setTimer(Timer timer){
        this.timer = timer;
    }

    /*
    private class TimerEnd{
        private Timer timer;
        private TimerEnd(int seconds, PlayerRound turnCallback){
            timer = new Timer();
            timer.schedule(new EndTurnTimer(), (long) (seconds * 1000));
            turnCallback.setTimer(timer);
        }
        private class EndTurnTimer extends TimerTask {
            @Override
            public void run(){
                endTurn("timer ending");
                timer.cancel();
            }
        }
    }*/
}
