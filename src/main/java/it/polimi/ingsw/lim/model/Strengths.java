package it.polimi.ingsw.lim.model;
import java.util.*;

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
        this.greenBonus = green;
        this.yellowBonus = yellow;
        this.blackBonus = blue;
        this.purpleBonus = purple;
        this.blackBonus = black;
    }

    /**
     * 
     */
    private int harvestBonus;

    /**
     * 
     */
    private int productionBonus;

    /**
     * 
     */
    private int greenBonus;

    /**
     * 
     */
    private int blueBonus;

    /**
     * 
     */
    private int yellowBonus;

    /**
     * 
     */
    private int blackBonus;

    /**
     * 
     */
    private int purpleBonus;

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

    public void printStrenghts(){
        System.out.println("[STRENGHTS PRINT]");
        System.out.println("    - Harvest:          "+harvestBonus);
        System.out.println("    - Production:       "+productionBonus);
        System.out.println("    - Green Bonus:      "+greenBonus);
        System.out.println("    - Blue Bonus:       "+blueBonus);
        System.out.println("    - Yellow Bonus:     "+yellowBonus);
        System.out.println("    - Purple Bonus:     "+purpleBonus);
        System.out.println("    - Black Bonus:      "+blackBonus);
        //TODO: print dices bonuses
        System.out.println("[END STRENGHTS PRINT]");
    }

}