package it.polimi.ingsw.lim.controller;
import it.polimi.ingsw.lim.Lock;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * In this class is handled an excommunication round.
 * Players will choose whether they want to suffer the excommunication or give up they faith points.
 * Players that can't choose or don't answer will automatically be excommunicated.
 */
public class ExcommunicationRound implements Runnable, Round {
    ArrayList<User> usersToCheck;
    ArrayList<Player> toExcommunicate;
    Lock excommLock;
    Room roomCallback;


    public ExcommunicationRound(Room roomCallback, int millisTimer, Lock excommLock) {
        this.excommLock = excommLock;
        this.roomCallback = roomCallback;
        roomCallback.setExcommunicationRound(this);
        this.usersToCheck = roomCallback.getUsersList();
        this.toExcommunicate = new ArrayList<>();
        this.toExcommunicate.addAll(usersToCheck.stream().map(user -> user.getPlayer())
                .collect(Collectors.toList()));
        new RoundTimer(20, this);
    }

    public void applyExcommAnswer(User user, boolean answer) throws BadRequestException{
        if (!answer)
            this.toExcommunicate.remove(toExcommunicate.indexOf(user.getPlayer()));
    }

    @Override
    public void timerEnded() {
        roomCallback.getGameController().applyExcommunication(toExcommunicate);
        excommLock.unlock();
    }

    @Override
    public void setTimer(Timer timer) {
    }

    @Override
    public void run() {
        Log.getLog().info("Starting excommunication round!");
        ArrayList<User> usersToAsk = new ArrayList<>(usersToCheck.stream().filter(user ->
                roomCallback.getGameController().getGame().isNotExcommunicable(user.getPlayer()))
                .collect(Collectors.toList()));
        usersToAsk.forEach(user -> user.askForExcommunication());
    }
}
