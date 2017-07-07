package it.polimi.ingsw.lim.model;

import static it.polimi.ingsw.lim.Settings.*;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * This class represents the market. Slots and bonuses are mapped one-to-one
 */
public class Market implements Serializable {

    /**
     * Constructor, it creates a market of the proper size based on the number of players in the game
     */
    public Market(int playersNumber, Object[] marketBonuses) {
        //TODO: exception for wrong player number or wrong bonuses number?
        getLog().info("Creating market instance");
        int slotsNumber = 2;
        if(playersNumber <= MAX_USERS_PER_ROOM && playersNumber > 3) //do we have to check if there are more than 6 players?
            slotsNumber = playersNumber;
        this.slots = new FamilyMember[slotsNumber];
        this.bonuses = new Assets[slotsNumber];
        this.bonuses = marketBonuses;
    }

    public Market(){
    }

    /**
     * 
     */
    private FamilyMember[] slots;

    /**
     * 
     */
    private Object[] bonuses;

    public void addFamilyMember(FamilyMember fm, int position){
        //Positions are 1 to 5, in the array are 0 to 4
        int marketPos = position - 1;
        this.slots[marketPos] = fm;
    }

    public Object getBonuses(int position){
        int marketPos = position - 1;
        return this.bonuses[marketPos];
    }

    public Object[] getBonuses(){
        return bonuses;
    }

    public FamilyMember[] getSlots() {
        return slots;
    }

    public void setSlots(FamilyMember[] slots){
        this.slots = slots;
    }

    public void setBonuses(Object[] bonuses){
        this.bonuses = bonuses;
    }


    public boolean isPositionOccupied(int position){
        if(this.slots[position-1] != null)
            return true;
        return false;
    }

    public void clear(){
        getLog().info("Clearing market spaces");
        Arrays.stream(slots).forEach(slot -> slot = null);
    }


}