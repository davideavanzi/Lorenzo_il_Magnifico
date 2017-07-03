package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

import java.util.*;

import static it.polimi.ingsw.lim.Settings.*;

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
        this.pickDiscounts = new HashMap<>();
        if (greenDiscount != null) this.pickDiscounts.put(GREEN_COLOR, greenDiscount);
        if (blueDiscount != null) this.pickDiscounts.put(BLUE_COLOR, blueDiscount);
        if (yellowDiscount != null) this.pickDiscounts.put(YELLOW_COLOR, yellowDiscount);
        if (purpleDiscount != null) this.pickDiscounts.put(PURPLE_COLOR, purpleDiscount);
        if (blackDiscount != null) this.pickDiscounts.put(BLACK_COLOR, blackDiscount);
        this.towerBonusAllowed = towerBonusAllowed;
    }

    /**
     * 
     */
    private Strengths permanentBonus;

    /**
     * 
     */
    private HashMap<String, Assets> pickDiscounts;

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
        Log.getLog().info("***BLUE CARD Testing Equals***");
        Card card = (Card) other;
        boolean equals = true;
        if (!(this.equals(card))){
            equals =  false;
        }
        BlueCard blueCard = (BlueCard) card;
        if(!(this.permanentBonus.equals(blueCard.getPermanentBonus()))){
            Log.getLog().info("permanent bonus different");
            equals = false;
        }
        if(!(this.pickDiscounts.equals(blueCard.getPickDiscounts()))){
            Log.getLog().info("pick discount different");
            equals = false;
        }
        if(!(this.permanentBonus.equals(blueCard.getPermanentBonus()))){
            Log.getLog().info("permanent bonus different");
            equals = false;
        }
        if(!(this.towerBonusAllowed == blueCard.getTowerBonusAllowed())){
            Log.getLog().info("permanent bonus different");
            equals = false;
        }
        if(equals){
            Log.getLog().info("BlueCardEqual");
        }
        return equals;
    }

    public Strengths getPermanentBonus() {
        return permanentBonus;
    }

    public boolean getTowerBonusAllowed() {
        return towerBonusAllowed;
    }

    public HashMap<String, Assets> getPickDiscounts() {
        return pickDiscounts;
    }
}