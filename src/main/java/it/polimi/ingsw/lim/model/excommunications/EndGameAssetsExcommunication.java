package it.polimi.ingsw.lim.model.excommunications;
import it.polimi.ingsw.lim.utils.Log;
import it.polimi.ingsw.lim.model.Assets;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * This excommunication gives a malus to the player at the end of the game
 */
public class EndGameAssetsExcommunication extends Excommunication {

    public EndGameAssetsExcommunication(){
        super();
    }

    /**
     * Default constructor
     */
    public EndGameAssetsExcommunication(Assets productionCardCostMalus, Assets[] onAssetsMalus) {
        this.productionCardCostMalus = productionCardCostMalus;
        this.onAssetsMalus = new Assets[2];
        this.onAssetsMalus[0] = onAssetsMalus[0];
        this.onAssetsMalus[1] = onAssetsMalus[1];
    }

    /**
     * Player will loose as many victory points as the result of the division of his YellowCards's cost and this asset.
     */
    private Assets productionCardCostMalus;

    /**
     * Player will loose as many assets in the first element of the array as the result of the division
     * between it's resources and the second assets element of this array
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = Assets[].class)
    private Assets[] onAssetsMalus;

    public Assets getProductionCardCostMalus(){
        return productionCardCostMalus;
    }

    @JsonIgnore
    public Assets getOnAssetsMalus(int i){
        return onAssetsMalus[i];
    }

    public void setProductionCardCostMalus (Assets productionCardCostMalus){
        this.productionCardCostMalus = productionCardCostMalus;
    }

    public void setOnAssetsMalus (Assets[] onAssetsMalus){
        this.onAssetsMalus = new Assets[2];
        this.onAssetsMalus = onAssetsMalus;
    }

    public Assets[] getOnAssetsMalus() {
        return onAssetsMalus;
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
        if(!(other instanceof EndGameAssetsExcommunication)){
            Log.getLog().info("other not a EndGameExcomm");
            return false;
        }
        EndGameAssetsExcommunication endGameAssetsExcommunication = (EndGameAssetsExcommunication) other;
        boolean equals = true;
        if(!(this.productionCardCostMalus.equals((endGameAssetsExcommunication.getProductionCardCostMalus())))){
            Log.getLog().info("productionCostMalus not equal");
            equals = false;
        }
        for(int i = 0; i < 2; i++){
            if(!(this.onAssetsMalus[i].equals((endGameAssetsExcommunication.getOnAssetsMalus(i))))){
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