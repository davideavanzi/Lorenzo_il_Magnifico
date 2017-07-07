package it.polimi.ingsw.lim.model.cards;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;
import it.polimi.ingsw.lim.model.Strengths;

import java.util.*;

/**
 * This class represents a yellow development card. These cards have different kinds of production possibilities:
 * - Just assets bonus
 * - Assets bonus with an assets cost (single transformation)
 * - Multiple transformations
 * - Assets bonus multiplied by the amount of cards of a specified color
 */
public class YellowCard extends Card {

    /**
     * Constructor. A production card can have multiple costs with a related result: if there are more than one,
     * the player can choose what to activate. Multiple transformations are passed by the parser with two arraylists:
     * one for the costs and one for the results, they are mapped one-to-one.
     */
    public YellowCard(String name, int age, Assets cost,
                      ArrayList<ImmediateEffect> iEffects,
                      ArrayList<Assets> productionCosts,
                      ArrayList<Object> productionResults,
                      Strengths actionStrength,
                      String bonusMultiplier) {
        super(name, age, cost, iEffects);
        this.productionCosts = productionCosts;
        this.productionResults = productionResults;
        this.actionStrength = actionStrength;
        this.cardMultiplier = bonusMultiplier;
    }

    public YellowCard(){
        super();
        this.productionCosts = new ArrayList<>();
        this.productionResults = new ArrayList<>();
    }


    /**
     * 
     */
    private ArrayList<Assets> productionCosts;

    /**
     * 
     */
    private ArrayList<Object> productionResults;

    /**
     * 
     */
    private Strengths actionStrength;

    /**
     * 
     */
    private String cardMultiplier;

    public void setProductionCosts(ArrayList<Assets> productionCosts){
        this.productionCosts = productionCosts;
    }

    public void setProductionResults(ArrayList<Object> productionResults){
        this.productionResults = productionResults;
    }

    public void setActionStrength (Strengths actionStrength){
        this.actionStrength = actionStrength;
    }

    public void setCardMultiplier(String cardMultiplier){
        this.cardMultiplier = cardMultiplier;
    }

    public String getCardMultiplier() {
        return cardMultiplier;
    }

    public ArrayList<Assets> getProductionCosts() {
        return productionCosts;
    }

    public ArrayList<Object> getProductionResults() {
        return productionResults;
    }

    public Strengths getActionStrength() {
        return actionStrength;
    }


    /*
    private class ProductionOption {
        private Assets cost;
        private Assets result;

        public ProductionOption(Assets cost, Assets result) {
            this.cost = cost;
            this.result = result;
        }

        public Assets getCost() {
            return cost;
        }

        public Assets getResult() {
            return result;
        }

        public void setCost(Assets cost) {
            this.cost = cost;
        }

        public void setResult(Assets result) {
            this.result = result;
        }
    }
    */

    /**
     * the task of this method is to compare if two YellowCard are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two YellowCard to be compared
     * @return true if the YelllowCard are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(!(other instanceof YellowCard)){
            Log.getLog().info("other not YellowCard");
            return false;
        }
        Log.getLog().info("***YELLOW CARD Testing Equals***");
        Card card = (Card) other;
        boolean equals = true;
        if (!(super.equals(card))){
            equals =  false;
        }
        YellowCard yellowCard = (YellowCard) card;
        if (!(this.productionResults.size() == yellowCard.getProductionResults().size() && this.productionCosts.size() == yellowCard.getProductionCosts().size())){
            Log.getLog().info("different size (production cost or production result)");
            equals = false;
        }
        else {
            for (int i = 0; i < this.productionResults.size(); i++) {
                if (!(this.productionCosts.get(i).equals(yellowCard.getProductionCosts().get(i)))) {
                    Log.getLog().info("production cost different");
                    equals = false;
                }
                if (!(this.productionResults.get(i).equals(yellowCard.getProductionResults().get(i)))) {
                    Log.getLog().info("production result different");
                    equals = false;
                }
            }
        }
        if(!(this.actionStrength.equals(yellowCard.getActionStrength()))){
            Log.getLog().info("action strength different");
            equals = false;
        }
        if(!(this.cardMultiplier.equals(yellowCard.getCardMultiplier()))){
            Log.getLog().info("card multiplier different");
            equals = false;
        }
        if(equals){
            Log.getLog().info("YellowCardEquals");
        }
        return equals;
    }
}