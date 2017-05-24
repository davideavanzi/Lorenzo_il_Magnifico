package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents family members. Players put them in places to make actions.
 * The neutral fm has a NEUTRAL color. it corresponds to a NEUTRAL dice which value is always 0.
 */
public class FamilyMember {

    /**
     * Constructor
     */
    public FamilyMember(String diceColor, String ownerColor) {
        this.diceColor = diceColor;
        this.ownerColor = ownerColor;
    }

    /**
     * The color of the owner, as the real game
     */
    private String ownerColor;

    /**
     * 
     */
    private String diceColor;

    public String getOwnerColor(){
        return this.ownerColor;
    }
    public String getDiceColor(){
        return this.diceColor;
    }

}