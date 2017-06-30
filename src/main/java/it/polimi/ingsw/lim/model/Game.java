package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.cards.Card;
import it.polimi.ingsw.lim.model.cards.PurpleCard;
import it.polimi.ingsw.lim.model.excommunications.Excommunication;
import it.polimi.ingsw.lim.parser.Parser;

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
    public Game(GameController controllerCallback) {
        getLog().info("Creating game instance");
        this.board = new Board();
        this.board.setAge(1);
        this.board.setTurn(0); //it is updated by one as soon as the game starts.
        this.players = new ArrayList<>();
        this.cardsDeck = new CardsDeck();
        this.availablePlayerColors = new ArrayList<>(PLAYER_COLORS);
        this.dice = new HashMap<>();
        this.controllerCallback = controllerCallback;
        this.randomGenerator = new Random();
        dice.put(NEUTRAL_COLOR, NEUTRAL_FM_STRENGTH);
    }

    /**
     * The board, contains links to the structures
     */
    private Board board;

    /**
     * List of all the players
     */
    private ArrayList<Player> players;

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

    /**
     * reference to the controller handling this game instance
     */
    private GameController controllerCallback;

    private Random randomGenerator;

    // ############################################################# METHODS AHEAD

    /**
     * @return an excommunication based on the game's age
     */
    public Excommunication GetExcommunication(){
        return this.board.getExcommunications().get(this.board.getAge());
    }

    /**
     * This method rolls dices
     */
    public void rollDices(){
        //For every dice, generates a random number between 1 and 6.
        DICE_COLORS.forEach(color -> this.dice.replace(color, randomGenerator.nextInt(6)+1));
    }

    /**
     * This method sets up the game after it is created by the constructor.
     */
    public void setUpGame(Parser parsedGame) throws GameSetupException {
        getLog().info("[GAME SETUP BEGIN]");
        DICE_COLORS.forEach(color -> this.dice.put(color, 0));
        int playersNumber = this.players.size();
        if (playersNumber < 2 || playersNumber > 5)
            throw new GameSetupException("Wrong player number on game setup");

        getLog().info("Creating towers with bonuses and players card slots");
        DEFAULT_TOWERS_COLORS.forEach(color -> {
                this.board.getTowers().put(color, new Tower(parsedGame.getTowerbonuses(color)));
                this.players.forEach(player -> player.getCards().put(color, new ArrayList<Card>()));});
        if (playersNumber == 5){
            getLog().info("Creating fifth tower and black card slots.");
            this.board.getTowers().put(BLACK_COLOR, new Tower(parsedGame.getTowerbonuses(BLACK_COLOR)));
            this.players.forEach(player -> player.getCards().put(BLACK_COLOR, new ArrayList<Card>()));
        }
        getLog().info("Creating market with bonuses");
        this.board.setMarket(new Market(playersNumber, parsedGame.getMarketBonuses()));
        getLog().info("Creating council with bonuses");
        this.board.setCouncil(new Council(parsedGame.getCouncilFavors(), parsedGame.getCouncilBonus()));
        getLog().info("Adding bonuses to faith track");
        //TODO: maybe better with a standard for cycle?
        int i = 0;
        for (Assets bonus : parsedGame.getFaithTrackBonuses())
            this.board.getFaithTrack()[i] = bonus;
        /*TODO: write excomm
        for (i = 1; i <= AGES_NUMBER; i++) {
            this.board.addExcommunication(parsedGame.getExcommunications().get(i)
                    .get(randomGenerator.nextInt(parsedGame.getExcommunications().get(i).size())));
        } */
        //TODO: When we allot leadercards?
        /*
         * Distribute initial resources starting from the first player,
         * the following will have a coin more than the player before them.
         * TODO: where we decide the player order? at the beginning we consider their order of creation in the list.
         */
        getLog().log(Level.INFO, () -> "Giving initial resources to"+playersNumber+"players");
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
        getLog().log(Level.INFO, () -> "[NEW_TURN_SETUP] - Setting up turn number: "+ this.board.getTurn());
        clearHarvest();
        clearProduction();
        this.board.getCouncil().clear();
        this.board.getMarket().clear();
        players.forEach(pl -> pl.clearFamilyMembers());
        this.board.getTowers().keySet().forEach(color ->
            {
                getLog().info("Clearing "+color+" tower");
                this.board.getTowers().get(color).clear();
                getLog().log(Level.INFO,"Adding cards to "+color+" tower");
                this.board.getTowers().get(color).addCards(cardsDeck.getCardsForTower(color, this.board.getAge()));
            });
        getLog().info("Allotting family members to players");
        this.players.forEach(player ->
                DICE_COLORS.forEach(color ->
                        player.addFamilyMember((new FamilyMember(color, player.getColor())))));
        this.rollDices();
        getLog().info("[NEW_TURN_SETUP_END]");
    }

    private void clearHarvest(){
        getLog().info("Clearing Harvest space");
        this.board.setHarvest(new ArrayList<>());
    }

    private void clearProduction(){
        getLog().info("Clearing Production space");
        this.board.setProduction(new ArrayList<>());
    }

    public int getAge() { return  this.board.getAge(); }
    public int getTurn() { return  this.board.getTurn(); }
    public ArrayList<Player> getPlayers(){ return this.players; }


    public Player getPlayer(String nickname) {
        getLog().log(Level.INFO, () -> "Getting player "+nickname+" from "+players.size()+" Players");
        return players.stream().filter(pl -> pl.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    public Player getPlayerFromColor(String color) {
        return players.stream().filter(pl -> pl.getColor().equals(color)).findFirst().orElse(null);
    }


    /**
     * This method adds a player to the game.
     * TODO: do we have to check again if there are more than 5?
     */
    public Player addPlayer(String nickname) {
        String color = this.availablePlayerColors.remove(0);
        Player pl = new Player(nickname, color);
        this.players.add(pl);
        return pl;
    }

    /**
     * This method advances the game state of one turn
     */

    public void newTurn(){
        if(this.board.getTurn() >= TURNS_PER_AGE){
            this.board.setTurn(1);
            this.board.setAge(this.board.getAge()+1);
            getLog().log(Level.INFO, () -> "Advancing into new age, number: " + this.board.getAge());
            controllerCallback.handleExcommunications();
        } else {
            this.board.setTurn(this.board.getTurn()+1);
            getLog().log(Level.INFO, () -> "Advancing into new turn, number: " + this.board.getTurn());
        }
    }

    /**
     * Tells if it's time to excommunicate players
     * @return true if the game is in the last turn of the age.
     */
    public boolean excommunicationTime() {
        return this.board.getTurn() >= TURNS_PER_AGE;
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
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        if (destination.isOccupied()) return false;
        if (!destination.hasCard()) return false;
        FamilyMember slot = null;
        for (int i = 0; i < TOWER_HEIGHT; i++, slot = this.board.getTowers().get(towerColor).getFloor(i).getFamilyMember()) {
            if (slot != null && slot.getOwnerColor() ==
                    fm.getOwnerColor() && fm.getDiceColor() != NEUTRAL_COLOR)
                return false;
        }
        if(destination.getActionCost() >
                this.dice.get(fm.getDiceColor()) +
                        strength.getTowerStrength(towerColor))
            return false;
        return true;
    }



    /**
     * This method checks if a specified move into a tower is affordable by the player performing the move.
     * it also checks if is a purple card and has an optional cost to pick the card
     * TODO: add malus count while entering tower from excomm.
     * @param towerColor
     * @param floorNumber
     * @param fm
     * @return
     */
    public boolean isTowerMoveAffordable(String towerColor, int floorNumber, FamilyMember fm) {
        Floor destination = this.getTower(towerColor).getFloor(floorNumber);
        //Checking Costs
        Assets cardCost = destination.getCard().getCost().subtractToZero
                        (this.getPlayerFromColor(fm.getOwnerColor()).getPickDiscount(towerColor));
        Assets additionalCost = new Assets();
        Assets playerAssets = new Assets(this.getPlayerFromColor(fm.getOwnerColor()).getResources());
        additionalCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        if (this.isTowerOccupied(towerColor) && !playerAssets.isGreaterOrEqual(additionalCost))
            return false;
        if (this.servantsForTowerAction(fm, towerColor, floorNumber) > playerAssets.getServants())
            return false;
        if (this.getPlayerFromColor(fm.getOwnerColor()).isTowerBonusAllowed())
            playerAssets.add(destination.getInstantBonus());
        if (playerAssets.isGreaterOrEqual(cardCost))
            return true;
        return false;
    }

    public boolean isCardAffordable(Card card, Player actor, String towerColor) {
        Assets cardCost = card.getCost().subtractToZero
                (actor.getPickDiscount(towerColor));
        return (actor.getResources().isGreaterOrEqual(cardCost));
    }

    public boolean isPurpleCardAffordable(PurpleCard card, Player pl){
        return (pl.getResources().getVictoryPoints() > card.getOptionalBpRequirement());
    }

    /**
     * This method makes the actual tower move
     * @param towerColor
     * @param floorNumber
     * @param fm
     * @param servantsDeployed
     * @param useBp
     */
    public void towerMove(String towerColor, int floorNumber, FamilyMember fm, int servantsDeployed, boolean useBp) {
        Player actor = this.getPlayerFromColor(fm.getOwnerColor());
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        Card card = destination.pullCard();
        //Action cost is different whether the player wants to pay the card's cost or the purple's card bp cost.
        Assets actionCost = (useBp) ? new Assets().addServants(((PurpleCard)card).getOptionalBpCost()) :
                new Assets(card.getCost()).addServants(servantsDeployed);
        if(this.isTowerOccupied(towerColor))
            actionCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        destination.setFamilyMemberSlot(actor.pullFamilyMember(fm.getDiceColor()));
        actionCost.subtractToZero(actor.getPickDiscount(towerColor));
        actor.getResources().subtract(actionCost);
        actor.addCard(card, towerColor);
    }

    /**
     * This method checks if a player can perform a fast tower move, also if it can afford to pick the card.
     * @param towerColor
     * @param floor
     * @return
     */
    public boolean isFastTowerMoveAllowed(String towerColor, int floor, Player pl) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floor);
        if (!destination.hasCard()) return false;
        if (destination.getCard().getCost().isGreaterOrEqual(pl.getResources().add(pl.getPickDiscount(towerColor))))
            return false;
        return true;
    }

    /**
     * This method checks if any player has entered a specified tower
     * @param towerColor the color of the tower
     * @return boolean
     */
    public boolean isTowerOccupied(String towerColor) {
        for (int i = 1; i <= TOWER_HEIGHT; i++)
            if (this.board.getTowers().get(towerColor).getFloor(i).isOccupied())
                return true;
        return false;
    }

    /**
     * This method tells if the player can enter the harvest site with the provided family member
     * @param fm
     * @return boolean
     */
    public boolean isHarvestMoveAllowed(FamilyMember fm) {
        if(this.players.size() == 2 && !this.board.getHarvest().isEmpty())
            return false;
        for (FamilyMember f : this.board.getHarvest())
            if (f.getOwnerColor().equals(fm.getOwnerColor()) && ((f.getDiceColor().equals(NEUTRAL_COLOR)) ==
                    (fm.getDiceColor().equals(NEUTRAL_COLOR))))
                return false;
        if (servantsForHarvestAction(fm, 0) > getPlayerFromColor(fm.getOwnerColor()).getResources().getServants())
            return false;
        return true;
    }

    /**
     * Performs a move to the harvest site.
     * @param fm the family member used in this action
     */
    public void harvestMove(FamilyMember fm) {
        this.board.getHarvest().add(fm);
        getPlayerFromColor(fm.getOwnerColor()).pullFamilyMember(fm.getDiceColor());
    }

    /**
     * Performs a move to the harvest site.
     * @param fm the family member used in this action
     */
    public void productionMove(FamilyMember fm) {
        this.board.getProduction().add(fm);
        getPlayerFromColor(fm.getOwnerColor()).pullFamilyMember(fm.getDiceColor());
    }

    /**
     * returns the amount of servants required to perform an harvest action:
     * Action cost - Dice strength + Player bonus(or malus if negative)
     * @param fm
     * @return
     * TODO: add excomm malus ? is it already in the player?
     */
    public int servantsForProductionAction(FamilyMember fm, int baseStr) {
        int baseStrength = (fm == null) ? baseStr : this.dice.get(fm.getDiceColor());
        int actionCost = (this.board.getHarvest().size() <= PRODUCTION_DEFAULTSPACE_SIZE)
                ? PRODUCTION_DEFAULT_STR : PRODUCTION_STR_MALUS;
        int actionStr = baseStrength +
                getPlayerFromColor(fm.getOwnerColor()).getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * returns the amount of servants required to perform an harvest action:
     * Action cost - Dice strength + Player bonus(or malus if negative)
     * @param fm
     * @return
     * TODO: add excomm malus ? is it already in the player?
     */
    public int servantsForHarvestAction(FamilyMember fm, int baseStr) {
        int baseStrength = (fm == null) ? baseStr : this.dice.get(fm.getDiceColor());
        int actionCost = (this.board.getHarvest().size() <= HARVEST_DEFAULTSPACE_SIZE)
                ? HARVEST_DEFAULT_STR : HARVEST_STR_MALUS;
        int actionStr = baseStrength +
                getPlayerFromColor(fm.getOwnerColor()).getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * Calculates the strength to perform an harvest action. If family member is null, it means that this
     * is a fast action provided by an immediate effect of a card.
     * @param fm
     * @param servantsDeployed
     * @param tmpActionStr
     * @return
     */
    public int calcHarvestActionStr(FamilyMember fm, int servantsDeployed, int tmpActionStr) {
        int baseStr = (fm == null) ? tmpActionStr : this.getDice().get(fm.getDiceColor());
        return baseStr+servantsDeployed+getPlayerFromColor(fm.getOwnerColor()).getStrengths().getHarvestBonus();
    }

    /**
     * Calculates the strength to perform a production action. If family member is null, it means that this
     * is a fast action provided by an immediate effect of a card.
     * @param fm
     * @param servantsDeployed
     * @param tmpActionStr
     * @return
     */
    public int calcProductionActionStr(FamilyMember fm, int servantsDeployed, int tmpActionStr) {
        int baseStr = (fm == null) ? tmpActionStr : this.getDice().get(fm.getDiceColor());
        return baseStr+servantsDeployed+getPlayerFromColor(fm.getOwnerColor()).getStrengths().getProductionBonus();
    }

    public boolean isProductionMoveAllowed(FamilyMember fm) {
        if(this.players.size() == 2 && !this.board.getProduction().isEmpty())
            return false;
        for (FamilyMember f : this.board.getProduction())
            if (f.getOwnerColor().equals(fm.getOwnerColor()) && ((f.getDiceColor().equals(NEUTRAL_COLOR)) ==
                    (fm.getDiceColor().equals(NEUTRAL_COLOR))))
                return false;
        if (servantsForProductionAction(fm, 0) > getPlayerFromColor(fm.getOwnerColor()).getResources().getServants())
            return false;
        return true;
    }

    public void addToHarvest(FamilyMember fm) {
        this.board.getHarvest().add(fm);
    }

    public void addToProduction(FamilyMember fm) {
        this.board.getProduction().add(fm);
    }

    public Tower getTower(String color){
        return this.board.getTowers().get(color);
    }


    public ArrayList<String> getNewPlayerOrder() {
        ArrayList<FamilyMember> fms = this.board.getCouncil().getFamilyMembers();
        ArrayList<String> councilPlayers = new ArrayList<>();
        for (FamilyMember fm : fms) {
            councilPlayers.add(players.stream().filter(pl -> pl.getColor().equals(fm.getOwnerColor()))
                    .findFirst().orElse(null).getNickname());
        }
        for (Player pl : this.players) {
            if (!councilPlayers.contains(pl.getNickname()))
                councilPlayers.add(pl.getNickname());
        }
        return councilPlayers;
    }

    public Council getCouncil() {
        return this.board.getCouncil();
    }

    /**
     * This method calculates the amount of servants that a player needs to perform a tower action
     * TODO: CHECK CORRECT RESULT
     * @param fm
     * @param towerColor
     * @param floor
     * @return
     */
    public int servantsForTowerAction(FamilyMember fm,String towerColor, int floor) {
        int actionStr = dice.get(fm.getDiceColor())
                + this.getPlayerFromColor(fm.getOwnerColor()).getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        return (servants > 0) ? -servants : 0;
    }

    public int servantsForFastTowerAction(int actionBonus,String towerColor, int floor, Player pl) {
        int actionStr = actionBonus
                + pl.getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        return (servants > 0) ? -servants : 0;
    }

    public Board getBoard() {
        return board;
    }

    // ------------------------------------------------------------------------ Excommunications

    /**
     * This method returns if a player has enough faith points in order not to be excommunicated
     * @param pl the player
     * @return the answer
     */
    public boolean isNotExcommunicable(Player pl) {
        return pl.getResources().getFaithPoints() > FIRST_EXCOMM_FP+this.board.getAge()-1;
    }

    public Excommunication getExcommunicationByAge(int age) {
        return this.board.getExcommunications().stream().filter(excomm -> excomm.getAge() == age)
                .findFirst().orElse(null);
    }

    /**
     * FOLLOWING METHODS ARE USED ONLY FOR TESTING PURPOSES
     */
    public ArrayList<FamilyMember> getHarvest() {
        return this.board.getHarvest();
    }
    public ArrayList<FamilyMember> getProduction() {
        return this.board.getProduction();
    }

    public HashMap<String, Integer> getDice() {
        return dice;
    }
}