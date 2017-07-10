package it.polimi.ingsw.lim.model.cards;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.immediateEffects.ImmediateEffect;

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
    public BlackCard(){
        super();
    }

    public BlackCard(String name, int age, Assets cost,
                     ArrayList<ImmediateEffect> iEffects) {
        super(name, age, cost, iEffects);
    }

    /**
     * the task of this method is to compare if two BlackCard are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two BlackCard to be compared
     * @return true if the BlackCard are equal, false otherwise
     */
    @Override
    public boolean equals (Object other) {
        if (!(other instanceof BlackCard)) {
            Log.getLog().info("other not BlackCard");
            return false;
        }
        Log.getLog().info("***BLACK CARD_CMD Testing Equals***");
        Card card = (Card) other;
        if (!(super.equals(card))) {
            return false;
        }
        Log.getLog().info("BlackCard Equal");
        return true;
    }
}