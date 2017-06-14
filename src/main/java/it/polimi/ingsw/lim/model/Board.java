package it.polimi.ingsw.lim.model;
import static it.polimi.ingsw.lim.Settings.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ava on 12/06/17.
 */
public class Board {

    /**
     * Constructor
     */
    public Board() {
        this.towers = new HashMap<>();
        this.production = new ArrayList<>();
        this.harvest = new ArrayList<>();
        this.faithTrack = new Assets[FAITH_TRACK_LENGTH];
    }

    /**
     * This maps the three excommunication with an int representing it's age
     */
    private HashMap<Integer, Excommunication> Excommunications;

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


    // GETTERS

    public HashMap<Integer, Excommunication> getExcommunications() {
        return this.Excommunications;
    }

    public ArrayList<FamilyMember> getProduction() { return this.production; }

    public ArrayList<FamilyMember> getHarvest() { return harvest; }

    public Council getCouncil() { return council; }

    public Assets[] getFaithTrack() {
        return faithTrack;
    }

    public HashMap<String, Tower> getTowers() {
        return towers;
    }

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
}
