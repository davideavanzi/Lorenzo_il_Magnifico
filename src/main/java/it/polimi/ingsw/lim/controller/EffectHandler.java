package it.polimi.ingsw.lim.controller;


import it.polimi.ingsw.lim.model.Game;
import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.model.immediateEffects.*;

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
            recipient.getRoom().getGameController()
                    .beginFastHarvest(effect.getStrength(),recipient);
        } else if (effect.getStrength().getProductionBonus() > 0) {
            recipient.getRoom().getGameController()
                    .beginFastProduction(effect.getStrength(),recipient);
        } else {
            recipient.getRoom().getGameController()
                    .beginFastTowerMove(effect.getStrength(), effect.getDiscount(), recipient);
        }
    }

    private static void activateAssetsEffect(AssetsEffect effect, User recipient, Game game) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked.");
        Player pl = recipient.getPlayer();
        game.giveAssetsToPlayer(effect.getBonus(), pl);
        if (game.playerHasActiveLeader(16, pl))
            game.giveAssetsToPlayer(effect.getBonus(), pl);
    }

    private static void activateAssetsMultipliedEffect(AssetsMultipliedEffect effect, User recipient, Game game) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked," +
                "multiplied by your resources.");
        Player pl = recipient.getPlayer();
        game.giveAssetsToPlayer(effect.getBonus().multiply(pl.getResources().divide(effect.getMultiplier())),pl);
        if (game.playerHasActiveLeader(16, pl))
            game.giveAssetsToPlayer(effect.getBonus().multiply(pl.getResources().divide(effect.getMultiplier())),pl);
    }

    private static void activateCardMultipliedEffect(CardMultipliedEffect effect, User recipient, Game game) {
        recipient.gameMessage("An immediate assets bonus is being provided to you from the card you picked," +
                "multiplied by your amount of "+effect.getMultiplierColor()+" cards.");
        Player pl = recipient.getPlayer();
        game.giveAssetsToPlayer(effect.getBonus().multiply(pl.getCardsAmount(effect.getMultiplierColor())),pl);
        if (game.playerHasActiveLeader(16, pl))
            game.giveAssetsToPlayer(effect.getBonus().multiply(pl.getCardsAmount(effect.getMultiplierColor())),pl);

    }

    private static void activateCouncilFavorsEffect(CouncilFavorsEffect effect, User recipient) {
        recipient.gameMessage("You're about to activate "+effect.getAmount()+" council favors provided to you " +
                "by the card you picked.");
        recipient.askForCouncilFavor(effect.getAmount());
    }

    public static void activateImmediateEffect(ImmediateEffect iEffect, User recipient) {
        Game game = recipient.getRoom().getGameController().getGame();
        if (iEffect instanceof ActionEffect) activateActionEffect((ActionEffect)iEffect, recipient);
        if (iEffect instanceof AssetsEffect) activateAssetsEffect((AssetsEffect)iEffect, recipient, game);
        if (iEffect instanceof AssetsMultipliedEffect) activateAssetsMultipliedEffect((AssetsMultipliedEffect)iEffect, recipient, game);
        if (iEffect instanceof CardMultipliedEffect) activateCardMultipliedEffect((CardMultipliedEffect)iEffect, recipient, game);
        if (iEffect instanceof CouncilFavorsEffect) activateCouncilFavorsEffect((CouncilFavorsEffect)iEffect, recipient);
    }
}
