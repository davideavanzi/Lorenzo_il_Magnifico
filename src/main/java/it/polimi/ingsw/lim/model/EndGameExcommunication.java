package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This excommunication gives a malus to the player at the end of the game
 */
public class EndGameExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public EndGameExcommunication() {
    }

    /**
     * The player will not receive endgame victory points from the cards of this color
     */
    private String blockedCardColor;

    /**
     * Player will loose as many victory points as the result of the division of his YellowCards's cost.
     */
    private Assets productionCardCostMalus;

    /**
     * 
     */
    private Assets onAssetsMalus;



}