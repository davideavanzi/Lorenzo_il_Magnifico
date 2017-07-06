package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * 
 */
public abstract class LeaderCard {

    /**
     * Default constructor
     */
    public LeaderCard() {
    }

    /**
     * 
     */
    private String cardName;

    /**
     * 
     */
    private int leaderCardId;

    /**
     * 
     */
    private Assets assetsRequirement;

    /**
     * 
     */
    private HashMap<String, Integer> cardsRequirement;

    public void setCardName(String cardName){
        this.cardName = cardName;
    }

    public void setLeaderCardId(int leaderCardId){
        this.leaderCardId = leaderCardId;
    }

    public void setAssetsRequirement(Assets assetsRequirement){
        this.assetsRequirement = assetsRequirement;
    }

    public void setCardsRequirement(HashMap<String, Integer> cardsRequirement){
        this.cardsRequirement= cardsRequirement;
    }

    public Assets getAssetsRequirement() {
        return assetsRequirement;
    }

    public HashMap<String, Integer> getCardsRequirement() {
        return cardsRequirement;
    }

    public int getLeaderCardId() {
        return leaderCardId;
    }

    public String getCardName() {
        return cardName;
    }
}