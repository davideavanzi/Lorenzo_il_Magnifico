package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

import java.util.*;

/**
 * This class represents a green development card. It can have only one production bonus.
 */
public class GreenCard extends Card {

    /**
     * Constructor
     */
    public GreenCard(String name, int age, Assets cost,
                     ArrayList<ImmediateEffect> iEffects,
                     Assets harvestResult,
                     Strengths actionStrength) {
        super(name, age, cost, iEffects);
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

    public Assets getHarvestResult(){
        return this.harvestResult;
    }

    public Strengths getActionStrength(){
        return this.actionStrength;
    }

    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("***GREEN CARD***\nother = null");
            return false;
        }
        if(!(other instanceof GreenCard)){
            Log.getLog().info("***GREEN CARD***\nother not GreenCard");
            return false;
        }
        GreenCard greenCard = (GreenCard) other;

        //starting to compare the immediate effects
        if(!(this.getImmediateEffects().size() == greenCard.getImmediateEffects().size())){
            Log.getLog().info("***GREEN CARD***\nimmediate effect size different");
            return false;
        }
        for(int i = 0; i < this.getImmediateEffects().size(); i++){
            if (!(this.getImmediateEffects().get(i).equals(greenCard.getImmediateEffects().get(i)))){
                Log.getLog().info("***GREEN CARD***\nimmediate effect " + i + " different");
                return false;
            }
        }
        if(!(this.getName().equals(greenCard.getName()))){
            Log.getLog().info("***GREEN CARD***\nname different");
            return false;
        }
        if(!(this.getCost().equals(greenCard.getCost()))){
            Log.getLog().info("***GREEN CARD***\ncost different");
            return false;
        }
        if(!(this.getAge() == (greenCard.getAge()))){
            Log.getLog().info("***GREEN CARD***\nage different");
            return false;
        }
        if(!(this.actionStrength.equals(greenCard.getActionStrength()))){
            Log.getLog().info("***GREEN CARD***\naction strength different");
            return false;
        }
        if(!(this.harvestResult.equals(greenCard.getHarvestResult()))){
            Log.getLog().info("***GREEN CARD***\nharvest result different");
            return false;
        }
        return (this.getName().equals(greenCard.getName()) &&
                this.getCost().equals(greenCard.getCost()) &&
                this.getAge() == greenCard.getAge() &&
                this.actionStrength.equals(greenCard.getActionStrength()) &&
                this.harvestResult.equals(greenCard.getHarvestResult()));
    }
}