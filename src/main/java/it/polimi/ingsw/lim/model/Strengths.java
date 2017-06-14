package it.polimi.ingsw.lim.model;
import java.util.*;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * This class holds values for strengths that affect the player while performing an action.
 */
public class Strengths {

    /**
     * Default constructor, all values are put to 0.
     */
    public Strengths() {
    }

    /**
     * This constructor sets all parameters but dice values, which are usually to 0 except leader cards.
     * @param harvest - harvest action strength
     * @param production - production action strength
     * @param green - strength to access the green tower
     * @param green - strength to access the green tower
     * @param blue - strength to access the green tower
     * @param purple - strength to access the green tower
     * @param black - strength to access the green tower
     */
    public Strengths(int harvest, int production, int green, int yellow, int blue, int purple, int black){
        this.towerstrengths = new HashMap<>();
        this.harvestBonus = harvest;
        this.productionBonus = production;
        this.towerstrengths.put(GREEN_COLOR, green);
        this.towerstrengths.put(YELLOW_COLOR, yellow);
        this.towerstrengths.put(BLUE_COLOR, blue);
        this.towerstrengths.put(PURPLE_COLOR, purple);
        this.towerstrengths.put(BLACK_COLOR, black);
    }

    /**
     * 
     */
    private int harvestBonus;

    /**
     * 
     */
    private int productionBonus;


    private HashMap<String, Integer> towerstrengths;

    /**
     * 
     */
    private HashMap<String, Integer> diceBonus;

    /**
     * @param operand
     */
    public void add(Strengths operand) {
        // TODO implement here
    }

    public int getTowerStrength(String color) {
        return this.towerstrengths.get(color);
    }

    public HashMap<String, Integer> getDiceBonus() {
        return diceBonus;
    }

    public HashMap<String, Integer> getTowerStrength() {
        return towerstrengths;
    }

    public int getHarvestBonus() {
        return harvestBonus;
    }

    public int getProductionBonus() {
        return productionBonus;
    }

    public void printStrengths(){
        System.out.println("[strengths PRINT]");
        System.out.println("    - Harvest:          "+harvestBonus);
        System.out.println("    - Production:       "+productionBonus);
        System.out.println("    - Green Bonus:      "+towerstrengths.get(GREEN_COLOR));
        System.out.println("    - Blue Bonus:       "+towerstrengths.get(BLUE_COLOR));
        System.out.println("    - Yellow Bonus:     "+towerstrengths.get(YELLOW_COLOR));
        System.out.println("    - Purple Bonus:     "+towerstrengths.get(PURPLE_COLOR));
        System.out.println("    - Black Bonus:      "+towerstrengths.get(BLACK_COLOR));
        //TODO: print dices bonuses
        System.out.println("[END strengths PRINT]");
    }

    /**
     * the task of this method is to compare if two Strengths are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two Strengths to be compared
     * @return true if the Strengths are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof Strengths)){
            return false;
        }
        Strengths strengths = (Strengths) other;
        return (this.harvestBonus == strengths.getHarvestBonus() &&
                this.productionBonus == strengths.getProductionBonus() &&
                this.towerstrengths.equals(strengths.getTowerStrength())
                //this.diceBonus.equals(strengths.getDiceBonus())
        );
    }
    //TODO: check if equals method work between to hashmap

}