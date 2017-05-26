package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * Abstract class that represents an excommunication. It has always an age, an id and an array that contains
 * the excommunicated players
 */
public abstract class Excommunication {

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
     * Excommunicated players will be stored here.
     */
    private ArrayList<Player> excommunicated;

}