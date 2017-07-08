package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.model.excommunications.Excommunication;
import org.codehaus.jackson.annotate.JsonIgnore;

import static it.polimi.ingsw.lim.Settings.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ava on 12/06/17.
 */
public class Board implements Serializable {

    /**
     * Constructor
     */
    public Board() {
        this.towers = new HashMap<>();
        DEFAULT_TOWERS_COLORS.forEach(color ->
            this.towers.put(color, new Tower())); //todo con 5 player fallisce nel restorare il gioco perhce la torre nera non e creta
        this.production = new ArrayList<>();
        this.harvest = new ArrayList<>();
        this.faithTrack = new Assets[FAITH_TRACK_LENGTH];
        this.excommunications = new ArrayList<>();
        this.excommunications = new ArrayList<>();
        this.dice = new HashMap<>();
        DICE_COLORS.forEach(color -> this.dice.put(color, 0));
        this.randomGenerator = new Random();
    }

    /**
     * The age in which the game is.
     */
    private int age;

    /**
     * The turn in which the game is.
     */
    private int turn;

    /**
     * This holds excommunications
     */
    private ArrayList<Excommunication> excommunications;

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
     * The three dices, mapped by color: BLACK, WHITE, ORANGE, NEUTRAL (always 0)
     */
    private HashMap<String, Integer> dice;

    /**
     * Random generator to roll dices
     */
    @JsonIgnore
    private Random randomGenerator;

    // GETTERS & SETTERS

    /**
     * This method rolls dices
     */
    public void rollDices(){
        //For every dice, generates a random number between 1 and 6.
        DICE_COLORS.forEach(color -> this.dice.replace(color, randomGenerator.nextInt(6)+1));
    }

    public ArrayList<Excommunication> getExcommunications() {
        return this.excommunications;
    }

    public void addExcommunication(Excommunication ex) { this.excommunications.add(ex); }

    public ArrayList<FamilyMember> getProduction() { return this.production; }

    public ArrayList<FamilyMember> getHarvest() { return harvest; }

    public Council getCouncil() { return council; }

    public Assets[] getFaithTrack() {
        return faithTrack;
    }

    public HashMap<String, Tower> getTowers() { return towers; }

    public Market getMarket() {
        return market;
    }

    public void setExcommunications(ArrayList<Excommunication> excommunications) {
        this.excommunications = excommunications;
    }

    public void setTowers (HashMap<String, Tower> towers){

        this.towers = towers;
    }

    public void setFaithTrack (Assets[] faithTrack){this.faithTrack = faithTrack;}

    public void setDice (HashMap<String, Integer> dice){this.dice = dice;}

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setCouncil(Council council) {
        this.council = council;
    }

    public void setHarvest(ArrayList<FamilyMember> harvest) {
        this.harvest = harvest;
    }

    public void setProduction(ArrayList<FamilyMember> production) {
        this.production = production;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTurn() { return turn; }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public HashMap<String, Integer> getDice() {
        return dice;
    }

    public Excommunication getExcommunicationsByAge(int age) {
        return this.excommunications.get(age - 1);
    }
}
