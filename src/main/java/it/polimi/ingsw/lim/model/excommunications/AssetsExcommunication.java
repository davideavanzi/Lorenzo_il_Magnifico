package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Assets;

/**
 *  This kinds of excommunication subtracts the specified amount of resourced every time a player gets a bonus.
 */
public class AssetsExcommunication extends Excommunication {

    /**
     * Constructor
     */
    public AssetsExcommunication(Assets malus) {
        super();
        this.malus = malus;
    }

    /**
     * The malus
     */
    private Assets malus;

    public Assets getMalus(){
        return this.malus;
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("other = null");
            return false;
        }
        if(!(other instanceof AssetsExcommunication)){
            Log.getLog().info("other not a AssetsExcomm");
            return false;
        }
        AssetsExcommunication assetsExcommunication = (AssetsExcommunication) other;
        boolean equals = true;
        if(!(this.malus.equals((assetsExcommunication.getMalus())))){
            Log.getLog().info("malus not equals");
            equals = false;
        }
        if (equals){
            Log.getLog().info("AssetsExcomm equals");
        }
        return equals;
    }

}