package it.polimi.ingsw.lim.model;
import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.DEFAULT_TOWERS_COLORS;

/**
 * Player are indexed by nickname, which corresponds to he user that is playing, and that username is unique
 * TODO: map family members with an hashmap?
 */
public class Player {

    /**
     * Creating an empty player with a nickname.
     */
    public Player(String nickname, String color) {
        //Creating objects
        this.nickname = nickname;
        this.resources = new Assets();
        this.strengths = new Strengths();
        this.leaderCards = new ArrayList<>();
        this.familyMembers = new ArrayList<>();
        this.pickDiscounts = new HashMap<>();
        this.defaultHarvestBonus = new Assets();
        this.defaultProductionBonus = new Assets();
        this.towerBonusAllowed = true;
        this.color = color;
        this.cards = new HashMap<>();
        getLog().log(Level.INFO, "New empty player %s created.", nickname);
    }

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private String color;

    /**
     * 
     */
    private Assets resources;

    /**
     * These are the strengths of the player. Elements inside it could be both positive and negative.
     */
    private Strengths strengths;

    /**
     * 
     */
    private int cardCount;

    /**
     * 
     */
    private ArrayList<LeaderCard> leaderCards;

    /**
     * 
     */
    private HashMap<String, Assets> pickDiscounts;

    /**
     * This boolean indicates if the player is allowed to pick bonuses when entering in towers.
     */
    private Boolean towerBonusAllowed;

    /**
     * This is a container for all development cards.
     * Arraylists of this hashmap are instantiated in setupGame() method.
     */
    private HashMap<String, ArrayList<Card>> cards;

    /**
     * TODO: Is it better to store them with an hashmap?
     */
    private ArrayList<FamilyMember> familyMembers;

    /**
     * 
     */
    private Assets defaultProductionBonus;

    /**
     * 
     */
    private Assets defaultHarvestBonus;

    /**
     *
     */

    public void clearFamilyMembers() { this.familyMembers = new ArrayList<>(); }

    public void addFamilyMember(FamilyMember fm){
        this.familyMembers.add(fm);
    }

    public String getColor(){
        return this.color;
    }

    public Assets getResources() { return this.resources; }

    public String getNickname() { return  this.nickname; }

    public Assets getPickDiscount(String color) {
        return this.pickDiscounts.get(color);
    }

    public HashMap getCards() { return this.cards; }

    public void addCard(Card card, String color) {
        this.cards.get(color).add(card);
    }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setResources(Assets resources){
        this.resources = resources;
    }

    public void setColor(String color) { this.color = color; }

    public FamilyMember pullFamilyMember(String color) {
        return this.familyMembers.stream().filter(fm -> fm.getDiceColor().equals(color)).findFirst().orElse(null);
    }

    public boolean isTowerBonusAllowed() {
        return this.towerBonusAllowed;
    }

    public Strengths getStrengths() {
        return this.strengths;
    }



}