package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This abstract class is extended by a subclass for any kind of effect:
 * - ActionEffect
 * - AssetsEffect
 * - AssetsMultipliedEffect
 * - CardMultipliedEffect
 * - CouncilFavorsEffect
 */
public abstract class ImmediateEffect {

    /**
     * Default constructor
     */
    public ImmediateEffect() {
    }

    public abstract void printEffect();
}