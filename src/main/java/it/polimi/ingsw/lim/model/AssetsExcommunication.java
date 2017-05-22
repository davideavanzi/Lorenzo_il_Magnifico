package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 *  This kinds of excommunication subtracts the specified amount of resourced every time a player gets a bonus.
 */
public class AssetsExcommunication extends Excommunication {

    /**
     * Constructor
     */
    public AssetsExcommunication(Assets malus) {
        super();
        this.malus = malus;
    }

    /**
     * The malus
     */
    private Assets malus;

    public Assets getMalus(){
        return this.malus;
    }

}