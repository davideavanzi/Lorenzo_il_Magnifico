package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
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
     * This method sets up the game after it is created by the constructor.
     */
    public void setUpGame(Parser parsedGame) throws GameSetupException {
        getLog().info("[GAME SETUP BEGIN]");
        int playersNumber = this.players.size();
        if (playersNumber < 2 || playersNumber > 5)
            throw new GameSetupException("Wrong player number on game setup");

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
        getLog().info("Giving initial resources to " +playersNumber+" players");
        int moreCoin = 0;
        for (Player pl : players)
            pl.setResources(pl.getResources().add(parsedGame.getStartingGameBonus()).addCoins(moreCoin));
            moreCoin++;

        getLog().info("[GAME SETUP END]");
    }

    /**
     * This method cleans the board and sets up another turn (reading it from it's state)
     * Deciding when to advance in ages and turn is a task of the main game controller.
     */
    public void setUpTurn(){
        getLog().info("[NEW_TURN_SETUP] - Setting up turn number: " + this.turn);
        clearHarvest();
        clearProduction();
        council.clear();
        market.clear();
        players.forEach(pl -> pl.clearFamilyMembers());
        towers.keySet().forEach(color ->
            {
                getLog().info("Clearing "+color+" tower");
                towers.get(color).clear();
                //towers.get(color).addCards(cardsDeck.getCardsForTower(color, age));
            });
        getLog().info("Allotting family members to players");
        this.players.forEach(player ->
                DICE_COLORS.forEach(color ->
                        player.addFamilyMember((new FamilyMember(color, player.getColor())))));
        getLog().info("[NEW_TURN_SETUP_END]");
    }

    /**
     * This method adds a player to the game.
     * TODO: do we have to check again if there are more than 5?
     */
    public void addPlayer(Player pl){
        this.players.add(pl);
    }

    private void clearHarvest(){
        getLog().info("Clearing Harvest space");
        //TODO: implement
    }

    private void clearProduction(){
        getLog().info("Clearing Production space");
        //TODO: implement
    }

    public int getAge() { return  this.age; }
    public int getTurn() { return  this.turn; }
    public ArrayList<Player> getPlayers(){ return this.players; }

    //TODO: This seems not to work
    public Player getPlayer(String nickname) {
        getLog().info("Getting player "+nickname+" from "+players.size()+" Players.");
        return players.stream().filter(pl -> pl.getNickname().equals(nickname)).findFirst().orElse(null);
    }



    public void addPlayer(String nickname) {
        this.players.add(new Player(nickname));
    }

    /**
     * This method advances the game state of one turn
     */

    public void newTurn(){
        if(this.turn >= TURNS_PER_AGE){
            this.turn = 1;
            this.age++;
            getLog().info("Advancing into new age, number:" +this.age);
        } else {
            this.turn++;
            getLog().info("Advancing into new turn, number:" + this.turn);
        }
    }
}