package it.polimi.ingsw.lim.model.excommunications;

import it.polimi.ingsw.lim.Log;

/**
 * 
 */
public class TurnExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public TurnExcommunication() {
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
        if (!(other instanceof TurnExcommunication)) {
            Log.getLog().info("other not a TurnExcomm");
            return false;
        }
        Log.getLog().info("TurnExcomm equals");
        return true;
    }


}