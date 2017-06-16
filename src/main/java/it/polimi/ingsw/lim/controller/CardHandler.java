package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.model.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
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
     * @param pl the player performing the action
     */
    public static void activateYellowCard (YellowCard card, Player pl) {
        Log.getLog().log(Level.INFO, () -> "activating production card "+card.getName()+" for player "+pl.getNickname());
        ArrayList<Assets> productionResults = card.getProductionResults();
        if (card.getCardMultiplier() != null) {
            pl.setResources(pl.getResources().add(productionResults.get(0)
                    .multiply(pl.getCardsAmount(card.getCardMultiplier()))));
        } else if (card.getProductionCosts().size() == 0) {
            pl.setResources(pl.getResources().add(card.getProductionResults().get(0)));
        } else {
            ArrayList<Assets[]> availableOptions = new ArrayList<>();
            for (int i = 0; i < card.getProductionCosts().size(); i++)
                if (pl.getResources().isGreaterOrEqual(card.getProductionCosts().get(i))) {
                    availableOptions.add(new Assets[]{card.getProductionCosts().get(i),
                            card.getProductionResults().get(i)});
                }
            if (availableOptions.size() > 0) {
                int option = 0;
                if (availableOptions.size() > 1) {
                    //TODO: ask the player which production prefers to activate!
                }
                pl.setResources(pl.getResources().subtract(availableOptions.get(option)[1])
                        .add(availableOptions.get(option)[2]));
            }
            /* pretty but useless :(
            ArrayList<Assets> productionCosts = card.getProductionCosts().stream()
                    .filter(cost -> pl.getResources().isGreaterOrEqual(cost))
                    .collect(Collectors.toCollection(ArrayList::new));
            */

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

    /**
     * TODO: do we have to set an else case? - USELESS
     * @param card
     * @param pl
     */
    /*
    public static void activateCard (Card card, Player pl) {
        if (card instanceof GreenCard) activateGreenCard((GreenCard)card, pl);
        if (card instanceof BlueCard) activateBlueCard((BlueCard)card, pl);
        if (card instanceof YellowCard) activateYellowCard((YellowCard) card, pl);
    } */
}
