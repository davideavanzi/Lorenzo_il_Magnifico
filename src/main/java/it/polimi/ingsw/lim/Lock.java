package it.polimi.ingsw.lim;

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