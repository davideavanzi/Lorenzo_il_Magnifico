package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.Log;

import java.util.*;

/**
 * This abstract class is responsible of representing a generic development card. Each subclass will define different
 * behaviours for the 5 kinds of cards in the game (Green, Yellow, Blue, Purple, Black)
 */
public abstract class Card {

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

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

    public ArrayList<ImmediateEffect> getImmediateEffects(){
        return this.immediateEffects;
    }

    /**
     * Contructor
     * @param name
     * @param age
     * @param cost
     * @param iEffects
     */
    public Card(String name, int age, Assets cost, ArrayList<ImmediateEffect> iEffects){
        //TODO: a card must have a name and an age, otherwise throws exception
        this.name = name;
        this.cost = cost;
        this.age = age;
        this.immediateEffects = iEffects;
    }

    /**
     * Default constructor
     */
    public Card(){}

    /**
     * Temporary method to print the card
     */
    public void printCard(){
        System.out.println("___________________________________________________");
        System.out.println("[CARD PRINT]:\n Name:"+name+"\nAge:"+age);
        if (cost != null){
            System.out.println("PRINTING COST");
            cost.printAssets();

        }
        //TODO:print effect
        if (immediateEffects != null)
            for (ImmediateEffect ie : immediateEffects) {
                ie.printEffect();
            }
    }

    public Assets getCost () { return this.cost; }

    /**
     * the task of this method is to compare name, age, cost, immediateEffect of two card and return true if they are
     * equals false otherwise
     * N.B.->This method does not override equals in Object because this is a method that is only called by equals
     * method in Green/Blue/Yellow/Purple/Black-Card
     * @param other is one of the two card to be compared
     * @return true if the card are equal, false otherwise
     */
    public boolean equals (Card other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("other = null");
            return false;
        }
        Card card = (Card) other;
        Boolean equals = true;
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
        if(!(this.getImmediateEffects().size() == card.getImmediateEffects().size())){
            Log.getLog().info("immediate effect size different");
            equals = false;
        }
        for(int i = 0; i < this.getImmediateEffects().size(); i++){
            if (!(this.getImmediateEffects().get(i).equals(card.getImmediateEffects().get(i)))){
                Log.getLog().info("immediate effect " + i + " different");
                equals = false;
            }
        }
        return equals;
    }

}