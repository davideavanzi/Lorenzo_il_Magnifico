package it.polimi.ingsw.lim.controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ava on 29/06/17.
 */
public class RoundTimer {
    private Timer timer;
    public RoundTimer(int seconds, Round roundCallback){
        timer = new Timer();
        timer.schedule(new EndTimer(roundCallback), seconds * 1000 /*by default ms (1s = 1000ms)*/);
    }
    private class EndTimer extends TimerTask {
        private Round roundCallback;

        private EndTimer(Round roundCallback) {
            this.roundCallback = roundCallback;
        }

        @Override
        public void run() {
            roundCallback.timerEnded();
            timer.cancel();
        }
    }
}
