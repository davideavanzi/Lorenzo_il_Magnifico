package it.polimi.ingsw.lim.model.immediateEffects;
import it.polimi.ingsw.lim.utils.Log;

/**
 * This effect gives the player a specified amount of council favors.
 */
public class CouncilFavorsEffect extends ImmediateEffect {

    /**
     * Constructor
     */
    public CouncilFavorsEffect(int amount) {
        this.amount = amount;
    }

    public CouncilFavorsEffect(){
        super();
    }

    /**
     * The amount of council favors.
     */
    private int amount;

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    /**
     * the task of this method is to compare if two CouncilFavoursEffect are equal and return true if they are
     * equals false otherwise.
     * @param other is one of the two CouncilFavoursEffect to be compared
     * @return true if the CouncilFavoursEffect are equal, false otherwise
     */
    @Override
    public boolean equals (Object other){
        if(other == this){
            return true;
        }
        if (other == null){
            return false;
        }
        if(!(other instanceof CouncilFavorsEffect)){
            return false;
        }
        boolean equals = true;
        CouncilFavorsEffect councilFavorsEffect = (CouncilFavorsEffect) other;
        if(!(this.amount == councilFavorsEffect.getAmount())){
            equals = false;
            Log.getLog().info("Different council favour effect");
        }
        return equals;
    }
}