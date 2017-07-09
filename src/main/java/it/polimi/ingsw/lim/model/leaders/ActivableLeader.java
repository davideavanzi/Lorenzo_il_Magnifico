package it.polimi.ingsw.lim.model.leaders;

import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;

/**
 * This class represents all Leaders that have an effect activated once per turn
 */
public class ActivableLeader extends LeaderCard {

    /**
     * All Leaders of this kind have an effect that can be modelled with an immediate effect
     */
    private ImmediateEffect effect;
    private boolean activated;

    /**
     * Default constructor
     */
    public ActivableLeader() {
    }

    protected ActivableLeader(Builder builder) {
        super(builder);
        effect = builder.immediateEffect;
        activated = false;
    }

    public ImmediateEffect getEffect() {
        return effect;
    }

    public boolean isActivated() {
        return this.activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getEffect() != null ? getEffect().hashCode() : 0);
        result = 31 * result + (isActivated() ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivableLeader)) return false;
        if (!super.equals(o)) return false;

        ActivableLeader that = (ActivableLeader) o;

        if (isActivated() != that.isActivated()) return false;
        return getEffect() != null ? getEffect().equals(that.getEffect()) : that.getEffect() == null;
    }

    public static class Builder extends LeaderCard.Builder<Builder> {

        private ImmediateEffect immediateEffect;

        public Builder() {}

        @Override
        public Builder getThis() { return this; }

        public ActivableLeader build() { return new ActivableLeader(this); }

        public Builder immediateEffect(ImmediateEffect iEffect) { immediateEffect = iEffect; return this; }
    }

}