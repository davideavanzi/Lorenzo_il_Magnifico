package it.polimi.ingsw.lim.utils;

import java.util.logging.Level;

/**
 * Created by ava on 22/06/17.
 */
public class Lock {

    private boolean isLocked = false;

    public synchronized void lock() {
        try {
            while (isLocked) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.getLog().log(Level.SEVERE, "Lock Error", e);
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notifyAll();
    }

    public synchronized boolean isLocked(){
        return isLocked;
    }
}