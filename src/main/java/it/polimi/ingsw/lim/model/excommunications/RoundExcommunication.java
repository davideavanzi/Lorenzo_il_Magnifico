package it.polimi.ingsw.lim.model.excommunications;

import it.polimi.ingsw.lim.utils.Log;

/**
 * 
 */
public class RoundExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public RoundExcommunication() {
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
        if (!(other instanceof RoundExcommunication)) {
            Log.getLog().info("other not a TurnExcomm");
            return false;
        }
        Log.getLog().info("TurnExcomm equals");
        return true;
    }


}