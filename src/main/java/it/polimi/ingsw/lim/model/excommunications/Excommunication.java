package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.model.Player;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract class that represents an excommunication. It has always an age, an id and an array that contains
 * the excommunicated players
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetsExcommunication.class, name = "AssetsExcommunications"),

        @JsonSubTypes.Type(value = EndGameAssetsExcommunication.class, name = "EndGameAssetsExcommunication"),

        @JsonSubTypes.Type(value = MarketExcommunication.class, name = "MarketExcommunication" ),

        @JsonSubTypes.Type(value = ServantsExcommunication.class, name = "ServantsExcommunication"),

        @JsonSubTypes.Type(value = StrengthsExcommunication.class, name = "StrengthsExcommunication" ),

        @JsonSubTypes.Type(value = RoundExcommunication.class, name = "RoundExcommunication" ),

        @JsonSubTypes.Type(value = EndGameCardsExcommunication.class, name = "EndGameCardsExcommunication")
})

public abstract class Excommunication implements Serializable {

    /**
     * Default constructor
     */
    public Excommunication() {
        this.excommunicated = new ArrayList<>();
    }

    /**
     * The age of the excommunication.
     */
    private int age;


    /**
     * Excommunicated players will be stored here by their color.
     */
    private ArrayList<String> excommunicated;

    public int getAge() {
        return age;
    }

    public ArrayList<String> getExcommunicated() {
        return excommunicated;
    }

    public void setAge (int age){
        this.age = age;
    }

    public void setExcommunicated (ArrayList<String> excommunicated){
        this.excommunicated = excommunicated;
    }

    public void addExcommunicated(Player pl) {
        excommunicated.add(pl.getColor());
    }
}