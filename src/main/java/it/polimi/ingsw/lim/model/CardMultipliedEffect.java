package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This kind of effect gives to the player a resource bonus, multiplied by the amount of cards
 * of the specified color in the multiplierColor string.
 */
public class CardMultipliedEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public CardMultipliedEffect(Assets bonus, String color) {
    this.bonus = bonus;
    this.multiplierColor = color;
    }

    /**
     * The bonus
     */
    private Assets bonus;

    /**
     * The multiplier, this string is the color of the card.
     */
    private String multiplierColor;


}