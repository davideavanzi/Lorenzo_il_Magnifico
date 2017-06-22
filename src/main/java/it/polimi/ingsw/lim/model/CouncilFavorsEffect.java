package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

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

    public int getAmount(){
        return this.amount;
    }

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("           Council favor");
        System.out.println("     ---- Favors amount ----      ");
        System.out.println(amount);
    }

    /**
     * the task of this method is to compare if two CouncilFavoursEffect are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two CouncilFavoursEffect to be compared
     * @return true if the CouncilFavoursEffect are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof CouncilFavorsEffect)){
            return false;
        }
        boolean equals = true;
        CouncilFavorsEffect councilFavorsEffect = (CouncilFavorsEffect) other;
        if(!(this.amount == councilFavorsEffect.getAmount())){
            equals = false;
            Log.getLog().info("Different council favour effect");
        }
        return equals;
    }
}