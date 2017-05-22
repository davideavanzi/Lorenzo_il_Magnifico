package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents family members. Players put them in places to make actions.
 * The neutral fm has a NEUTRAL color. it corresponds to a NEUTRAL dice which value is always 0.
 */
public class FamilyMember {

    /**
     * Default constructor
     */
    public FamilyMember(String diceColor) {
        this.diceColor = diceColor;
    }

    /**
     * 
     */
    private int ownerId;

    /**
     * 
     */
    private String diceColor;



}