package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Strengths;

/**
 * 
 */
public class StrengthsExcommunication extends Excommunication {

    public StrengthsExcommunication(){
        super();
    }

    /**
     * Default constructor
     */
    public StrengthsExcommunication(Strengths malus) {
    this.malus = malus;
    }

    /**
     * 
     */
    private Strengths malus;

    public Strengths getMalus(){
        return this.malus;
    }

    public void setMalus(Strengths malus){
        this.malus = malus;
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
        if(!(other instanceof StrengthsExcommunication)){
            Log.getLog().info("other not a StrengthsExcomm");
            return false;
        }
        StrengthsExcommunication strengthsExcommunication = (StrengthsExcommunication) other;
        boolean equals = true;
        if(!(this.malus.equals((strengthsExcommunication.getMalus())))){
            Log.getLog().info("malus not equals");
            equals = false;
        }
        if (equals){
            Log.getLog().info("StrengthsExcomm equals");
        }
        return equals;
    }
}