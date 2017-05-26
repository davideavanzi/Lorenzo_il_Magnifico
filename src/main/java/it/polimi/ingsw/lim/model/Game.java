package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.parser.Parser;

import java.util.*;
import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.Log.*;

/**
 * THE GAME INSTANCE
 * This class acts as a hub between all places of the game (model), it has links to others model classes and joins the
 * model with the main game controller.
 * It deals with all the low-level management (little logic) that isn't suitable for the main controller.
 */
public class Game {

    /**
     * Constructor. Sets starting age and turn. These go from 1 inclusive to their MAX value defined in the settings
     * TODO: should we leave them here?
     */
    public Game() {
        getLog().info("Creating game instance");
        this.age = 1;
        this.turn = 1;
        this.players = new ArrayList<>();
        this.towers = new HashMap<>();
        this.production = new ArrayList<>();
        this.harvest = new ArrayList<>();
        this.faithTrack = new Assets[FAITH_TRACK_LENGTH];
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
    private HashMap<Integer, Excommunication> Excommunications;

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
     * @return an excommunication based on the game's age
     */
    public Excommunication GetExcommunication(){
        return this.Excommunications.get(this.age);
    }

    /**
     * This method rolls dices
     */
    public void rollDices(){
        //For every dice, generates a random number between 1 and 6.
        Random randomGenerator = new Random();
        DICE_COLORS.forEach(color -> this.dice.put(color, randomGenerator.nextInt(5)+1));
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
        getLog().info("[GAME SETUP BEGIN]");
        //TODO: handle players creation in the controller
        //Creating towers with respective bonus.
        getLog().info("Creating towers with bonuses");
        DEFAULT_TOWERS_COLORS.forEach(color ->
                this.towers.put(color, new Tower(parsedGame.getTowerbonuses(color))));
        //Adding one more tower if there are 5 players
        if (playersNumber == 5){
            getLog().info("Creating fifth tower.");
            this.towers.put(BLACK_COLOR, new Tower(parsedGame.getTowerbonuses(BLACK_COLOR)));
        }
        //Create market
        getLog().info("Creating market with bonuses");
        this.market = new Market(playersNumber, parsedGame.getMarketBonuses());
        //Create council
        getLog().info("Creating council with bonuses");
        this.council = new Council(parsedGame.getCouncilFavors(), parsedGame.getCouncilBonus());
        getLog().info("Adding bonuses to faith track");
        //TODO: maybe better with a standard for cycle?
        int i = 0;
        for (Assets bonus : parsedGame.getFaithTrackbonuses())
            this.faithTrack[i] = bonus;

        //TODO: Get a random excommunication for every age
        //TODO: When we allot leadercards?
        /*
         * Distribute initial resources starting from the first player,
         * the following will have a coin more than the player before them.
         * TODO: where we decide the player order? at the beginning we consider their order of creation in the list.
         */
        getLog().info("Giving initial resources to players");
        int moreCoin = 0;
        //TODO: exception if creating a game without players?
        if (!players.isEmpty())
            for (Player pl : players){
                //TODO: check if this really works
                pl.setResources(pl.getResources().add(parsedGame.getStartingGameBonus()).addCoins(moreCoin));
                moreCoin++;
            }
        getLog().info("[GAME SETUP END]");
    }

    /**
     * This method cleans the board and sets up another turn (specified)
     */
    public void setUpTurn(){
        //Clean every structure
        cleanHarvest();
        cleanProduction();
        council.clear();
        market.clear();

        //Clean towers
        towers.keySet().forEach(color ->
            {towers.get(color).clear(); towers.get(color).addCards(cardsDeck.getCardsForTower(color, age));});
        //TODO: do we have to check if the arraylist of cards is the same length of the tower?

        //Distribute family members
        this.allotFamilyMembers();
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

    public int getAge() { return  this.age; }
    public int getTurn() { return  this.turn; }

    public void setAge(int age) { this.age = age; }
    public void setTurn(int turn) { this.turn = turn; }


}