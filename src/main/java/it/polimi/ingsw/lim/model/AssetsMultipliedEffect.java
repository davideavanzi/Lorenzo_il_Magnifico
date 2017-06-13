package it.polimi.ingsw.lim.model;
import java.util.*;

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

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("        Multiplied Assets");
        System.out.println("      ---- Base bonus ----      ");
        bonus.printAssets();
        System.out.println("   ---- Bonus Multiplier ----      ");
        multiplier.printAssets();
    }

    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof ActionEffect)){
            return false;
        }
        AssetsMultipliedEffect assetsMultipliedEffect = (AssetsMultipliedEffect) other;
        return (this.bonus.equals(assetsMultipliedEffect.getBonus()) &&
                this.multiplier.equals(assetsMultipliedEffect.getMultiplier()));
    }
}