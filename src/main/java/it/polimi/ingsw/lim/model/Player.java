package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.model.cards.Card;
import it.polimi.ingsw.lim.model.leaders.ActivableLeader;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.lim.Settings.BLACK_COLOR;
import static it.polimi.ingsw.lim.Settings.DEFAULT_TOWERS_COLORS;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * Player are indexed by nickname, which corresponds to he user that is playing, and that username is unique
 */
public class Player implements Serializable{

    /**
     * The nickname of the player, unique in the current server session
     */
    private String nickname;
    /**
     * The color of the player, unique in the room/game
     */
    private String color;
    /**
     * The player's assets (his wallet)
     */
    private Assets resources;
    /**
     * These are the strengths of the player. Elements inside it could be both positive and negative.
     */
    private Strengths strengths;
    /**
     * This HashMap holds temporary bonuses to dices value that will be erased at the end of the turn
     */
    private HashMap<String, Integer> diceOverride;
    /**
     * The player's leader cards
     */
    private ArrayList<LeaderCard> leaderCards;
    /**
     * The player's pick discounts. These are optional discounts in terms of assets that a player can have
     * to pick cards from towers
     */
    private HashMap<String, Assets> pickDiscounts;
    /**
     * This is a container for all player's development cards.
     * ArrayLists of this HashMap are instantiated in setupGame() method.
     */
    private HashMap<String, ArrayList<Card>> cards;
    /**
     * The family members that a player can deploy on the board
     */
    private ArrayList<FamilyMember> familyMembers;
    /**
     * Default production bonus that is given to the user performing a production action,
     * with strength greater or equal of the default action cost.
     */
    private Assets defaultProductionBonus;
    /**
     * Default production bonus that is given to the user performing a production action,
     * with strength greater or equal of the default action cost.
     */
    private Assets defaultHarvestBonus;

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
        this.diceOverride = new HashMap<>();
        this.defaultHarvestBonus = new Assets();
        this.defaultProductionBonus = new Assets();
        this.color = color;
        this.cards = new HashMap<>();
        DEFAULT_TOWERS_COLORS.forEach(towerColor -> this.pickDiscounts.put(towerColor, new Assets()));
        pickDiscounts.put(BLACK_COLOR, new Assets());
        getLog().log(Level.INFO, "New empty player %s created.", nickname);
    }

    void addLeader(LeaderCard leader) {
        this.leaderCards.add(leader);
    }

    public LeaderCard getLeaderById(int id) {
        return leaderCards.stream().filter(leader -> leader.getLeaderCardId() == id).findFirst().orElse(null);
    }

    public ArrayList<LeaderCard> getDeployedLeaders() {
        return new ArrayList<>(leaderCards.stream().filter(leader -> leader.isDeployed())
                .collect(Collectors.toList()));
    }

    public ArrayList<LeaderCard> getActivatedLeaders() {
        return new ArrayList<>(leaderCards.stream().filter(leader -> leader instanceof ActivableLeader &&
                ((ActivableLeader) leader).isActivated())
                .collect(Collectors.toList()));
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards){
        this.leaderCards = leaderCards;
    }

    public HashMap<String, Assets> getPickDiscounts() {
        return pickDiscounts;
    }

    public void setPickDiscounts (HashMap<String, Assets> pickDiscounts){
        this.pickDiscounts = pickDiscounts;
    }

    public void clearFamilyMembers() { this.familyMembers = new ArrayList<>(); }

    public void addFamilyMember(FamilyMember fm){
        this.familyMembers.add(fm);
    }

    public ArrayList<FamilyMember> getFamilyMembers() { return this.familyMembers; }

    public void setFamilyMembers(ArrayList<FamilyMember> familyMembers){
        this.familyMembers = familyMembers;
    }

    @JsonIgnore
    public FamilyMember getFamilyMember(String color) {
        FamilyMember fm2 = this.familyMembers.stream().filter(fm -> fm.getDiceColor().equals(color)).findFirst().orElse(null);
        System.out.println("GETTING FAMILY MEMBER OF COLOR: "+color+" -> found fm of color "+fm2.getDiceColor());
        return fm2;
    }

    public String getColor(){
        return this.color;
    }

    public void setColor(String color) { this.color = color; }

    public Assets getResources() { return this.resources; }

    public void setResources(Assets resources){
        this.resources = resources;
    }

    public String getNickname() { return  this.nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    @JsonIgnore
    public Assets getPickDiscount(String color) {
        return this.pickDiscounts.get(color);
    }

    public HashMap getCards() { return this.cards; }

    public void setCards (HashMap<String, ArrayList<Card>> cards){
        this.cards = cards;
    }

    @JsonIgnore
    public ArrayList<Card> getCardsOfColor(String color) { return this.cards.get(color); }

    public void addCard(Card card, String color) {
        this.cards.get(color).add(card);
    }

    public FamilyMember pullFamilyMember(String color) {
        for (FamilyMember fm : familyMembers)
            if (fm.getDiceColor().equals(color))
                return familyMembers.remove(familyMembers.indexOf(fm));
        return null;
    }

    public void resetFaithPoints() {
        this.resources.setFaithPoints(0);
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

    public void setDefaultHarvestBonus(Assets defaultHarvestBonus){
        this.defaultHarvestBonus = defaultHarvestBonus;
    }

    public Assets getDefaultProductionBonus() {
        return defaultProductionBonus;
    }

    public void setDefaultProductionBonus(Assets defaultProductionBonus){
        this.defaultHarvestBonus = defaultProductionBonus;
    }

    public HashMap<String, Integer> getDiceOverride() {
        return diceOverride;
    }

    public void setDiceOverride(HashMap<String, Integer> diceOverride) {
        this.diceOverride = diceOverride;
    }

    //Used in lambda to generate rank
    @JsonIgnore
    public int getMilitaryPoints() { return this.resources.getBattlePoints(); }

    @Override
    public int hashCode() {
        int result = getNickname().hashCode();
        result = 31 * result + getColor().hashCode();
        result = 31 * result + getResources().hashCode();
        result = 31 * result + getStrengths().hashCode();
        result = 31 * result + (getDiceOverride() != null ? getDiceOverride().hashCode() : 0);
        result = 31 * result + (getLeaderCards() != null ? getLeaderCards().hashCode() : 0);
        result = 31 * result + (getPickDiscounts() != null ? getPickDiscounts().hashCode() : 0);
        result = 31 * result + getCards().hashCode();
        result = 31 * result + getFamilyMembers().hashCode();
        result = 31 * result + getDefaultProductionBonus().hashCode();
        result = 31 * result + getDefaultHarvestBonus().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (!getNickname().equals(player.getNickname())) return false;
        if (!getColor().equals(player.getColor())) return false;
        if (!getResources().equals(player.getResources())) return false;
        if (!getStrengths().equals(player.getStrengths())) return false;
        if (getDiceOverride() != null ? !getDiceOverride().equals(player.getDiceOverride()) : player.getDiceOverride() != null)
            return false;
        if (getLeaderCards() != null ? !getLeaderCards().equals(player.getLeaderCards()) : player.getLeaderCards() != null)
            return false;
        if (getPickDiscounts() != null ? !getPickDiscounts().equals(player.getPickDiscounts()) : player.getPickDiscounts() != null)
            return false;
        if (!getCards().equals(player.getCards())) return false;
        if (!getFamilyMembers().equals(player.getFamilyMembers())) return false;
        if (!getDefaultProductionBonus().equals(player.getDefaultProductionBonus())) return false;
        return getDefaultHarvestBonus().equals(player.getDefaultHarvestBonus());
    }
}