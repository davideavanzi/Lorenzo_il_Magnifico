package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents a single floor of a tower
 */
public class Floor {

    /**
     * Default constructor
     */
    public Floor() {
    }

    /**
     * The bonus given when a player puts his family member in this floor
     */
    private Assets istantBonus;

    /**
     * This slot holds the card
     */
    private Card cardSlot;

    /**
     * The slot where is put the family member
     */
    private FamilyMember familyMemberSlot;

    /**
     * The strength required to perform this action (enter this floor)
     */
    private Strengths actionCost;

    /**
     * @return
     */
    public boolean isOccupied() {
        // TODO implement here
        return false;
    }

    /**
     * @param familyMember
     */
    public void setFamilyMemberSlot(FamilyMember familyMember) {
        this.familyMemberSlot = familyMember;
    }

    public void setCardSlot(Card card) {
        this.cardSlot = card;
    }

    public void setIstantBonus(Assets bonus) { this.istantBonus = bonus; }

}