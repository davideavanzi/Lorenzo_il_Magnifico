package it.polimi.ingsw.lim.model;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.lim.Log.getLog;

/**
 * This class represents the council. Players will put family members here. At the end of the turn, the gaming
 * order of the players is obtained by looking at the order of the family members in here. Otherwise it will be the
 * same as the turn before. If not all players put a family member here, the order starts with the ones that put
 * a fm here, then the others following in the order of the turn before.
 */
public class Council implements Serializable {

    /**
     * Constructor
     */
    public Council(int favorsAmount, Assets councilBonus) {
        getLog().info("Creating council istance");
        this.favorsAmount = favorsAmount;
        this.councilBonus = councilBonus;
        this.slots = new ArrayList<>();
    }

    /**
     * Slots for the family members
     */
    private ArrayList<FamilyMember> slots;

    /**
     * The amount of council favors given when a player puts a family member here
     */
    private int favorsAmount;

    /**
     * The bonus given when a player puts a family member here
     */

    private Assets councilBonus;

    public int getFavorsAmount(){
        return this.favorsAmount;
    }

    public ArrayList<FamilyMember> getFamilyMembers() { return this.slots; }

    public Assets getCouncilBonus(){
        return this.councilBonus;
    }

    /**
     * Add a new family member to the council
     * @param fm
     */
    public void addFamilyMember(FamilyMember fm){
        this.slots.add(fm);
    }

    public void clear(){
        getLog().info("Clearing Production space");
        this.slots.clear();
    }
}