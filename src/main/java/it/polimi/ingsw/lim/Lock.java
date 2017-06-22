package it.polimi.ingsw.lim;

/**
 * Created by ava on 22/06/17.
 */
public class Lock{

    private boolean isLocked = false;

    public synchronized void lock()
            throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}