package it.polimi.ingsw.lim.model.leaders;

import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;

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

        @Override
        public PermanentLeader.Builder getThis() { return this; }

        public Builder() {}

        public PermanentLeader build() { return new PermanentLeader(this); }
    }

}