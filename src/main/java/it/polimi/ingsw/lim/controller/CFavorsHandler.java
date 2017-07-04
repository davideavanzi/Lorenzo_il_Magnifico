package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ava on 26/06/17.
 */
public class CFavorsHandler {

    /**
     * This list holds all possible favors obtainable from the council
     */
    private static final ArrayList<Assets> possibleFavors = new ArrayList<Assets>(Arrays.asList(
            new Assets(0,1,1,0,0,0,0),
            new Assets(0,0,0,0,2,0,0),
            new Assets(2,0,0,0,0,0,0),
            new Assets(0,0,0,0,0,2,0),
            new Assets(0,0,0,0,1,0,0)));

    /**
     * This method activates a certain amount of different favors from the council.
     * Gives the user the list to pick one from, until the list is empty.
     * We assume that the input provided from the client is correct,
     * it will be up to the client handler to provide valid data from the user.
     * @param recipient the user receiving favors
     * @param amount the amount of favors received. They can't be of the same kind.
     */
    public static void giveFavors(User recipient, int amount) throws ControllerException {
        if (amount < possibleFavors.size()) throw new ControllerException("Can't give more favors than favors size!");
        List<Assets> tmpFavors = new ArrayList<>(possibleFavors);
        for (int i = amount; i < 0; i--) {
            recipient.chooseFavor(tmpFavors);
            //activateFavor(recipient.getPlayer(), tmpFavors.remove(choice)); //TODO: !
        }
    }

    private static void activateFavor(Player pl, Assets bonus) {
        pl.setResources(pl.getResources().add(bonus));
    }
}


