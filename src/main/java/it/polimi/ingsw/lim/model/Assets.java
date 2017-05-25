package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class acts as a container for every kind of resource that a player can have. Also all kind of bonuses and costs
 * in terms of resources are represented with objects of this class.
 */
public class Assets {

    /**
     * Default constructor, puts every resource to 0
     */
    public Assets() {
    }

    /**
     * Fast constructor, utilized for parsing assets objects from file
     */
    public Assets(int coins, int wood, int stone, int servants, int faithPoints, int battlePoints, int victoryPoints){
        this.coins = coins;
        this.wood = wood;
        this.stone = stone;
        this.servants = servants;
        this.faithPoints = faithPoints;
        this.battlePoints = battlePoints;
        this.victoryPoints = victoryPoints;
    }

    /**
     * This integer represents the amount of coins in the object
     */
    private int coins;

    /**
     * This integer represents the amount of wood in the object
     */
    private int wood;

    /**
     * This integer represents the amount of stone in the object
     */
    private int stone;

    /**
     * This integer represents the amount of servants in the object
     */
    private int servants;

    /**
     * This integer represents the amount of faithPoints in the object
     */
    private int faithPoints;

    /**
     * This integer represents the amount of battlePoints in the object
     */
    private int battlePoints;

    /**
     * This integer represents the amount of victoryPoints in the object
     */
    private int victoryPoints;

    /**
     * This method executes a sum between two objects of this class.
     * @param operand is the second operand.
     */
    public void add(Assets operand) {
        // TODO implement here
    }

    /**
     * This method multiplies two objects of this class.
     * @param operand is the second operand.
     * Every value of the first object is multiplied by the corresponding value of the second operand.
     */
    public void multiply(Assets operand) {
        // TODO implement here
    }

    /**
     * This method multiplies all values of the object by the integer given.
     * @param operand is the integer used in the operation.
     */
    public void multiply(int operand) {
        // TODO implement here
    }

    /**
     * This prints assets
     */
    public void printAssets(){
        System.out.println("[ASSETS PRINT]");
        System.out.println("    - Gold:             "+coins);
        System.out.println("    - Wood:             "+wood);
        System.out.println("    - Stone:            "+stone);
        System.out.println("    - Servants:         "+servants);
        System.out.println("    - FaithPoints:      "+faithPoints);
        System.out.println("    - BattlePoints:     "+battlePoints);
        System.out.println("    - VictoryPoints:    "+victoryPoints);
        System.out.println("[END ASSETS PRINT]");
    }

}