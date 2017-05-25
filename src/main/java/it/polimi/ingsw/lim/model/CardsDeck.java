package it.polimi.ingsw.lim.model;

import java.util.*;
import static  it.polimi.ingsw.lim.Settings.*;

/**
 * This class acts as a container for all the cards still to be played. Will it contain the leader cards?
 * UPDATE: The game can be set up to have an arbitrary number of ages and round per age, so we have an arraylist
 * whose elements are hashmaps containing all developement cards of a specific age.
 */
public class CardsDeck {

    /**
     * Default constructor
     */
    public CardsDeck() {
    }

    /**
     * The development cards
     */
    private ArrayList<HashMap<String,ArrayList<Card>>> developementCards;

    /**
     *
     */
    private LeaderCard leaderCardsDeck;

    /**
     * This method gets an arraylist of cards to put into a tower
     * @param color the color of the cards to pick
     * @param age the age of the cards to pick
     * @return an arraylist of cards that will go into the tower
     * TODO: check integrity
     */
    public ArrayList <Card> getCardsForTower(String color, int age) {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < TOWER_HEIGHT; i++){
            //Generate a random number and pick that card from the arraylist
            Random randomGenerator = new Random();
            //Age starts from 1, the arraylist from 0
            //TODO: Check if this really works :S
            cards.add(developementCards.get(age+1).get(color).get(randomGenerator.nextInt(developementCards.get(age+1).get(color).size())));
        }
        return cards;
    }
}