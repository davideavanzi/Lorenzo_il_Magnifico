package it.polimi.ingsw.lim.model.immediateEffects;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.model.Assets;

/**
 * This effect class provides a simple bonus in term of resources
 */
public class AssetsEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public AssetsEffect(Assets bonus) {
        this.bonus = bonus;
    }

    public AssetsEffect(){super();}

    /**
     * The bouns
     */
    private Assets bonus;

    public Assets getBonus(){
        return this.bonus;
    }

    public void setBonus(Assets bonus){this.bonus = bonus;}

    /**
     * the task of this method is to compare if two AssetsEffect are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two AssetsEffect to be compared
     * @return true if the AssetsEffect are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("***ASSETS EFFECT***\nother is null");
            return false;
        }
        if(!(other instanceof AssetsEffect)){
            Log.getLog().info("***ASSETS EFFECT***\nother is not AssetsEffect");
            return false;
        }
        AssetsEffect assetsEffect = (AssetsEffect) other;
        return this.bonus.equals(assetsEffect.getBonus());
    }
}