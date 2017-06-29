package it.polimi.ingsw.lim.model.immediateEffects;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Strengths;

/**
 * This class represents an immediate effect that gives the player the ability to make another action:
 * This action could be harvest, production or picking a card from a tower. The action is specified in the strength
 * value: the player will be able to perform the action which corresponding strength value is not 0.
 * The only case when multiple values are not 0 is when the player is able to pick a card from ONE of the towers,
 * of his choice.
 * There's also an optional assets discount for the action.
 */
public class ActionEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public ActionEffect(Strengths strength, Assets discount) {
        this.strength = strength;
        this.discount = discount;
    }

    /**
     * Here is stored the strength of the bonus action
     */
    private Strengths strength;

    /**
     * Here is stored an optional resource discount for the action, this discount will be subtracted from the card's
     * cost.
     */
    private Assets discount;

    public Strengths getStrength(){
        return this.strength;
    }

    public Assets getDiscount(){
        return this.discount;
    }

    public void printEffect(){
        System.out.println("[IMMEDIATE EFFECT PRINT]");
        System.out.println("      ---- Effect type ----      ");
        System.out.println("            New Action");
        System.out.println("    ---- Action Strenght ----      ");
        strength.printStrengths();
        if (discount != null){
            System.out.println("    ---- Action Discount ----      ");
            //discount.printAssets();
        }
    }

    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null || !(other instanceof ActionEffect)){
            Log.getLog().info("other not BlueCard");
            return false;
        }
        boolean equals = true;
        ActionEffect actionEffect = (ActionEffect) other;
        if(!(this.strength.equals(actionEffect.getStrength()))){
            Log.getLog().info("actionEffect Strength different");
            equals = false;
        }
        if(!(this.discount.equals(actionEffect.getDiscount()))){
            Log.getLog().info("actionEffect Discount different");
            equals= false;
        }
        return equals;
    }
}