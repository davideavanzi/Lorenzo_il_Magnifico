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
    private Assets instantBonus;

    /**
     * This slot holds the card
     */
    private Card cardSlot;

    /**
     * The slot where is put the family member
     */
    private FamilyMember familyMemberSlot;

    /**
     * The strength required to perform this action (enter this floor).
     * This is not a "Strength" type because the tower corresponding to this specified action is implicit
     * as the floor is part of that tower.
     */
    private int actionCost;

    /**
     * @return
     */
    public boolean isOccupied() {
        if(this.familyMemberSlot != null)
            return true;
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

    public void setInstantBonus(Assets bonus) { this.instantBonus = bonus; }

    public Assets getInstantBonus() { return this.instantBonus; }

    public FamilyMember getFamilyMember(){
        return this.familyMemberSlot;
    }

    public Card getCard(){
        return this.cardSlot;
    }

    public int getActionCost() { return this.actionCost; }

}