package it.polimi.ingsw.lim.controller;

import java.util.Timer;

/**
 * Round interface. It is used by a timer to call the end of the timer
 */
public interface Round{

    void timerEnded();

    void setTimer(Timer timer);
}
