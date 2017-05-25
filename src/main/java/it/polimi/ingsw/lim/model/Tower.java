package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.Settings.*;

import java.util.*;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * Class representing a tower
 */
public class Tower {

    /**
     * Default constructor
     */
    public Tower(String color, Assets[] slotsBonuses) {
        //a tower has always four floors
        this.floors = new Floor[TOWER_HEIGHT];
        this.color = color;
        //TODO: add bonus to each floor, assuming they are in the right order
    }

    /**
     * 
     */
    private String color;

    /**
     * 
     */
    private Floor[] floors;

    /**
     * This method clears all cards and family members of the tower
     */
    public void clean(){
        for (Floor fl : floors){
            fl.setFamilyMemberSlot(null);
            fl.setCardSlot(null);
        }
    }

    /**
     * Setter that picks an arraylist of cards and puts them in the tower's card slots
     * uses a lambda expression
     */
    public void addCards(ArrayList<Card> cards){
        Arrays.stream(floors).forEach(floor -> floor.setCardSlot(cards.remove(0)));
    }

}