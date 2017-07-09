package it.polimi.ingsw.lim.model.leaders;

/**
 * 
 */
public class PermanentLeader extends LeaderCard {

    /**
     * Default constructor
     */
    public PermanentLeader() {
    }



    protected PermanentLeader(PermanentLeader.Builder builder) {
        super(builder);
    }

    public static class Builder extends LeaderCard.Builder<PermanentLeader.Builder> {

        public Builder() {}

        @Override
        public PermanentLeader.Builder getThis() { return this; }

        public PermanentLeader build() { return new PermanentLeader(this); }
    }

}