package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents the council. Players will put family members here. At the end of the turn, the gaming
 * order of the players is obtained by looking at the order of the family members in here. Otherwise it will be the
 * same as the turn before. If not all players put a family member here, the order starts with the ones that put
 * a fm here, then the others following in the order of the turn before.
 */
public class Council {

    /**
     * Constructor
     */
    public Council(int favorsAmount, Assets councilBonus) {
        this.favorsAmount = favorsAmount;
        this.councilBonus = councilBonus;
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

    public Assets getCouncilBonus(){
        return this.councilBonus;
    }

    public void addFamilyMember(FamilyMember fm){
        this.slots.add(fm);
    }

}