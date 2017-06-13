package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

import java.util.*;

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

    /**
     * The bouns
     */
    private Assets bonus;

    public Assets getBonus(){
        return this.bonus;
    }

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("          Assets bonus");
        System.out.println("        ---- Bonus ----      ");
        bonus.printAssets();

    }
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