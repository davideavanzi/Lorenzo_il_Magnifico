package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class represents all leaders that have an effect activated once per turn
 */
public class ActivableLeader extends LeaderCard {

    /**
     * Default constructor
     */
    public ActivableLeader() {
    }

    /**
     * All leaders of this kind have an effect that can be modelled with an immediate effect
     */
    private ImmediateEffect effect;

    /**
     * This List of integers is updated everytime the player activates this card. The integer represents the number
     * of the game turn. This is useful for checking whether a card has been played in one turn or not.
     */
    private ArrayList<Integer> roundsActivated;


}