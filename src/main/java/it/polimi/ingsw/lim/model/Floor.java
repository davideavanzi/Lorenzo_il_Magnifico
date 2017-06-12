package it.polimi.ingsw.lim.model;
import static it.polimi.ingsw.lim.Settings.FLOORS_ACTION_COSTS;

/**
 * This class represents a single floor of a tower
 */
public class Floor {

    /**
     * Constructor. it takes the integer corresponding to it's position in the tower
     * and picks it's action cost from the settings
     */
    public Floor(int floorPosition) {
        this.actionCost = FLOORS_ACTION_COSTS[floorPosition];
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

}