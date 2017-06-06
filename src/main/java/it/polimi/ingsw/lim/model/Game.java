package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.parser.Parser;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.util.*;
import java.util.logging.Level;

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
        this.cardsDeck = new CardsDeck();
        this.availablePlayerColors = new ArrayList<>(PLAYER_COLORS);
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

    /**
     *
     */
    private List<String> availablePlayerColors;

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

        getLog().info("Creating towers with bonuses");
        DEFAULT_TOWERS_COLORS.forEach(color ->
                this.towers.put(color, new Tower(parsedGame.getTowerbonuses(color))));
        if (playersNumber == 5){
            getLog().info("Creating fifth tower.");
            this.towers.put(BLACK_COLOR, new Tower(parsedGame.getTowerbonuses(BLACK_COLOR)));
        }
        getLog().info("Creating market with bonuses");
        this.market = new Market(playersNumber, parsedGame.getMarketBonuses());
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
        getLog().log(Level.INFO, "Giving initial resources to %s players", playersNumber);
        int moreCoin = 0;
        for (Player pl : players) {
            pl.setResources(pl.getResources().add(parsedGame.getStartingGameBonus()).addCoins(moreCoin));
            moreCoin++;
        }

        getLog().info("Moving loaded cards to Cards Deck");
        //TODO: when the china's work is done, change 1 to AGES_NUMBER
        for (int j = 1; j <= 1; j++) {
            cardsDeck.addDevelopementCardsOfAge(j,parsedGame.getCard(j));
        }

        getLog().info("[GAME SETUP END]");
    }

    /**
     * This method cleans the board and sets up another turn (reading it from it's state)
     * Deciding when to advance in ages and turn is a task of the main game controller.
     */
    public void setUpTurn(){
        getLog().log(Level.INFO, "[NEW_TURN_SETUP] - Setting up turn number: %s", this.turn);
        clearHarvest();
        clearProduction();
        council.clear();
        market.clear();
        players.forEach(pl -> pl.clearFamilyMembers());
        towers.keySet().forEach(color ->
            {
                getLog().info("Clearing "+color+" tower");
                towers.get(color).clear();
                getLog().log(Level.INFO,"Adding cards to %s tower", color);
                towers.get(color).addCards(cardsDeck.getCardsForTower(color, age));
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
        this.harvest = new ArrayList<>();
    }

    private void clearProduction(){
        getLog().info("Clearing Production space");
        this.harvest = new ArrayList<>();
    }

    public int getAge() { return  this.age; }
    public int getTurn() { return  this.turn; }
    public ArrayList<Player> getPlayers(){ return this.players; }


    public Player getPlayer(String nickname) {
        getLog().log(Level.INFO, "Getting player %s Players.", nickname+" from "+players.size());
        return players.stream().filter(pl -> pl.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    public Player getPlayerFromColor(String color) {
        return players.stream().filter(pl -> pl.getColor().equals(color)).findFirst().orElse(null);
    }


    /**
     * This method picks an available color and adds it to the new created player
     * @param nickname
     */
    public void addPlayer(String nickname) {
        String color = this.availablePlayerColors.remove(0);
        this.players.add(new Player(nickname, color));
    }

    /**
     * This method advances the game state of one turn
     */

    public void newTurn(){
        if(this.turn >= TURNS_PER_AGE){
            this.turn = 1;
            this.age++;
            getLog().log(Level.INFO, () -> "Advancing into new age, number: " + this.age);
        } else {
            this.turn++;
            getLog().log(Level.INFO, () -> "Advancing into new turn, number: %d" + this.turn);
        }
    }

    /**
     * This method checks if a player is able to put a family member in a tower on a specified floor.
     * it checks if that floor is occupied, if there are others family members of that player in the tower and
     * if the player has enough strength to perform the action
     * @param towerColor
     * @param floorNumber
     * @param fm the family member performing the action
     * @param strength all the bonuses and maluses except the family member
     */
    public boolean isTowerMoveAllowed(String towerColor, int floorNumber, FamilyMember fm, Strengths strength) {
        if(towers.get(towerColor).getFloor(floorNumber).isOccupied())
            return false;
        for (int i = 1; i <= TOWER_HEIGHT; i++)
            if (towers.get(towerColor).getFloor(i).getFamilyMember().getOwnerColor() == fm.getOwnerColor() && fm.getDiceColor() != NEUTRAL_COLOR)
                return false;
        if(towers.get(towerColor).getFloor(floorNumber).getActionCost() > this.dice.get(fm.getDiceColor() + strength.getTowerStrength(towerColor)))
            return false;
        return true;
    }

    /**
     * This method checks if a specified move into a tower is affordable by the player performing the move.
     * TODO: add malus count while entering tower from excomm.
     * @param towerColor
     * @param floorNumber
     * @param fm
     * @return
     */
    public boolean isTowerMoveAffordable(String towerColor, int floorNumber, FamilyMember fm) {
        Floor destination = this.getTower(towerColor).getFloor(floorNumber);
        Assets cardCost = destination.getCard().getCost();
        Assets additionalCost = new Assets();
        Assets playerAssets = new Assets(this.getPlayerFromColor(fm.getOwnerColor()).getResources());
        additionalCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        if (this.isTowerOccupied(towerColor) && playerAssets.subtract(additionalCost).isNegative())
            return false;
        if (this.getPlayerFromColor(fm.getOwnerColor()).isTowerBonusAllowed())
            playerAssets.add(destination.getInstantBonus());
        if (playerAssets.isGreaterOrEqual(cardCost))
            return true;
        return false;
    }

    /**
     * This method checks if any player has entered a specified tower
     * @param towerColor
     * @return
     */
    public boolean isTowerOccupied(String towerColor) {
        for (int i = 1; i <= TOWER_HEIGHT; i++)
            if (towers.get(towerColor).getFloor(i).isOccupied())
                return true;
        return false;
    }

    public boolean isHarvestMoveAllowed(FamilyMember fm) {
        return false;
    }

    public Tower getTower(String color){
        return this.towers.get(color);
    }


    public ArrayList<String> getNewPlayerOrder() {
        ArrayList<FamilyMember> fms = council.getFamilyMembers();
        ArrayList<String> councilPlayers = new ArrayList<>();
        for (FamilyMember fm : fms) {
            councilPlayers.add(players.stream().filter(pl -> pl.getColor().equals(fm.getOwnerColor())).findFirst().orElse(null).getNickname());
        }
        for (Player pl : this.players) {
            if (!councilPlayers.contains(pl.getNickname()))
                councilPlayers.add(pl.getNickname());
        }
        return councilPlayers;
    }

    public Council getCouncil() {
        return council;
    }
}