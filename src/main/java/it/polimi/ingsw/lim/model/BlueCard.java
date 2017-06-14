package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

import java.util.*;

/**
 * This class represent a blue development card.
 */
public class BlueCard extends Card {

    /**
     * Constructor
     */
    public BlueCard(String name, int age, Assets cost,
                    ArrayList<ImmediateEffect> iEffects,
                    Strengths permaBonus,
                    Assets greenDiscount,
                    Assets blueDiscount,
                    Assets yellowDiscount,
                    Assets purpleDiscount,
                    Assets blackDiscount,
                    boolean towerBonusAllowed) {
        super(name, age, cost, iEffects);
        this.permanentBonus = permaBonus;
        this.greenDiscount = greenDiscount;
        this.blueDiscount = blueDiscount;
        this.yellowDiscount = yellowDiscount;
        this.purpleDiscount = purpleDiscount;
        this.blackDiscount = blackDiscount;
        this.towerBonusAllowed = towerBonusAllowed;
    }

    /**
     * 
     */
    private Strengths permanentBonus;

    /**
     * 
     */
    private Assets greenDiscount;

    /**
     * 
     */
    private Assets blueDiscount;

    /**
     * 
     */
    private Assets yellowDiscount;

    /**
     * 
     */
    private Assets purpleDiscount;

    /**
     * 
     */
    private Assets blackDiscount;

    /**
     * 
     */
    private boolean towerBonusAllowed;

    /**
     * the task of this method is to compare if two assets are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two BlueCard to be compared
     * @return true if the BlueCard are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(!(other instanceof BlueCard)){
            Log.getLog().info("other not BlueCard");
            return false;
        }
        Card card = (Card) other;
        if (!(this.equals(card))){
            Log.getLog().info("***BLUE CARD***");
            return false;
        }
        //TODO:implementare equals della blue card
        return true;
    }

}