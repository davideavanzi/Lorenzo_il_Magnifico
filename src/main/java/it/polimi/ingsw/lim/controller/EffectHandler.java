package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.model.*;

/**
 * Created by ava on 15/06/17.
 * This class is responsible of activating immediate effects.
 */
public class EffectHandler {

    public static void activateActionEffect(ActionEffect effect, Player pl) {

    }

    public static void activateAssetsEffect(AssetsEffect effect, Player pl) {
        pl.setResources(pl.getResources().add(effect.getBonus()));
    }

    public static void activateAssetsMultipliedEffect(AssetsMultipliedEffect effect, Player pl) {
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getResources().divide(effect.getMultiplier()))));
    }

    public static void activateCardMultipliedEffect(CardMultipliedEffect effect, Player pl) {
        pl.setResources(pl.getResources().add(
                effect.getBonus().multiply(pl.getCardsAmount(effect.getMultiplierColor()))));
    }

    public static void activateCouncilFavorsEffect(CouncilFavorsEffect effect, Player pl) {

    }

    public static void activateImmediateEffect(ImmediateEffect iEffect, Player pl) {
        if (iEffect instanceof ActionEffect) activateActionEffect((ActionEffect)iEffect, pl);
        if (iEffect instanceof AssetsEffect) activateAssetsEffect((AssetsEffect)iEffect, pl);
        if (iEffect instanceof AssetsMultipliedEffect) activateAssetsMultipliedEffect((AssetsMultipliedEffect)iEffect, pl);
        if (iEffect instanceof CardMultipliedEffect) activateCardMultipliedEffect((CardMultipliedEffect)iEffect, pl);
        if (iEffect instanceof CouncilFavorsEffect) activateCouncilFavorsEffect((CouncilFavorsEffect)iEffect, pl);
    }
}
