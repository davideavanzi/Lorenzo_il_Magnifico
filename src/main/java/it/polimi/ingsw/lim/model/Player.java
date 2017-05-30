package it.polimi.ingsw.lim.model;
import java.util.*;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * Player are indexed by nickname, which corresponds to he user that is playing, and that username is unique
 */
public class Player {

    /**
     * Creating an empty player with a nickname.
     */
    public Player(String nickname, String color) {
        //Creating objects
        this.nickname = nickname;
        this.resources = new Assets();
        this.strength = new Strengths();
        this.leaderCards = new ArrayList<>();
        this.familyMembers = new ArrayList<>();
        this.pickDiscounts = new HashMap<>();
        this.defaultHarvestBonus = new Assets();
        this.defaultProductionBonus = new Assets();
        this.color = color;
        getLog().info("New empty player "+nickname+" created.");
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
     * 
     */
    private Strengths strength;

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
     * 
     */
    private Card cards;

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

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setResources(Assets resources){
        this.resources = resources;
    }

    public void setColor(String color) { this.color = color; }

    public FamilyMember pullFamilyMember(String color) {
        return this.familyMembers.stream().filter(fm -> fm.getDiceColor().equals(color)).findFirst().orElse(null);
    }



}