package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This excommunication gives a malus to the player at the end of the game
 */
public class EndGameExcommunication extends Excommunication {

    /**
     * Default constructor
     */
    public EndGameExcommunication(String blockedCardColor, Assets productionCardCostMalus, Assets[] onAssetsMalus) {
        this.blockedCardColor = blockedCardColor;
        this.productionCardCostMalus = productionCardCostMalus;
        this.onAssetsMalus = new Assets[2];
        this.onAssetsMalus[0] = onAssetsMalus[0];
        this.onAssetsMalus[1] = onAssetsMalus[1];
    }

    /**
     * The player will not receive endgame victory points from the cards of this color
     */
    private String blockedCardColor;

    /**
     * Player will loose as many victory points as the result of the division of his YellowCards's cost and this asset.
     */
    private Assets productionCardCostMalus;

    /**
     * Player will loose as many assets in the first element of the array as the result of the division
     * between it's resources and the second assets element of this array
     */
    private Assets[] onAssetsMalus;



}