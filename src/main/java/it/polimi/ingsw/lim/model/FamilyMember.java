package it.polimi.ingsw.lim.model;

import java.io.Serializable;

/**
 * This class represents family members. Players put them in places to make actions.
 * The neutral fm has a NEUTRAL color. it corresponds to a NEUTRAL dice which value is always 0.
 */
public class FamilyMember implements Serializable {

    /**
     * The color of the owner, as the real game
     */
    private String ownerColor;
    /**
     *
     */
    private String diceColor;

    public FamilyMember(){

    }

    /**
     * Constructor
     */
    public FamilyMember(String diceColor, String ownerColor) {
        this.diceColor = diceColor;
        this.ownerColor = ownerColor;
    }

    public String getOwnerColor(){
        return this.ownerColor;
    }

    public void setOwnerColor(String ownerColor){
        this.ownerColor = ownerColor;
    }

    public String getDiceColor(){
        return this.diceColor;
    }

    private void setDiceColor(String diceColor){
        this.diceColor = diceColor;
    }

    @Override
    public int hashCode() {
        int result = getOwnerColor().hashCode();
        result = 31 * result + getDiceColor().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FamilyMember)) return false;

        FamilyMember that = (FamilyMember) o;

        if (!getOwnerColor().equals(that.getOwnerColor())) return false;
        return getDiceColor().equals(that.getDiceColor());
    }
}