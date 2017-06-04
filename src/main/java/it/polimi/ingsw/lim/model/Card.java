package it.polimi.ingsw.lim.model;
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

}