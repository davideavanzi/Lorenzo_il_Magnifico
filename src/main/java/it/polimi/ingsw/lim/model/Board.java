package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class acts as a hub between all places of the game.
 */
public class Board {

    /**
     * Constructor called by game class
     */
    public Board() {
    }

    /**
     * This maps the three excommunication with an int representing it's age
     */
    private HashMap<Integer, Excommunication> Excomunications;

    /**
     * This list holds slots for the production site.
     */
    private ArrayList<FamilyMember> production;

    /**
     * This list holds slots for the harvest site
     */
    private ArrayList<FamilyMember> harvest;

    /**
     * This is the council.
     */
    private Council council;

    /**
     * The towers, mapped by color with a string
     * GREEN, YELLOW, BLUE, PURPLE, BLACK
     */
    private HashMap<String, Tower> towers;

    /**
     * The faith track is an array of 30 bonuses, specified with the Assets type
     */
    private Assets[] faithTrack;

    /**
     * Link to the market
     */
    private Market market;

    /**
     * Link to the cards container
     */
    private CardsDeck cardsDeck;

    /**
     * The three dices, mapped by color: BLACK, WHITE, ORANGE, NEUTRAL (always 0)
     */
    private HashMap<String, Integer> dice;

    // ############################################################# METHODS AHEAD

    // GETTERS

    /**
     * @return an excommunication based on it's
     * @param age
     */
    public Excommunication GetExcommunication(int age){
        return this.Excomunications.get(age);
    }

    /**
     * This method rolls dices
     */
    public void rollDices(){
        //For every dice, generates a random number between 1 and 6.
        Random randomGenerator = new Random();
        this.dice.put("BLACK", randomGenerator.nextInt(5)+1);
        this.dice.put("ORANGE", randomGenerator.nextInt(5)+1);
        this.dice.put("WHITE", randomGenerator.nextInt(5)+1);
    }







}