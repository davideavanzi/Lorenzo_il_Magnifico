package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represent a blue development card.
 */
public class BlueCard extends Card {

    /**
     * Constructor
     */
    public BlueCard(String name, int age, Assets cost,
                    ArrayList<ImmediateEffect> iEffect,
                    Strengths permaBonus,
                    Assets greenDiscount,
                    Assets blueDiscount,
                    Assets yellowDiscount,
                    Assets purpleDiscount,
                    Assets blackDiscount,
                    boolean towerBonusAllowed) {
        super(name, age, cost, iEffect);
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

}