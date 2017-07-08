package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.model.cards.Card;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * Player are indexed by nickname, which corresponds to he user that is playing, and that username is unique
 * TODO: map family members with an hashmap?
 */
public class Player implements Serializable{

    public Player(){

    }

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
        this.color = color;
        this.cards = new HashMap<>();
        DEFAULT_TOWERS_COLORS.forEach(towerColor -> this.pickDiscounts.put(towerColor, new Assets()));
        pickDiscounts.put(BLACK_COLOR, new Assets());
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
    private ArrayList<LeaderCard> leaderCards;

    /**
     * 
     */
    private HashMap<String, Assets> pickDiscounts;

    /**
     * This is a container for all development cards.
     * Arraylists of this hashmap are instantiated in setupGame() method.
     */
    private HashMap<String, ArrayList<Card>> cards;

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

    public LeaderCard getLeaderById(int id) {
        return leaderCards.stream().filter(leader -> leader.getLeaderCardId() == id).findFirst().orElse(null);
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards){
        this.leaderCards = leaderCards;
    }

    public void setPickDiscounts (HashMap<String, Assets> pickDiscounts){
        this.pickDiscounts = pickDiscounts;
    }

    public void setCards (HashMap<String, ArrayList<Card>> cards){
        this.cards = cards;
    }

    public void setFamilyMembers(ArrayList<FamilyMember> familyMembers){
        this.familyMembers = familyMembers;
    }

    public void setDefaultProductionBonus(Assets defaultProductionBonus){
        this.defaultHarvestBonus = defaultProductionBonus;
    }

    public void setDefaultHarvestBonus(Assets defaultHarvestBonus){
        this.defaultHarvestBonus = defaultHarvestBonus;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public HashMap<String, Assets> getPickDiscounts() {
        return pickDiscounts;
    }

    public void clearFamilyMembers() { this.familyMembers = new ArrayList<>(); }

    public void addFamilyMember(FamilyMember fm){
        this.familyMembers.add(fm);
    }

    public ArrayList<FamilyMember> getFamilyMembers() { return this.familyMembers; }

    @JsonIgnore
    public FamilyMember getFamilyMember(String color) {
        return this.familyMembers.stream().filter(fm -> fm.getDiceColor().equals(color)).findFirst().orElse(null);
    }

    public String getColor(){
        return this.color;
    }

    public Assets getResources() { return this.resources; }

    public String getNickname() { return  this.nickname; }

    @JsonIgnore
    public Assets getPickDiscount(String color) {
        return this.pickDiscounts.get(color);
    }

    public HashMap getCards() { return this.cards; }

    @JsonIgnore
    public ArrayList<Card> getCardsOfColor(String color) { return this.cards.get(color); }

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
        /* TODO: does this really works? (Pulls fm)
        for (FamilyMember fm : familyMembers)
            if (fm.getDiceColor().equals(color))
                return familyMembers.remove(familyMembers.indexOf(fm));
        return null; */
    }

    public Strengths getStrengths() {
        return this.strengths;
    }

    public void setStrengths(Strengths strengths) {
        this.strengths = strengths;
    }

    @JsonIgnore
    public void setPickDiscount(String color, Assets value) {
        this.pickDiscounts.replace(color, value);
    }

    @JsonIgnore
    public int getCardsAmount(String color) {
        return this.cards.get(color).size();
    }

    public Assets getDefaultHarvestBonus() {
        return defaultHarvestBonus;
    }

    public Assets getDefaultProductionBonus() {
        return defaultProductionBonus;
    }

    //Used in lambda to generate rank
    @JsonIgnore
    public int getMilitaryPoints() { return this.resources.getBattlePoints(); }

}