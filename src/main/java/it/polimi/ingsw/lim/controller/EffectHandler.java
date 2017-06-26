package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.Log;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.model.*;

import java.util.logging.Level;

/**
 * Created by ava on 15/06/17.
 * This class is responsible of activating immediate effects.
 */
public class EffectHandler {

    private static void activateActionEffect(ActionEffect effect, User recipient) {

    }

    private static void activateAssetsEffect(AssetsEffect effect, User recipient) {
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(effect.getBonus()));
    }

    private static void activateAssetsMultipliedEffect(AssetsMultipliedEffect effect, User recipient) {
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getResources().divide(effect.getMultiplier()))));
    }

    private static void activateCardMultipliedEffect(CardMultipliedEffect effect, User recipient) {
        Player pl = recipient.getPlayer();
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getCardsAmount(effect.getMultiplierColor()))));
    }

    private static void activateCouncilFavorsEffect(CouncilFavorsEffect effect, User recipient) {
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
