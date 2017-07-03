package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.BlueCard;
import it.polimi.ingsw.lim.model.cards.GreenCard;
import it.polimi.ingsw.lim.model.cards.YellowCard;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by ava on 13/06/17.
 * This class manages cards activations.
 */
public class CardHandler {

    public static void activateGreenCard (GreenCard card, Player pl) {
        pl.getResources().add(card.getHarvestResult());
    }

    /**
     * This method activates a yellow card, checking the type of bonus.
     * productionResults must not be null in any case (TODO: check it in yellow card constructor!)
     * @param card the card to activate
     * @param actor the user performing the action
     */
    public static void activateYellowCard (YellowCard card, User actor, GameController controllerCallback) {
        Log.getLog().log(Level.INFO, () ->
                "activating production card "+card.getName()+" for player "+actor.getPlayer().getNickname());
        if (card.getCardMultiplier() != null) {
            controllerCallback.addBonusToAccumulator(card.getProductionResults().get(0)
                    .multiply(actor.getPlayer().getCardsAmount(card.getCardMultiplier())));
        } else if (card.getProductionCosts().size() == 0) {
            controllerCallback.addBonusToAccumulator(card.getProductionResults().get(0));
        } else {
            ArrayList<Assets[]> availableOptions = new ArrayList<>();
            for (int i = 0; i < card.getProductionCosts().size(); i++)
                if (actor.getPlayer().getResources().isGreaterOrEqual(card.getProductionCosts().get(i))) {
                    availableOptions.add(new Assets[]{card.getProductionCosts().get(i),
                            card.getProductionResults().get(i)});
                }

            if (availableOptions.size() > 0) {
                controllerCallback.addProductionOptions(availableOptions);
                /*
                int chosenOption = actor.chooseProduction(availableOptions);
                actor.getPlayer().setResources(actor.getPlayer().getResources()
                        .subtract(availableOptions.get(chosenOption)[1]));
                controllerCallback.add(availableOptions.get(chosenOption)[2]); */
            }
        }
    }

    /**
     * activate a blue card (when picked). Gives the player the permanent bonuses given by the card.
     * Values in the player HashMap are updated only if present in the card's one.
     * @param card
     * @param pl
     */
    public static void activateBlueCard (BlueCard card, Player pl) {
        Log.getLog().log(Level.INFO, () -> "activating blue card "+card.getName()+" for player "+pl.getNickname());
        if (card.getTowerBonusAllowed() == true) {
            pl.notTowerBonusAllowed();
        } else {
            if (card.getPermanentBonus() != null)
                pl.setStrengths(pl.getStrengths().add(card.getPermanentBonus()));
            if (card.getPickDiscounts() != null)
                card.getPickDiscounts().keySet().forEach(color ->
                        pl.setPickDiscount(color, pl.getPickDiscount(color).add(card.getPickDiscounts().get(color))));
        }
    }
}
