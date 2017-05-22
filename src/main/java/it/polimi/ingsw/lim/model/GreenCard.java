package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents a green development card. It can have only one production bonus.
 */
public class GreenCard extends Card {

    /**
     * Constructor
     */
    public GreenCard(String name, int age, Assets cost,
                     ArrayList<ImmediateEffect> iEffect,
                     Assets harvestResult,
                     Strengths actionStrength) {
        super(name, age, cost, iEffect);
        this.actionStrength = actionStrength;
        this.harvestResult = harvestResult;
    }

    /**
     * This is the bonus given to the player when activates the production turn
     */
    private Assets harvestResult;

    /**
     * This is the strength required to perform this action
     */
    private Strengths actionStrength;

}