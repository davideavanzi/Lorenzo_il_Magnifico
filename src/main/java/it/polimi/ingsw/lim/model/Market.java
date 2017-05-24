package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents the market. Slots and bonuses are mapped one-to-one
 */
public class Market {

    /**
     * Constructor, it creates a market of the proper size based on the number of players in the game
     */
    public Market(int playersNumber) {
        int slotsNumber = 2;
        if(playersNumber < 6 && playersNumber > 3) //do we have to check if there are more than 6 players?
            slotsNumber = playersNumber;
        this.slots = new FamilyMember[slotsNumber];
        this.bonuses = new Assets[slotsNumber];
        //TODO: get assets bonuses from file
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
        position--;
        this.slots[position] = fm;
    }

    public Assets getBonus(int position){
        position--;
        return this.bonuses[position];
    }

    public boolean isPositionOccupied(int position){
        if(this.slots != null)
            return true;
        return false;
    }
}