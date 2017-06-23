package it.polimi.ingsw.lim.model;
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
    public Market(int playersNumber, Assets[] marketBonuses) {
        //TODO: exception for wrong player number or wrong bonuses number?
        getLog().info("Creating market instance");
        int slotsNumber = 2;
        if(playersNumber < 6 && playersNumber > 3) //do we have to check if there are more than 6 players?
            slotsNumber = playersNumber;
        this.slots = new FamilyMember[slotsNumber];
        this.bonuses = new Assets[slotsNumber];
        this.bonuses = marketBonuses;
    }

    /**
     * 
     */
    private FamilyMember[] slots;

    /**
     * 
     */
    private Assets[] bonuses;

    public void addFamilyMember(FamilyMember fm, int position){
        //Position are 1 to 5, in the array are 0 to 4
        int marketPos = position - 1;
        this.slots[marketPos] = fm;
    }

    public Assets getBonus(int position){
        int marketPos = position - 1;
        return this.bonuses[marketPos];
    }

    public boolean isPositionOccupied(int position){
        if(this.slots != null)
            return true;
        return false;
    }

    public void clear(){
        getLog().info("Clearing market spaces");
        Arrays.stream(slots).forEach(slot -> slot = null);
    }


}