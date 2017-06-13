package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.GreenCard;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.YellowCard;

import java.util.ArrayList;

/**
 * Created by ava on 13/06/17.
 * This class manages cards activations.
 */
public class CardController {

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

}
