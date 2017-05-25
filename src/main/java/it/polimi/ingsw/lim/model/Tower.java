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

}