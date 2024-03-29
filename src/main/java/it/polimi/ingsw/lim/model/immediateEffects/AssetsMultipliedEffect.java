package it.polimi.ingsw.lim.model.immediateEffects;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.model.Assets;

/**
 * This class represent a specific immediate effect: it gives a bonus multiplied for the amount of assets specified
 * in the "multiplier" value.
 */
public class AssetsMultipliedEffect extends ImmediateEffect {

    /**
     * Constructor.
     */
    public AssetsMultipliedEffect(Assets bonus, Assets multiplier) {
        super();
        this.bonus = bonus;
        this.multiplier = multiplier;
    }

    public AssetsMultipliedEffect(){super();}

    /**
     * The bonus
     */
    private Assets bonus;

    /**
     * The multiplier
     */
    private Assets multiplier;

    public Assets getBonus(){
        return this.bonus;
    }

    public Assets getMultiplier(){
        return this.multiplier;
    }

    public void setBonus (Assets bonus){this.bonus = bonus;}

    public void setMultiplier(Assets multiplier){
        this.multiplier = multiplier;
    }

    /**
     * the task of this method is to compare if two AssetsMultipliedEffect are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two AssetsMultipliedEffect to be compared
     * @return true if the AssetsMultipliedEffect are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof AssetsMultipliedEffect)){
            return false;
        }
        boolean equals = true;
        AssetsMultipliedEffect assetsMultipliedEffect = (AssetsMultipliedEffect) other;
        if(!(this.bonus.equals(assetsMultipliedEffect.getBonus()))){
            Log.getLog().info("bonus different");
            equals = false;
        }
        if(!(this.multiplier.equals(assetsMultipliedEffect.getMultiplier()))){
            Log.getLog().info("multiplier different");
            equals = false;
        }
        return equals;
    }
}