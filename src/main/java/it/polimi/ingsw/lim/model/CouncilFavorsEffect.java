package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This effect gives the player a specified amount of council favors.
 */
public class CouncilFavorsEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public CouncilFavorsEffect(int amount) {
        this.amount = amount;
    }

    /**
     * The amount of council favors.
     */
    private int amount;

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("           Council favor");
        System.out.println("     ---- Favors amount ----      ");
        System.out.println(amount);
    }
}