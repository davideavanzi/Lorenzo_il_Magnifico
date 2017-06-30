package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.model.cards.Card;

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
        this.developementCards = new HashMap<>();
    }

    /**
     * The development cards. Indexed first by Age, then by color
     */
    private HashMap<Integer, HashMap<String,ArrayList<Card>>> developementCards;

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
    public ArrayList <Card> pullCardsForTower(String color, int age) {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < TOWER_HEIGHT; i++){
            Random randomGenerator = new Random();
            cards.add(developementCards.get(age).get(color).remove(randomGenerator.nextInt(developementCards.get(age).get(color).size())));
        }
        return cards;
    }

    public void addDevelopementCardsOfAge(int age, HashMap<String, ArrayList<Card>> cards){
        this.developementCards.put(age, cards);
    }
}