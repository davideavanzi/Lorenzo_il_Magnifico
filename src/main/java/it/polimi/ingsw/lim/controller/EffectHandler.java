package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by ava on 15/06/17.
 * This class is responsible of activating immediate effects.
 */
public class EffectHandler {

    /**
     * This method activates an effect that gives the user the ability to perform another action.
     * it requires correct data, it is up to the effect constructor to check parsed data validity.
     * @param effect
     * @param recipient
     */
    private static void activateActionEffect(ActionEffect effect, User recipient) {
        recipient.gameMessage("You're about to activate an immediate Action effect from the card you picked.");
        if (effect.getStrength().getHarvestBonus() > 0) {
            //Perform fast harvest action
            recipient.getRoom().getGameController()
                    .beginFastHarvest(effect.getStrength(),recipient);
        } else if (effect.getStrength().getProductionBonus() > 0) {
            recipient.getRoom().getGameController()
                    .beginFastProduction(effect.getStrength(),recipient);
        } else {
            /* Parse possible target towers and send them to the gameController */
            HashMap<String, Integer> targetTowers = new HashMap<>();
            recipient.getRoom().getGameController()
                    .beginFastTowerMove(effect.getStrength(), effect.getDiscount(), recipient);
        }
    }

    private static void activateAssetsEffect(AssetsEffect effect, User recipient) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked.");
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(effect.getBonus()));
    }

    private static void activateAssetsMultipliedEffect(AssetsMultipliedEffect effect, User recipient) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked," +
                "multiplied by your resources.");
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getResources().divide(effect.getMultiplier()))));
    }

    private static void activateCardMultipliedEffect(CardMultipliedEffect effect, User recipient) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked," +
                "multiplied by your amount of "+effect.getMultiplierColor()+" cards.");
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getCardsAmount(effect.getMultiplierColor()))));
    }

    private static void activateCouncilFavorsEffect(CouncilFavorsEffect effect, User recipient) {
        recipient.gameMessage("You're about to activate "+effect.getAmount()+" council favors provided to you " +
                "by the card you picked.");
        try {
            CFavorsHandler.giveFavors(recipient, effect.getAmount());
        } catch (ControllerException e) {
            Log.getLog().log(Level.SEVERE, "Controller error. Providing more favors than possible.");
        }

    }

    public static void activateImmediateEffect(ImmediateEffect iEffect, User recipient) {
        if (iEffect instanceof ActionEffect) activateActionEffect((ActionEffect)iEffect, recipient);
        if (iEffect instanceof AssetsEffect) activateAssetsEffect((AssetsEffect)iEffect, recipient);
        if (iEffect instanceof AssetsMultipliedEffect) activateAssetsMultipliedEffect((AssetsMultipliedEffect)iEffect, recipient);
        if (iEffect instanceof CardMultipliedEffect) activateCardMultipliedEffect((CardMultipliedEffect)iEffect, recipient);
        if (iEffect instanceof CouncilFavorsEffect) activateCouncilFavorsEffect((CouncilFavorsEffect)iEffect, recipient);
    }
}
