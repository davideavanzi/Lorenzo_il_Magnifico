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
        this.harvestBonus = harvest;
        this.productionBonus = production;
        this.towerStrenghts.put(GREEN_COLOR, green);
        this.towerStrenghts.put(YELLOW_COLOR, yellow);
        this.towerStrenghts.put(BLUE_COLOR, blue);
        this.towerStrenghts.put(PURPLE_COLOR, purple);
        this.towerStrenghts.put(BLACK_COLOR, black);
    }

    /**
     * 
     */
    private int harvestBonus;

    /**
     * 
     */
    private int productionBonus;


    private HashMap<String, Integer> towerStrenghts;

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
        return this.towerStrenghts.get(color);
    }

    public void printStrengths(){
        System.out.println("[STRENGHTS PRINT]");
        System.out.println("    - Harvest:          "+harvestBonus);
        System.out.println("    - Production:       "+productionBonus);
        System.out.println("    - Green Bonus:      "+towerStrenghts.get(GREEN_COLOR));
        System.out.println("    - Blue Bonus:       "+towerStrenghts.get(BLUE_COLOR));
        System.out.println("    - Yellow Bonus:     "+towerStrenghts.get(YELLOW_COLOR));
        System.out.println("    - Purple Bonus:     "+towerStrenghts.get(PURPLE_COLOR));
        System.out.println("    - Black Bonus:      "+towerStrenghts.get(BLACK_COLOR));
        //TODO: print dices bonuses
        System.out.println("[END STRENGHTS PRINT]");
    }

}