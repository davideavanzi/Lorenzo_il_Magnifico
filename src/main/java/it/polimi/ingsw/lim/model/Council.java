package it.polimi.ingsw.lim.model;

import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * This class represents the council. Players will put family members here. At the end of the turn, the gaming
 * order of the players is obtained by looking at the order of the family members in here. Otherwise it will be the
 * same as the turn before. If not all players put a family member here, the order starts with the ones that have put
 * a fm here, then the others following in the order of the turn before.
 */
public class Council implements Serializable {

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
    /**
     * here are stored all the options a player can choose from when he receives a favor.
     */
    private ArrayList<Assets> favorBonuses;

    /**
     * Constructor
     */
    public Council(){
        this.slots = new ArrayList<>();
    }

    public Council(int favorsAmount, Assets councilBonus) {
        getLog().info("Creating council istance");
        this.favorsAmount = favorsAmount;
        this.councilBonus = councilBonus;
        this.slots = new ArrayList<>();

    }

    public void setSlots(ArrayList<FamilyMember> slots){
        this.slots = slots;
    }

    public int getFavorsAmount(){
        return this.favorsAmount;
    }

    public void setFavorsAmount (int favorsAmount){
        this.favorsAmount = favorsAmount;
    }

    public ArrayList<FamilyMember> getFamilyMembers() { return this.slots; }

    public Assets getCouncilBonus(){
        return this.councilBonus;
    }

    private void setCouncilBonus (Assets councilBonus){
        this.councilBonus = councilBonus;
    }

    /**
     * Add a new family member to the council
     * @param fm
     */
    public void addFamilyMember(FamilyMember fm) {
        this.slots.add(fm);
    }

    public void clear(){
        getLog().info("Clearing Production space");
        this.slots.clear();
    }

    public ArrayList<Assets> getFavorBonuses() {
        return favorBonuses;
    }

    public void setFavorBonuses(ArrayList<Assets> favorBonuses) {
        this.favorBonuses = favorBonuses;
    }

    @Override
    public int hashCode() {
        int result = slots.hashCode();
        result = 31 * result + getFavorsAmount();
        result = 31 * result + getCouncilBonus().hashCode();
        result = 31 * result + getFavorBonuses().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Council)) return false;

        Council council = (Council) o;

        if (getFavorsAmount() != council.getFavorsAmount()) return false;
        if (!slots.equals(council.slots)) return false;
        if (!getCouncilBonus().equals(council.getCouncilBonus())) return false;
        return getFavorBonuses().equals(council.getFavorBonuses());
    }
}