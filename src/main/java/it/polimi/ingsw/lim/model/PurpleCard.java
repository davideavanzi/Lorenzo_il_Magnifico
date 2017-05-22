package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represent a purple developement card. It has an endgame bonus in terms of resources
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

}