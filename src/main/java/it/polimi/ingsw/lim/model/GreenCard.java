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
            return false;
        }
        if(!(other instanceof GreenCard)){
            return false;
        }
        GreenCard greenCard = (GreenCard) other;

        //starting to compare the immediate effects
        if(!(this.getImmediateEffects().size() == greenCard.getImmediateEffects().size())){
            return false;
        }
        for(int i = 0; i < this.getImmediateEffects().size(); i++){
            if (!(this.getImmediateEffects().get(i).equals(greenCard.getImmediateEffects().get(i)))){
                return false;
            }
        }
        return (this.getName().equals(greenCard.getName()) &&
                this.getCost().equals(greenCard.getCost()) &&
                this.getAge() == greenCard.getAge() &&
                this.actionStrength.equals(greenCard.getActionStrength()) &&
                this.harvestResult.equals(greenCard.getHarvestResult()));
    }
}