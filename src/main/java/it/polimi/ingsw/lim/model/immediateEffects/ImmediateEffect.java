package it.polimi.ingsw.lim.model.immediateEffects;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

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

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionEffect.class, name = "ActionEffect"),

        @JsonSubTypes.Type(value = AssetsEffect.class, name = "AssetsEffect"),

        @JsonSubTypes.Type(value = AssetsMultipliedEffect.class, name = "AssetsMultipliedEffect" ),

        @JsonSubTypes.Type(value = CardMultipliedEffect.class, name = "CardMultipliedEffect"),

        @JsonSubTypes.Type(value = CouncilFavorsEffect.class, name = "CouncilFavorsEffect" )
})
public abstract class ImmediateEffect implements Serializable {

    /**
     * Default constructor
     */
    public ImmediateEffect() {
    }

}