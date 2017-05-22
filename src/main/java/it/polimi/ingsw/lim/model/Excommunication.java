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
    }

    /**
     * The age of the excommunication
     */
    private int age;

    /**
     * the id of the excommunication. Is this really useful?
     */
    private int idExcommunication;

    /**
     * Excommunicated players will be stored here.
     */
    private ArrayList<Player> excommunicated;

}