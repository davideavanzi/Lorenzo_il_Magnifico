package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;
import static it.polimi.ingsw.lim.Utils.*;

import java.io.Serializable;
import java.util.*;

/**
 * This class acts as a container for every kind of resource that a player can have. Also all kind of bonuses and costs
 * in terms of resources are represented with objects of this class.
 */
public class Assets implements Serializable {

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
     * Copy constructor. this constructor creates a new copy of the one given.
     * @param copy the object to copy
     */
    public Assets(Assets copy) {
        this(copy.getCoins(),
                copy.getWood(),
                copy.getStone(),
                copy.getServants(),
                copy.getFaithPoints(),
                copy.getBattlePoints(),
                copy.getVictoryPoints());
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
    public Assets add(Assets operand) {
        Assets sum = new Assets();
        sum.coins = this.coins + operand.getCoins();
        sum.wood = this.wood + operand.getWood();
        sum.stone = this.stone + operand.getStone();
        sum.servants = this.servants + operand.getServants();
        sum.faithPoints = this.faithPoints + operand.getFaithPoints();
        sum.battlePoints = this.battlePoints + operand.getBattlePoints();
        sum.victoryPoints = this.victoryPoints + operand.getVictoryPoints();
        return sum;
    }

    public Assets subtract(Assets operand) {
        Assets diff = new Assets();
        diff.coins = this.coins - operand.getCoins();
        diff.wood = this.wood - operand.getWood();
        diff.stone = this.stone - operand.getStone();
        diff.servants = this.servants - operand.getServants();
        diff.faithPoints = this.faithPoints - operand.getFaithPoints();
        diff.battlePoints = this.battlePoints - operand.getBattlePoints();
        diff.victoryPoints = this.victoryPoints - operand.getVictoryPoints();
        return diff;
    }

    public boolean isGreaterOrEqual(Assets operand) {
        return (this.coins >= operand.getCoins() &&
                this.wood >= operand.getWood() &&
                this.stone >= operand.getStone() &&
                this.servants >= operand.getServants() &&
                this.faithPoints >= operand.getFaithPoints() &&
                this.battlePoints >= operand.getFaithPoints() &&
                this.victoryPoints >= operand.getVictoryPoints()
        );
    }

    public boolean isNegative() {
        return (this.coins < 0 &&
                this.wood < 0 &&
                this.stone < 0 &&
                this.servants < 0 &&
                this.faithPoints < 0 &&
                this.battlePoints < 0 &&
                this.victoryPoints < 0);
    }

    /**
     * This method subtracts two Assets without going below zero. (brrr)
     * @param operand the Asset to subtract
     * @return the result
     */
    public Assets subtractToZero(Assets operand) {
        Assets diff = new Assets();
        diff.coins = (this.coins - operand.getCoins() >= 0) ? this.coins - operand.getCoins() : 0;
        diff.wood = (this.wood - operand.getWood() >= 0) ? this.wood - operand.getWood() : 0;
        diff.stone = (this.stone - operand.getStone() >= 0) ? this.stone - operand.getStone() : 0;
        diff.servants = (this.servants - operand.getServants() >= 0) ? this.servants - operand.getServants() : 0;
        diff.faithPoints = (this.faithPoints - operand.getFaithPoints() >= 0) ? this.faithPoints - operand.getFaithPoints() : 0;
        diff.battlePoints = (this.battlePoints - operand.getBattlePoints() >= 0) ? this.battlePoints - operand.getBattlePoints() : 0;
        diff.victoryPoints = (this.victoryPoints - operand.getVictoryPoints() >= 0) ? this.victoryPoints - operand.getVictoryPoints() : 0;
        return diff;
    }

    /**
     * This method multiplies two objects of this class.
     * @param operand is the second operand.
     * Every value of the first object is multiplied by the corresponding value of the second operand.
     */
    public Assets multiply(Assets operand) {
        Assets product = new Assets();
        product.coins = (this.coins * operand.getCoins());
        product.wood = (this.wood * operand.getWood());
        product.stone = (this.stone * operand.getStone());
        product.servants = (this.servants * operand.getServants());
        product.faithPoints = (this.faithPoints * operand.getFaithPoints());
        product.battlePoints = (this.battlePoints * operand.getBattlePoints());
        product.victoryPoints = (this.victoryPoints * operand.getVictoryPoints());
        return product;
    }

    /**
     * This method multiplies all values of the object by the integer given.
     * @param operand is the integer used in the operation.
     */
    public Assets multiply(int operand) {
        Assets product = new Assets();
        product.coins = (this.coins * operand);
        product.wood = (this.wood * operand);
        product.stone = (this.stone * operand);
        product.servants = (this.servants * operand);
        product.faithPoints = (this.faithPoints * operand);
        product.battlePoints = (this.battlePoints * operand);
        product.victoryPoints = (this.victoryPoints * operand);
        return product;
    }

    public int divide(Assets operand) {
        List<Integer> values = new ArrayList<>();
        if (operand.getCoins() != 0 && this.coins/operand.getCoins() > 0)
            values.add(this.coins/operand.getCoins());
        if (operand.getWood() != 0 && this.wood/operand.getWood() > 0)
            values.add(this.wood/operand.getWood());
        if (operand.getStone() != 0 && this.stone/operand.getStone() > 0)
            values.add(this.stone/operand.getStone());
        if (operand.getServants() != 0 && this.servants/operand.getServants() > 0)
            values.add(this.servants/operand.getServants());
        if (operand.getFaithPoints() != 0 && this.faithPoints/operand.getFaithPoints() > 0)
            values.add(this.faithPoints/operand.getFaithPoints());
        if (operand.getBattlePoints() != 0 && this.battlePoints/operand.getBattlePoints() > 0)
            values.add(this.battlePoints/operand.getBattlePoints());
        if (operand.getVictoryPoints() != 0 && this.victoryPoints/operand.getVictoryPoints() > 0)
            values.add(this.victoryPoints/operand.getVictoryPoints());
        Integer min = min(values.toArray(new Integer[values.size()]));
        return (min != null && min != 0) ? min : 0;
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

    /**
     * Single getters
     */
    public int getCoins(){ return this.coins; }
    public int getWood(){ return this.wood; }
    public int getStone(){ return this.stone; }
    public int getServants(){ return this.servants; }
    public int getFaithPoints(){ return this.faithPoints; }
    public int getBattlePoints(){ return this.battlePoints; }
    public int getVictoryPoints(){ return this.victoryPoints; }

    /**
     * Single adders. At the moment we don't need direct setters
     * Write here more adders if needed.
     * @return the object itself (used in game setup)
     */
    public Assets addCoins(int amount) { this.coins += amount; return this; }

    public Assets subtractCoins(int amount) { this.coins -= amount; return this;}

    /**
     * the task of this method is to compare if two assets are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two assets to be compared
     * @return true if the assets are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("***ASSETS***\nother == null");
            return false;
        }
        if(!(other instanceof Assets)){
            Log.getLog().info("***ASSETS***\nother not Assets");
            return false;
        }
        Assets assets = (Assets) other;
        return (this.coins == assets.getCoins() &&
                this.wood == assets.getWood() &&
                this.stone == assets.getStone() &&
                this.servants == assets.getServants() &&
                this.faithPoints == assets.getFaithPoints() &&
                this.battlePoints == assets.getBattlePoints() &&
                this.victoryPoints == assets.getVictoryPoints()
        );
    }
}