package it.polimi.ingsw.lim.model.leaders;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;

import java.util.*;

/**
 * This class represents all Leaders that have an effect activated once per turn
 */
public class ActivableLeader extends LeaderCard {

    /**
     * Default constructor
     */
    public ActivableLeader() {
    }

    protected ActivableLeader(Builder builder) {
        super(builder);
        effect = builder.immediateEffect;
    }

    /**
     * All Leaders of this kind have an effect that can be modelled with an immediate effect
     */
    private ImmediateEffect effect;

    /**
     * This List of integers is updated everytime the player activates this card. The integer represents the number
     * of the game turn. This is useful for checking whether a card has been played in one turn or not.
     */
    private ArrayList<Integer> roundsActivated;


    public static class Builder extends LeaderCard.Builder<Builder> {

        @Override
        public Builder getThis() { return this; }

        private ImmediateEffect immediateEffect;

        public Builder() {}

        public Builder immediateEffect(ImmediateEffect iEffect) { immediateEffect = iEffect; return this; }

        public ActivableLeader build() { return new ActivableLeader(this); }
    }

}