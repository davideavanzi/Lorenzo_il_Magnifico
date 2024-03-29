package it.polimi.ingsw.lim.model;


import it.polimi.ingsw.lim.model.cards.Card;
import org.codehaus.jackson.annotate.JsonSetter;

import java.io.Serializable;
import java.util.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * Class representing a tower.
 */
public class Tower implements Serializable {

    Tower(){
        this.floors = new Floor[TOWER_HEIGHT];
    }

    @JsonSetter
    public void setFloors(Floor[] floors){
        this.floors = floors;
    }

    /**
     * Default constructor
     */
    Tower(Assets[] slotsBonuses) {
        //Creating floors
        this.floors = new Floor[TOWER_HEIGHT];
        //Adding bonuses to each floor, ordered
        for(int i = 0; i < TOWER_HEIGHT; i++) {
            Floor fl = new Floor(i);
            fl.setInstantBonus(slotsBonuses[i]);
            floors[i] = fl;
        }
    }


    /**
     * 
     */
    private Floor[] floors;

    /**
     * This method clears all cards and family members of the tower
     */
    public void clear(){
        Arrays.stream(floors).forEach(floor -> {floor.setFamilyMemberSlot(null); floor.setCardSlot(null);});
    }

    /**
     * Setter that picks an arraylist of cards and puts them in the tower's card slots
     * uses a lambda expression
     */
    void addCards(ArrayList<Card> cards){
        Arrays.stream(floors).forEach(floor -> floor.setCardSlot(cards.remove(0)));
    }

    public Floor getFloor(int number) {
        return this.floors[number-1];
    }

    public Floor[] getFloors(){
        return this.floors;
    }

}