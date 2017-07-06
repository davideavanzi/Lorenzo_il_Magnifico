package it.polimi.ingsw.lim.model.excommunications;

import it.polimi.ingsw.lim.Log;

/**
 * Created by ava on 06/07/17.
 */
public class EndGameCardsExcommunication extends Excommunication{

    private String blockedCardColor;

    public EndGameCardsExcommunication(){}

    public EndGameCardsExcommunication(String blockedCardColor) {
        this.blockedCardColor = blockedCardColor;
    }

    public String getBlockedCardColor() {
        return blockedCardColor;
    }

    public void setBlockedCardColor(String blockedCardColor) {
        this.blockedCardColor = blockedCardColor;
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
        if(!(other instanceof EndGameCardsExcommunication)){
            Log.getLog().info("other not a EndGameExcomm");
            return false;
        }
        EndGameCardsExcommunication endGameCardsExcommunication = (EndGameCardsExcommunication) other;
        boolean equals = true;
        if(!(this.blockedCardColor.equals((endGameCardsExcommunication.getBlockedCardColor())))){
            Log.getLog().info("blockedColorCard not equal");
            equals = false;
        }
        if(equals){
            Log.getLog().info("EndGameExcomm equals!");
        }
        return equals;
    }
}
