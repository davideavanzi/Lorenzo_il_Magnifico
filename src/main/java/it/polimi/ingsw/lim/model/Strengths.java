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
        this.towerstrengths = new HashMap<>();
        this.harvestBonus = 0;
        this.productionBonus = 0;
        this.towerstrengths.put(GREEN_COLOR, 0);
        this.towerstrengths.put(YELLOW_COLOR, 0);
        this.towerstrengths.put(BLUE_COLOR, 0);
        this.towerstrengths.put(PURPLE_COLOR, 0);
        this.towerstrengths.put(BLACK_COLOR, 0);
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

    public void setTowerStrength(String color, int value) {
        this.towerstrengths.replace(color, value);
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