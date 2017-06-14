package it.polimi.ingsw.lim.model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

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