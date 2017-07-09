package it.polimi.ingsw.lim.model.excommunications;

import it.polimi.ingsw.lim.utils.Log;

/**
 * 
 */
public class MarketExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public MarketExcommunication() {
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
        if (!(other instanceof MarketExcommunication)) {
            Log.getLog().info("other not a MarketExcomm");
            return false;
        }
        Log.getLog().info("MarketExcomm equals");
        return true;
    }
}