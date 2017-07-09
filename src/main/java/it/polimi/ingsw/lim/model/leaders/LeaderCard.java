package it.polimi.ingsw.lim.model.leaders;

import it.polimi.ingsw.lim.model.Assets;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 */
public abstract class LeaderCard implements Serializable {

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
    private String description;

    /**
     * Default constructor
     */
    public LeaderCard() {
    }


    protected LeaderCard(Builder builder) {
        cardName = builder.cardName;
        leaderCardId = builder.leaderId;
        assetsRequirement = builder.assetsRequirement;
        cardsRequirement = builder.cardsRequirement;
        this.description = builder.description;
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

    public Assets getAssetsRequirement() {
        return assetsRequirement;
    }

    public void setAssetsRequirement(Assets assetsRequirement){
        this.assetsRequirement = assetsRequirement;
    }

    public HashMap<String, Integer> getCardsRequirement() {
        return cardsRequirement;
    }

    public void setCardsRequirement(HashMap<String, Integer> cardsRequirement){
        this.cardsRequirement= cardsRequirement;
    }

    public int getLeaderCardId() {
        return leaderCardId;
    }

    public void setLeaderCardId(int leaderCardId){
        this.leaderCardId = leaderCardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName){
        this.cardName = cardName;
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
        private String description;

        Builder() {}

        public abstract T getThis();

        public T name(String name) { cardName = name; return getThis(); }
        public T id(int id) { leaderId = id;  return getThis(); }
        public T description(String descr) { description = descr;  return getThis(); }
        public T assetsRequirement(Assets requirement) { assetsRequirement = requirement;  return getThis(); }
        public T cardsRequirement(HashMap<String, Integer> cardsRequirement)
        { cardsRequirement.putAll(cardsRequirement);  return getThis(); }

        public LeaderCard build() { return new LeaderCard(this) {
        }; }

        @Override
        public int hashCode() {
            int result = cardName.hashCode();
            result = 31 * result + leaderId;
            result = 31 * result + (assetsRequirement != null ? assetsRequirement.hashCode() : 0);
            result = 31 * result + (cardsRequirement != null ? cardsRequirement.hashCode() : 0);
            result = 31 * result + description.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Builder)) return false;

            Builder<?> builder = (Builder<?>) o;

            if (leaderId != builder.leaderId) return false;
            if (!cardName.equals(builder.cardName)) return false;
            if (assetsRequirement != null ? !assetsRequirement.equals(builder.assetsRequirement) : builder.assetsRequirement != null)
                return false;
            if (cardsRequirement != null ? !cardsRequirement.equals(builder.cardsRequirement) : builder.cardsRequirement != null)
                return false;
            return description.equals(builder.description);
        }
    }



}