package it.polimi.ingsw.lim.model.excommunications;

import it.polimi.ingsw.lim.Log;

/**
 * 
 */
public class ServantsExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public ServantsExcommunication() {
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            Log.getLog().info("other = null");
            return false;
        }
        if (!(other instanceof ServantsExcommunication)) {
            Log.getLog().info("other not a ServantsExcomm");
            return false;
        }
        Log.getLog().info("ServantsExcomm equals");
        return true;
    }


}