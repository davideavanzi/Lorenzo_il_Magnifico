package it.polimi.ingsw.lim.model.cards;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;

import java.util.*;

/**
 * This class represent a purple development card. It has an endgame bonus in terms of resources
 * (usually victory points). It also has an optional
 */
public class PurpleCard extends Card {

    /**
     * Constructor
     */
    public PurpleCard(String name, int age, Assets cost,
                      ArrayList<ImmediateEffect> iEffect,
                      Assets endgameBonus,
                      int optionalBpRequirement,
                      int optionalBpCost) {
        super(name, age, cost, iEffect);
        this.endgameBonus = endgameBonus;
        this.optionalBpRequirement = optionalBpRequirement;
        this.optionalBpCost = optionalBpCost;
    }

    /**
     * 
     */
    private Assets endgameBonus;

    /**
     * 
     */
    private int optionalBpRequirement;

    /**
     * 
     */
    private int optionalBpCost;

    public Assets getEndgameBonus(){
        return endgameBonus;
    }

    public int getOptionalBpRequirement(){
        return optionalBpRequirement;
    }

    public int getOptionalBpCost(){
        return optionalBpCost;
    }

    /**
     * the task of this method is to compare if two PurpleCard are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two PurpleCard to be compared
     * @return true if the PurpleCard are equal, false otherwise
     */
    @Override
    public boolean equals (Object other) {
        if (!(other instanceof PurpleCard)) {
            Log.getLog().info("other not PurpleCard");
            return false;
        }
        Log.getLog().info("***PURPLE CARD Testing Equals***");
        Card card = (Card) other;
        boolean equals = true;
        if (!(super.equals(card))) {
            equals = false;
        }
        PurpleCard purpleCard = (PurpleCard) card;
        if (!(this.endgameBonus.equals(purpleCard.getEndgameBonus()))){
            Log.getLog().info("end game bonus different");
            equals = false;
        }
        if(!(this.optionalBpRequirement == purpleCard.getOptionalBpRequirement())){
            Log.getLog().info("optional bp requirement different");
            equals = false;
        }
        if(!(this.optionalBpCost == purpleCard.getOptionalBpCost())){
            Log.getLog().info("optional bp cost different");
            equals = false;
        }
        if(equals){
            Log.getLog().info("purpleCardEqual");
        }
        return equals;
    }
}