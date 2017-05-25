package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * 
 */
public class Player {

    /**
     * Default constructor
     */
    public Player() {
        this.resources = new Assets();
    }

    /**
     * 
     */
    private int playerId;

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
     * 
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
    public void addFamilyMember(FamilyMember fm){
        this.familyMembers.add(fm);
    }

    public String getColor(){
        return this.color;
    }

    public Assets getResources() { return this.resources; }

    public void setResources(Assets resources){
        this.resources = resources;
    }





}