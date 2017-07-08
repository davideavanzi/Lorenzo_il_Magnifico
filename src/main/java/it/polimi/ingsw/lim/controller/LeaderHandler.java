package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.model.leaders.ActivableLeader;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import it.polimi.ingsw.lim.model.leaders.PermanentLeader;

import java.util.ArrayList;

import static it.polimi.ingsw.lim.Settings.*;

/**
 * Created by ava on 08/07/17.
 */
public class LeaderHandler {

    public static void activatePermanentLeader(int id, User actor) {
        PermanentLeader leader = (PermanentLeader)actor.getPlayer().getLeaderById(id);
        if (id == 4)
            actor.getPlayer().getStrengths().getDiceBonus().replace(NEUTRAL_COLOR, 3);
        else if (id == 11)
            DICE_COLORS.forEach(color -> actor.getPlayer().getStrengths().getDiceBonus()
                    .replace(color, actor.getPlayer().getStrengths().getDiceBonus().get(color)+2));
        else if (id == 13) {
            ArrayList<String> availableChoices = actor.getRoom().getGameController().getGame().getAllDeployedLeaders();
            actor.chooseLeaderToCopy(availableChoices);
            actor.getRoom().getGameController().setPendingLeaderCopy(availableChoices);
        } else if (id == 10) {
            DICE_COLORS.forEach(color -> actor.getPlayer().getDiceOverride().put(color, 5));
        }
    }

    public static void activateActivableLeader(int id, User actor) {
        ActivableLeader leader = (ActivableLeader)actor.getPlayer().getLeaderById(id);

        if(id == 12) actor.askFmToBoost();

        leader.setActivated(true);
        EffectHandler.activateImmediateEffect(leader.getEffect(), actor);
    }

}
