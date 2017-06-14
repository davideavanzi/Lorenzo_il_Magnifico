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

    public Assets getBonus(){
        return this.bonus;
    }

    public String getMultiplierColor(){
        return this.multiplierColor;
    }

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("      Card multiplied bonus");
        System.out.println("       ---- Base Bonus ----      ");
        bonus.printAssets();
        System.out.println("    ---- Multiplier color ----      ");
        System.out.println(multiplierColor);
    }

    /**
     * the task of this method is to compare if two CardMultipliedEffect are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two CardMultipliedEffect to be compared
     * @return true if the CardMultipliedEffect are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof CardMultipliedEffect)){
            return false;
        }
        CardMultipliedEffect cardMultipliedEffect = (CardMultipliedEffect) other;
        return (this.bonus.equals(cardMultipliedEffect.getBonus()) &&
                this.multiplierColor.equals(cardMultipliedEffect.getMultiplierColor()));
    }
}