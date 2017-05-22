package it.polimi.ingsw.lim.model;
import java.util.*;

/**
 * This class acts as a container for all the cards still to be played. It also contains all the leader cards
 */
public class CardsDeck {

    /**
     * Default constructor
     */
    public CardsDeck() {
    }

    /**
     * 
     */
    private HashMap<String,ArrayList<Card>> firstAgeCards;

    /**
     * 
     */
    private HashMap<String,ArrayList<Card>> secondAgeCards;

    /**
     * 
     */
    private HashMap<String,ArrayList<Card>> thirdAgeCards;

    /**
     *
     */
    private LeaderCard leaderCardsDeck;


}