package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.Settings.*;

import java.util.*;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * Class representing a tower.
 * TODO: The tower itself doesn't know it's color, only the game knows. Is this right?
 */
public class Tower {

    /**
     * Default constructor
     * TODO: can I do better?
     */
    public Tower(Assets[] slotsBonuses) {
        //Creating floors
        this.floors = new Floor[TOWER_HEIGHT];
        //Adding bonuses to each floor, ordere
        for(int i = 0; i < TOWER_HEIGHT; i++){
            Floor fl = new Floor();
            floors[i] = fl;
            fl.setInstantBonus(slotsBonuses[i]);
        }
    }


    /**
     * 
     */
    private Floor[] floors;

    /**
     * This method clears all cards and family members of the tower
     * TODO: use lambda function
     */
    public void clear(){
        Arrays.stream(floors).forEach(floor -> {floor.setFamilyMemberSlot(null); floor.setCardSlot(null);});
    }

    /**
     * Setter that picks an arraylist of cards and puts them in the tower's card slots
     * uses a lambda expression
     */
    public void addCards(ArrayList<Card> cards){
        Arrays.stream(floors).forEach(floor -> floor.setCardSlot(cards.remove(0)));
    }

}