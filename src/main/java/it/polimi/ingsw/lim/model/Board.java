package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.model.excommunications.Excommunication;

import static it.polimi.ingsw.lim.Settings.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ava on 12/06/17.
 */
public class Board implements Serializable {

    /**
     * Constructor
     */
    public Board() {
        this.towers = new HashMap<>();
        this.production = new ArrayList<>();
        this.harvest = new ArrayList<>();
        this.faithTrack = new Assets[FAITH_TRACK_LENGTH];
        this.excommunications = new ArrayList<>();
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

    // GETTERS & SETTERS

    public List<Excommunication> getExcommunications() {
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
}
