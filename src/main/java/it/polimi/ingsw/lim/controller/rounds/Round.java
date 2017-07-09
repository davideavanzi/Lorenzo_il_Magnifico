package it.polimi.ingsw.lim.controller.rounds;

import java.util.Timer;

/**
 * rounds interface. It is used by a timer to call the end of the timer
 */
public interface Round{

    void timerEnded();

    void setTimer(Timer timer);
}
