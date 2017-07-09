package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.model.cards.Card;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

import static it.polimi.ingsw.lim.Settings.FLOORS_ACTION_COSTS;

/**
 * This class represents a single floor of a tower
 */
public class Floor implements Serializable {

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
     * Constructor. it takes the integer corresponding to it's position in the tower
     * and picks it's action cost from the settings
     */
    public Floor(int floorPosition) {
        this.actionCost = FLOORS_ACTION_COSTS[floorPosition];
    }

    public Floor (){

    }

    /**
     * @return
     */
    @JsonIgnore
    public boolean isOccupied() {
        if(this.familyMemberSlot != null)
            return true;
        return false;
    }

    public Assets getInstantBonus() { return this.instantBonus; }

    public void setInstantBonus(Assets instantBonus) { this.instantBonus = instantBonus; }

    public Card getCardSlot(){
        return this.cardSlot;
    }

    public void setCardSlot(Card cardSlot) {
        this.cardSlot = cardSlot;
    }

    public boolean hasCard() { return (this.cardSlot != null); }

    /**
     * Get the card and set the slot to null
     * @return
     */
    public Card pullCard() {
        Card card = this.cardSlot;
        this.cardSlot = null;
        return card;
    }

    public int getActionCost() { return this.actionCost; }

    public void setActionCost(int actionCost){
        this.actionCost = actionCost;
    }

    public FamilyMember getFamilyMemberSlot() { return  this.familyMemberSlot; }

    /**
     * @param familyMemberSlot
     */
    public void setFamilyMemberSlot(FamilyMember familyMemberSlot) {
        this.familyMemberSlot = familyMemberSlot;
    }

    @Override
    public int hashCode() {
        int result = getInstantBonus() != null ? getInstantBonus().hashCode() : 0;
        result = 31 * result + getCardSlot().hashCode();
        result = 31 * result + (getFamilyMemberSlot() != null ? getFamilyMemberSlot().hashCode() : 0);
        result = 31 * result + getActionCost();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Floor)) return false;

        Floor floor = (Floor) o;

        if (getActionCost() != floor.getActionCost()) return false;
        if (getInstantBonus() != null ? !getInstantBonus().equals(floor.getInstantBonus()) : floor.getInstantBonus() != null)
            return false;
        if (!getCardSlot().equals(floor.getCardSlot())) return false;
        return getFamilyMemberSlot() != null ? getFamilyMemberSlot().equals(floor.getFamilyMemberSlot()) : floor.getFamilyMemberSlot() == null;
    }
}