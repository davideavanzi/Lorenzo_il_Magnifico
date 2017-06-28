package it.polimi.ingsw.lim.model.immediateEffects;
import java.io.Serializable;
import java.util.*;

/**
 * This abstract class is extended by a subclass for any kind of effect:
 * - ActionEffect
 * - AssetsEffect
 * - AssetsMultipliedEffect
 * - CardMultipliedEffect
 * - CouncilFavorsEffect
 */
public abstract class ImmediateEffect implements Serializable {

    /**
     * Default constructor
     */
    public ImmediateEffect() {
    }

    public abstract void printEffect();
}