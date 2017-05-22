package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represent a specific immediate effect: it gives a bonus multiplied for the amount of assets specified
 * in the "multiplier" value.
 */
public class AssetsMultipliedEffect extends ImmediateEffect {

    /**
     * Constructor.
     */
    public AssetsMultipliedEffect(Assets bonus, Assets multiplier) {
        super();
        this.bonus = bonus;
        this.multiplier = multiplier;
    }

    /**
     * The bonus
     */
    private Assets bonus;

    /**
     * The multiplier
     */
    private Assets multiplier;

}