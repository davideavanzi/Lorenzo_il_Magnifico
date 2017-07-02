package it.polimi.ingsw.lim.model;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * This class holds values for strengths that affect the player while performing an action.
 */
public class Strengths implements Serializable {

    /**
     * Default constructor, all values are put to 0.
     */
    public Strengths() {
        this.towerStrengths = new HashMap<>();
        this.harvestBonus = 0;
        this.productionBonus = 0;
        this.towerStrengths.put(GREEN_COLOR, 0);
        this.towerStrengths.put(YELLOW_COLOR, 0);
        this.towerStrengths.put(BLUE_COLOR, 0);
        this.towerStrengths.put(PURPLE_COLOR, 0);
        this.towerStrengths.put(BLACK_COLOR, 0);
    }

    /**
     * This constructor sets all parameters but dice values, which are usually to 0 except leader cards.
     * @param harvest - harvest action strength
     * @param production - production action strength
     * @param green - strength to access the green tower
     * @param yellow - strength to access the yellow tower
     * @param blue - strength to access the blue tower
     * @param purple - strength to access the purple tower
     * @param black - strength to access the black tower
     */
    public Strengths(int harvest, int production, int green, int yellow, int blue, int purple, int black){
        this.towerStrengths = new HashMap<>();
        this.harvestBonus = harvest;
        this.productionBonus = production;
        this.towerStrengths.put(GREEN_COLOR, green);
        this.towerStrengths.put(BLUE_COLOR, blue);
        this.towerStrengths.put(YELLOW_COLOR, yellow);
        this.towerStrengths.put(PURPLE_COLOR, purple);
        this.towerStrengths.put(BLACK_COLOR, black);
    }

    /**
     * 
     */
    private int harvestBonus;

    /**
     * 
     */
    private int productionBonus;


    private HashMap<String, Integer> towerStrengths;

    /**
     * 
     */
    private HashMap<String, Integer> diceBonus;

    /**
     * This method adds two strengths. it adds only tower bonus if they are specified in the hashmap.
     * TODO: HashMap has empty values or default zeros?
     * @param operand
     */
    public Strengths add(Strengths operand) {
        Strengths sum = new Strengths();
        sum.harvestBonus = this.harvestBonus + operand.getHarvestBonus();
        sum.productionBonus = this.productionBonus + operand.getProductionBonus();
        operand.getTowerStrength().keySet().forEach(color ->
                sum.setTowerStrength(color,this.getTowerStrength(color) + operand.getTowerStrength(color)));
        return sum;
    }

    public int getTowerStrength(String color) {
        return this.towerStrengths.get(color);
    }

    public HashMap<String, Integer> getDiceBonus() {
        return diceBonus;
    }

    public HashMap<String, Integer> getTowerStrength() {
        return towerStrengths;
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
        System.out.println("    - Green Bonus:      "+ towerStrengths.get(GREEN_COLOR));
        System.out.println("    - Blue Bonus:       "+ towerStrengths.get(BLUE_COLOR));
        System.out.println("    - Yellow Bonus:     "+ towerStrengths.get(YELLOW_COLOR));
        System.out.println("    - Purple Bonus:     "+ towerStrengths.get(PURPLE_COLOR));
        System.out.println("    - Black Bonus:      "+ towerStrengths.get(BLACK_COLOR));
        //TODO: print dices bonuses
        System.out.println("[END strengths PRINT]");
    }

    public void setTowerStrength(String color, int value) {
        this.towerStrengths.replace(color, value);
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
                this.towerStrengths.equals(strengths.getTowerStrength())
                //this.diceBonus.equals(strengths.getDiceBonus())
        );
    }
    //TODO: check if equals method work between to hashmap

}