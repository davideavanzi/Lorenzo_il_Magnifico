package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents the fifth card type, used in case of a match with five players.
 * Black cards are bad actions (such as theft, kidnap, blackmail): they cost a lot of faith points but
 * give a great bonus in terms of resources.
 */
public class BlackCard extends Card {

    /**
     * Default constructor
     */
    public BlackCard(String name, int age, Assets cost,
                     ArrayList<ImmediateEffect> iEffects) {
        super(name, age, cost, iEffects);
    }

}