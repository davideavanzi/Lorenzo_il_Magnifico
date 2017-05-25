package it.polimi.ingsw.lim.model;
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

}