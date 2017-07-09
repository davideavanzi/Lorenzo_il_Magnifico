package it.polimi.ingsw.lim.model.cards;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;
import it.polimi.ingsw.lim.model.Strengths;

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
                     Strengths actionStrength,
                     int councilFavourAmount) {
        super(name, age, cost, iEffects);
        this.actionStrength = actionStrength;
        this.harvestResult = harvestResult;
        this.councilFavourAmount = councilFavourAmount;
    }

    public GreenCard(){super();}

    /**
     * This is the bonus given to the player when activates the production turn
     */
    private Assets harvestResult;

    /**
     * This is the strength required to perform this action
     */
    private Strengths actionStrength;


    private int councilFavourAmount;

    public int getCouncilFavourAmount(){
        return councilFavourAmount;
    }

    public void setCouncilFavourAmount(int councilFavourAmount){
        this.councilFavourAmount = councilFavourAmount;
    }

    public Assets getHarvestResult(){
        return this.harvestResult;
    }

    public Strengths getActionStrength(){
        return this.actionStrength;
    }

    public void setHarvestResult (Assets harvestResult){
        this.harvestResult = harvestResult;
    }

    public void setActionStrength (Strengths actionStrength){
        this.actionStrength = actionStrength;
    }

    /**
     * the task of this method is to compare if two GreenCard are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two GreenCard to be compared
     * @return true if the GreenCard are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(!(other instanceof GreenCard)){
            Log.getLog().info("other not GreenCard");
            return false;
        }
        Log.getLog().info("***GREEN CARD Testing Equals***");
        Card card = (Card) other;
        boolean equals = true;
        if (!(super.equals(card))){
            equals =  false;
        }
        GreenCard greenCard = (GreenCard) other;
        if(!(this.actionStrength.equals(greenCard.getActionStrength()))){
            Log.getLog().info("action strength different");
            equals = false;
        }
        if(!(this.harvestResult.equals(greenCard.getHarvestResult()))){
            Log.getLog().info("harvest result different");
            equals = false;
        }
        if(equals){
            Log.getLog().info("GreenCardEqual");
        }
        return equals;
    }
}