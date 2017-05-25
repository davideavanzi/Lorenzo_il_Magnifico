package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.parser.Parser;

import java.util.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * THE GAME INSTANCE
 * This class acts as a hub between all places of the game (model), it has links to others model classes and joins the
 * model with the controller. It has al the low-level management that isn't suitable for the main controller.
 */
public class Game {

    /**
     * Constructor. Sets starting age and turn. These go from 1 inclusive to their MAX value defined in the settings
     */
    public Game() {
        this.age = 1;
        this.turn = 1;
    }

    /**
     * The age in which the game is.
     */
    private int age;

    /**
     * The turn in which the game is.
     */

    private int turn;

    /**
     * List of all the players
     */
    private ArrayList<Player> players;

    /**
     * This maps the three excommunication with an int representing it's age
     */
    private HashMap<Integer, Excommunication> Excomunications;

    /**
     * This list holds slots for the production site.
     */
    private ArrayList<FamilyMember> production;

    /**
     * This list holds slots for the harvest site
     */
    private ArrayList<FamilyMember> harvest;

    /**
     * This is the council.
     */
    private Council council;

    /**
     * The towers, mapped by color with a string
     * GREEN, YELLOW, BLUE, PURPLE, BLACK
     */
    private HashMap<String, Tower> towers;

    /**
     * The faith track is an array of 30 bonuses, specified with the Assets type
     */
    private Assets[] faithTrack;

    /**
     * Link to the market
     */
    private Market market;

    /**
     * Link to the cards container
     */
    private CardsDeck cardsDeck;

    /**
     * The three dices, mapped by color: BLACK, WHITE, ORANGE, NEUTRAL (always 0)
     */
    private HashMap<String, Integer> dice;

    // ############################################################# METHODS AHEAD

    /**
     * @return an excommunication based on it's
     * @param age
     */
    public Excommunication GetExcommunication(int age){
        return this.Excomunications.get(age);
    }

    /**
     * This method rolls dices
     */
    public void rollDices(){
        //For every dice, generates a random number between 1 and 6.
        Random randomGenerator = new Random();
        this.dice.put("BLACK", randomGenerator.nextInt(5)+1);
        this.dice.put("ORANGE", randomGenerator.nextInt(5)+1);
        this.dice.put("WHITE", randomGenerator.nextInt(5)+1);
    }

    /**
     * This method distributes four family member to any player, one per kind
     */
    private void allotFamilyMembers(){
        //Give all family members to the players
        this.players.forEach(player ->
                DICE_COLORS.forEach(color ->
                        player.addFamilyMember((new FamilyMember(color, player.getColor())))));
    }

    /**
     * This method sets up the game after it is created by the constructor.
     */
    public void setUpGame(int playersNumber, Parser parsedGame){
        //TODO: handle players creation in the controller
        //Creating towers with respective bonus.
        DEFAULT_TOWERS_COLORS.forEach(color -> this.towers.put(color, new Tower(color, parsedGame.getTowerbonuses(color))));
        //Adding one more tower if there are 5 players
        if (playersNumber == 5){
            this.towers.put(BLACK_COLOR, new Tower(BLACK_COLOR, parsedGame.getTowerbonuses(BLACK_COLOR)));
        }
        //Create market
        this.market = new Market(playersNumber, parsedGame.getMarketBonuses());
        //Create council
        this.council = new Council(parsedGame.getCouncilFavors(), parsedGame.getCouncilBonus());
        //Do we have to create a faith track or we just check the players's amount of FP?
        //TODO: Get a random excommunication for every age
        //TODO: When we allot leadercards?
        /*
         * Distribute initial resources starting from the first player,
         * the following will have a coin more than the player before them.
         * TODO: where we decide the player order? at the beginning we consider their order of creation in the list.
         */
        int moreCoin = 0;
        for (Player pl : players){
            //TODO: check if this really works
            pl.setResources(pl.getResources().add(parsedGame.getStartingGameBonus()).addCoins(moreCoin));
            moreCoin++;
        }
    }

    /**
     * This method cleans the board and sets up another turn (specified)
     */
    public void setUpTurn(int turn){
        //Clean every structure
        this.cleanHarvest();
        this.cleanProduction();
        this.cleanCouncil();
        //Clean towers
        //TODO:Do we really need to clean the towers or we can simply overwrite them?
        towers.keySet().forEach(color -> {towers.get(color).clean(); towers.get(color).addCards(cardsDeck.getCardsForTower(color, age));});
        //TODO: do we have to check if the arraylist of cards is the same length of the tower?

        //Distribute family members
        allotFamilyMembers();
    }

    /**
     * This method adds a player to the game.
     * TODO: do we have to check again if there are more than 5?
     */
    public void addPlayer(Player pl){
        this.players.add(pl);
    }

    private void cleanHarvest(){
        //TODO: implement
    }

    private void cleanProduction(){
        //TODO: implement
    }

    private void cleanCouncil(){
        //TODO: implement
    }

    public int getAge() { return  this.age; }
    public int getTurn() { return  this.turn; }



}