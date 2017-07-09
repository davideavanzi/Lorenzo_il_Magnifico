package it.polimi.ingsw.lim.model.leaders;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.leaders.ActivableLeader.Builder;

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

    private boolean deployed = false;

    private boolean discarded = false;


    protected LeaderCard(Builder builder) {
        cardName = builder.cardName;
        leaderCardId = builder.leaderId;
        assetsRequirement = builder.assetsRequirement;
        cardsRequirement = builder.cardsRequirement;
    }

    /**
     * Leaders are built with the builder pattern.
     * The idea of using a generics T to instantiate subclasses with the builder comes from:
     * https://stackoverflow.com/questions/17164375/subclassing-a-java-builder-class
     * @param <T> the generic builder passed from subclasses' builder instance
     */
    public abstract static class Builder<T extends Builder<T>> {

        private String cardName;
        private int leaderId;
        private Assets assetsRequirement;
        private HashMap<String, Integer> cardsRequirement = new HashMap<>();

        Builder() {}

        public abstract T getThis();

        public T name(String name) { cardName = name; return getThis(); }
        public T id(int id) { leaderId = id;  return getThis(); }
        public T assetsRequirement(Assets requirement) { assetsRequirement = requirement;  return getThis(); }
        public T cardsRequirement(HashMap<String, Integer> cardsRequirement)
        { cardsRequirement.putAll(cardsRequirement);  return getThis(); }

        public LeaderCard build() { return new LeaderCard(this) {
        }; }
    }

    public boolean isDeployed() { return this.deployed; }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

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