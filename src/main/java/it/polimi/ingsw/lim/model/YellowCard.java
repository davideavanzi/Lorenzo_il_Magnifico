package it.polimi.ingsw.lim.model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.*;

/**
 * This class represents a yellow developement card. These cards have different kinds of production possibilities:
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
                      ArrayList<Assets> productionResults,
                      Strengths actionStrenght,
                      String bonusMultiplier) {
        super(name, age, cost, iEffects);
        this.productionCosts = productionCosts;
        this.productionResults = productionResults;
        this.actionStrenght = actionStrenght;
        this.cardMultiplier = bonusMultiplier;

    }

    /**
     * 
     */
    private ArrayList<Assets> productionCosts;

    /**
     * 
     */
    private ArrayList<Assets> productionResults;

    /**
     * 
     */
    private Strengths actionStrenght;

    /**
     * 
     */
    private String cardMultiplier;

    public String getCardMultiplier() {
        return cardMultiplier;
    }

    public ArrayList<Assets> getProductionCosts() {
        return productionCosts;
    }

    public ArrayList<Assets> getProductionResults() {
        return productionResults;
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
}