package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.Assets;

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

    public String getBlockedCardColor(){
        return blockedCardColor;
    }

    public Assets getProductionCardCostMalus(){
        return productionCardCostMalus;
    }

    public Assets getOnAssetsMalus(int i){
        return onAssetsMalus[i];
    }

    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            Log.getLog().info("other = null");
            return false;
        }
        if(!(other instanceof EndGameExcommunication)){
            Log.getLog().info("other not a EndGameExcomm");
            return false;
        }
        EndGameExcommunication endGameExcommunication = (EndGameExcommunication) other;
        boolean equals = true;
        if(!(this.blockedCardColor.equals((endGameExcommunication.getBlockedCardColor())))){
            Log.getLog().info("blockedColorCard not equal");
            equals = false;
        }
        if(!(this.productionCardCostMalus.equals((endGameExcommunication.getProductionCardCostMalus())))){
            Log.getLog().info("productionCostMalus not equal");
            equals = false;
        }
        for(int i = 0; i < 2; i++){
            if(!(this.onAssetsMalus[i].equals((endGameExcommunication.getOnAssetsMalus(i))))){
                Log.getLog().info("onAssets not equal");
                equals = false;
            }
        }
        if(equals){
            Log.getLog().info("EndGameExcomm equals!");
        }
        return equals;
    }


}