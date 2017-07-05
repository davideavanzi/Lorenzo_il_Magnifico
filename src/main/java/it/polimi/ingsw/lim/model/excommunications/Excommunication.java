package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.model.Player;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract class that represents an excommunication. It has always an age, an id and an array that contains
 * the excommunicated players
 */
public abstract class Excommunication implements Serializable {

    /**
     * Default constructor
     */
    public Excommunication() {
        this.excommunicated = new ArrayList<>();
    }

    /**
     * The age of the excommunication. TODO: DELET DIS  .. ?
     */
    private int age;


    /**
     * Excommunicated players will be stored here by their color.
     */
    private ArrayList<String> excommunicated;

    public int getAge() {
        return age;
    }

    public ArrayList<String> getExcommunicated() {
        return excommunicated;
    }

    public void addExcommunicated(Player pl) {
        excommunicated.add(pl.getColor());
    }
}