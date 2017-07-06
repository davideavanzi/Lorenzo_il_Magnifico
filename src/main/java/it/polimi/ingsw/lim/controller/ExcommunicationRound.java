package it.polimi.ingsw.lim.controller;
import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In this class is handled an excommunication round.
 * Players will choose whether they want to suffer the excommunication or give up they faith points.
 * Players that can't choose or don't answer will automatically be excommunicated.
 */
public class ExcommunicationRound implements Runnable, Round {
    ArrayList<User> usersToCheck;
    Lock excommLock;

    public ExcommunicationRound(Room roomCallback, int millisTimer, Lock excommLock) {
        this.excommLock = excommLock;
        new RoundTimer(20, this);
    }

    @Override
    public void timerEnded() {
        excommLock.unlock();
    }

    @Override
    public void setTimer(Timer timer) {
    }

    @Override
    public void run() {
        Log.getLog().info("Starting excommunication round!");
    }
}
