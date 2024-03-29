package it.polimi.ingsw.lim.model.cards;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;
import it.polimi.ingsw.lim.utils.Log;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This abstract class is responsible of representing a generic development card. Each subclass will define different
 * behaviours for the 5 kinds of cards in the game (Green, Yellow, Blue, Purple, Black)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GreenCard.class, name = "GreenCard"),

        @JsonSubTypes.Type(value = BlueCard.class, name = "BlueCard"),

        @JsonSubTypes.Type(value = YellowCard.class, name = "YellowCard" ),

        @JsonSubTypes.Type(value = PurpleCard.class, name = "PurpleCard"),

        @JsonSubTypes.Type(value = BlackCard.class, name = "BlackCard" )
})

public abstract class Card implements Serializable {

    /**
     * The name of the card
     */
    private String name;

    /**
     * The cost to pick the card. This is not mandatory
     */
    private Assets cost;

    /**
     * The age of the card
     */
    private int age;

    /**
     * The list of immediate effects that a card can have. This is not mandatory
     */
    private ArrayList<ImmediateEffect> immediateEffects;

    /**
     * Contructor
     * @param name
     * @param age
     * @param cost
     * @param iEffects
     */
    public Card(String name, int age, Assets cost, ArrayList<ImmediateEffect> iEffects){
        this.name = name;
        this.cost = (cost == null) ? new Assets() : cost;
        this.age = age;
        this.immediateEffects = iEffects;
    }

    /**
     * Default constructor
     */
    public Card(){}

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getAge(){
        return this.age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public ArrayList<ImmediateEffect> getImmediateEffects(){
        return this.immediateEffects;
    }

    public void setImmediateEffects(ArrayList<ImmediateEffect> immediateEffects){
        this.immediateEffects = immediateEffects;
    }

    public Assets getCost () { return this.cost; }

    public void setCost(Assets cost){
        this.cost = cost;
    }

    public boolean hasCost(){
        return this.cost.isNotNull();
    }

    /**
     * the task of this method is to compare name, age, cost, immediateEffect of two card and return true if they are
     * equals false otherwise
     * N.B.->This method does not override equals in Object because this is a method that is only called by equals
     * method in Green/Blue/Yellow/Purple/Black-Card
     * @param other is one of the two card to be compared
     * @return true if the card are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("other = null");
            return false;
        }
        if(!(other instanceof Card)){
            Log.getLog().info("other not a card");
            return false;
        }
        Card card = (Card) other;
        boolean equals = true;
        if(!(this.getName().equals(card.getName()))){
            Log.getLog().info("name different");
            equals = false;
        }
        if(!(this.getAge() == (card.getAge()))){
            Log.getLog().info("age different");
            equals = false;
        }
        if(!(this.getCost().equals(card.getCost()))){
            Log.getLog().info("cost different");
            equals = false;
        }
        //starting to compare the immediate effects
        if(this.getImmediateEffects().size() != card.getImmediateEffects().size()){
            Log.getLog().info("immediate effect size different");
            equals = false;
        }
        for(int i = 0; i < this.getImmediateEffects().size(); i++){
            if (!(this.getImmediateEffects().get(i).equals(card.getImmediateEffects().get(i)))){
                Log.getLog().info(("immediate effect " + i).concat(" different"));
                equals = false;
            }
        }
        return equals;
    }

}