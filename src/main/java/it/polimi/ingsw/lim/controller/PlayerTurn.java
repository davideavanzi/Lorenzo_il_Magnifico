package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Player;

import static it.polimi.ingsw.lim.Settings.DEPLOYABLE_FM_PER_TURN;

/**
 * Created by ava on 16/06/17.
 * This class represent the single turn phase of a player .
 */
public class PlayerTurn {

    /**
     * The user playing the game
     */
    private User user;

    /**
     *
     */
    private int fmToDeploy = DEPLOYABLE_FM_PER_TURN;

    //Do we need them?
    private int bonusTowerAction = 0;
    private int bonusHarvestAction = 0;
    private int bonusProductionAction = 0;

    public String getUserName(){
        return this.user.getUsername();
    }

    public PlayerTurn(User user) {
       this.user = user;
    }
}
