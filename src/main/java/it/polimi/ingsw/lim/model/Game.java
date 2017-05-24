package it.polimi.ingsw.lim.model;
import java.util.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * THE GAME INSTANCE
 * This class acts as a hub between all places of the game (model), it has links to others model classes and joins the
 * model with the controller. It has al the low-level management that isn't suitable for the main controller.
 */
public class Game {

    /**
     * Constructor
     */
    public Game() {
    }

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
        for (Player pl : this.players){
            for (String color : FAMILY_MEMBERS_COLORS)
                pl.addFamilyMember(new FamilyMember(color, pl.getColor()));
        }
    }

    /**
     * This method sets up the game after it is created by the constructor.
     */
    public void setUpGame(int playersNumber){
        //Create towers.
        this.towers.put(GREEN_COLOR, new Tower(GREEN_COLOR));
        this.towers.put(BLUE_COLOR, new Tower(BLUE_COLOR));
        this.towers.put(YELLOW_COLOR, new Tower(YELLOW_COLOR));
        this.towers.put(PURPLE_COLOR, new Tower(PURPLE_COLOR));
        //TODO: put bonuses in every tower
        if (playersNumber == 5){
            this.towers.put(BLACK_COLOR, new Tower(BLACK_COLOR));
        }
        //Create market TODO: put bonuses in the market
        this.market = new Market(playersNumber);
        //Create council TODO: get values from game parser
        //TODO: this.council = new Council();

        /*
         *Distribute initial resources starting from the first player,
         *the following will have a coin more than the player before them.
         */
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
        for (String color : towers.keySet()){
            //Clean the tower
            //TODO:Do we really need to clean the tower or we can simply overwrite them? (yes, maybe not for cards slots)
            towers.get(color).clean();
            //fill the towers with new cards
        }

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

    }





}