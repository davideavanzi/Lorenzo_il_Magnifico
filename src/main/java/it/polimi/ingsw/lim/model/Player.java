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







}