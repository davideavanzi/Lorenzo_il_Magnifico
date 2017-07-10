package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.cards.Card;
import it.polimi.ingsw.lim.model.cards.PurpleCard;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.leaders.ActivableLeader;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import it.polimi.ingsw.lim.model.leaders.Leaders;
import it.polimi.ingsw.lim.model.leaders.PermanentLeader;
import it.polimi.ingsw.lim.parser.Parser;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.model.leaders.Leaders.MIRANDOLA_PICK_BONUS;
import static it.polimi.ingsw.lim.model.leaders.Leaders.MORO_FM_BONUS;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * THE GAME INSTANCE
 * This class acts as a hub between the instance of the game (model) and the main logic in the controller package,
 * it has links to the board and all the others model classes.
 * It deals with all the low-level game management (little logic) in order to make the main controller slimmer.
 */
public class Game {

    /**
     * The board, contains links to the structures (such as market, council etc.)
     */
    private Board board;
    /**
     * List of all the players in this game instance
     */
    private ArrayList<Player> players;
    /**
     * Link to the cards container, it holds all cards that are yet to be played
     */
    private CardsDeck cardsDeck;
    /**
     * This list holds colors that are not picked by players yet
     */
    private List<String> availablePlayerColors;
    /**
     * reference to the controller handling this game instance
     */
    @JsonIgnore
    private GameController controllerCallback;
    @JsonIgnore
    private Random randomGenerator;

    /**
     * Constructor. Sets starting age and turn. These go from 1 inclusive to their MAX value defined in the settings
     */
    public Game(GameController controllerCallback) {
        this.controllerCallback = controllerCallback;
        this.board = new Board();
        this.players = new ArrayList<>();
        this.cardsDeck = new CardsDeck();
        this.availablePlayerColors = new ArrayList<>(PLAYER_COLORS);
        this.randomGenerator = new Random();
    }

    /**
     * Empty constructor, setters and getters are used to save and retrieve game from file.
     */
    public Game(){
        getLog().info("Creating game instance");
        this.board = new Board();
        this.players = new ArrayList<>();
        this.cardsDeck = new CardsDeck();
        this.availablePlayerColors = new ArrayList<>(PLAYER_COLORS);
        this.randomGenerator = new Random();
    }

    public CardsDeck getCardsDeck (){
        return cardsDeck;
    }

    public void setCardsDeck(CardsDeck cardsDeck){
        this.cardsDeck = cardsDeck;
    }

    public List<String> getAvailablePlayerColors(){
        return availablePlayerColors;
    }

    public void setAvailablePlayerColors(List<String> availablePlayerColors){
        this.availablePlayerColors = availablePlayerColors;
    }

    @JsonIgnore
    public int getAge() { return  this.board.getAge(); }

    /**
     * This method calculates the strength value of a given family member.
     * @param fm the family member
     * @return the calculated value
     */
    public int getFmStrength(FamilyMember fm) {
        if (getPlayerFromColor(fm.getOwnerColor()).getDiceOverride().get(fm.getDiceColor()) != null)
            return getPlayerFromColor(fm.getOwnerColor()).getDiceOverride().get(fm.getDiceColor());
        if (playerHasActiveLeader(10, getPlayerFromColor(fm.getOwnerColor())) &&
                !fm.getDiceColor().equals(NEUTRAL_COLOR)) return MORO_FM_BONUS;
        int baseFmValue = (fm.getDiceColor().equals(NEUTRAL_COLOR)) ? NEUTRAL_FM_STRENGTH :
                this.board.getDice().get(fm.getDiceColor());
        return baseFmValue+getPlayerFromColor(fm.getOwnerColor())
                .getStrengths().getDiceBonus().get(fm.getDiceColor());
    }

    /**
     * clears the harvest site
     */
    private void clearHarvest(){
        getLog().info("Clearing Harvest space");
        this.board.setHarvest(new ArrayList<>());
    }

    /**
     * clears the production site
     */
    private void clearProduction(){
        getLog().info("Clearing Production space");
        this.board.setProduction(new ArrayList<>());
    }

    @JsonIgnore
    public int getTurn() { return  this.board.getTurn(); }

    public ArrayList<Player> getPlayers(){ return this.players; }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    @JsonIgnore
    public Council getCouncil() {
        return this.board.getCouncil();
    }

    @JsonIgnore
    public Player getPlayer(String nickname) {
        getLog().log(Level.INFO, () -> "Getting player "+nickname+" from "+players.size()+" Players");
        return players.stream().filter(pl -> pl.getNickname().equals(nickname)).findFirst().orElse(null);
    }

    @JsonIgnore
    public Player getPlayerFromColor(String color) {
        return players.stream().filter(pl -> pl.getColor().equals(color)).findFirst().orElse(null);
    }

    /**
     * This method adds a player to the game.
     * @param nickname the nickname of the user
     */
    public Player addPlayer(String nickname) {
        String color = this.availablePlayerColors.remove(0);
        Player pl = new Player(nickname, color);
        this.players.add(pl);
        return pl;
    }

    /**
     * This method sets up the game after it is created by the constructor.
     * @param parsedGame the parsed game
     */
    public void setUpGame(Parser parsedGame) throws GameSetupException {
        getLog().info("[GAME SETUP BEGIN]");
        this.board.setAge(1);
        this.board.setTurn(0); //it is updated by one as soon as the game starts.
        int playersNumber = this.players.size();
        if (playersNumber < 2 || playersNumber > 5)
            throw new GameSetupException("Wrong player number on game setup");
        getLog().info("Creating towers with bonuses and players card slots");
        HashMap<String, Tower> towers = new HashMap<>();
        DEFAULT_TOWERS_COLORS.forEach(color -> {
            towers.put(color, new Tower(parsedGame.getTowerbonuses(color)));
            this.players.forEach(player -> player.getCards().put(color, new ArrayList<Card>()));});
        if (playersNumber == 5){
            getLog().info("Creating fifth tower and black card slots.");
            towers.put(BLACK_COLOR, new Tower(parsedGame.getTowerbonuses(BLACK_COLOR)));
            this.players.forEach(player -> player.getCards().put(BLACK_COLOR, new ArrayList<Card>()));
        }
        this.board.setTowers(towers);
        getLog().info("Creating market with bonuses");
        this.board.setMarket(new Market(playersNumber, parsedGame.getMarketBonuses()));
        getLog().info("Creating council with bonuses");
        this.board.setCouncil(new Council(parsedGame.getCouncilFavors(), parsedGame.getCouncilBonus()));
        getLog().info("Adding bonuses to faith track");
        this.board.setFaithTrack(parsedGame.getFaithTrackBonuses());
        getLog().info("Giving each user a random production/harvest default bonus");
        this.players.forEach(player -> {
            int choice = randomGenerator.nextInt(parsedGame.getBoardPlayersHarvestBonus().size());
            player.setDefaultHarvestBonus(parsedGame.getBoardPlayersHarvestBonus().get(choice));
            player.setDefaultProductionBonus(parsedGame.getBoardPlayersProductionBonus().get(choice));
        });
        for (int i = 1; i <= AGES_NUMBER; i++) {
            this.board.addExcommunication(parsedGame.getExcommunications().get(i)
                    .get(randomGenerator.nextInt(parsedGame.getExcommunications().get(i).size())));
        }
        getLog().log(Level.INFO, () -> "Giving initial resources to"+playersNumber+"players");
        int moreCoin = 0;
        for (Player pl : players) {
            giveAssetsToPlayer(parsedGame.getStartingGameBonus().addCoins(moreCoin), pl);
            moreCoin++;
        }
        getLog().info("Moving loaded cards to Cards Deck");
        for (int j = 1; j <= AGES_NUMBER; j++) {
            cardsDeck.addDevelopementCardsOfAge(j,parsedGame.getCard(j));
        }
        ArrayList<Assets> cfBonuses = new ArrayList<>(Arrays.asList(parsedGame.getCouncilFavourBonuses()));
        this.board.getCouncil().setFavorBonuses(cfBonuses);
        getLog().info("[GAME SETUP END]");
    }

    /**
     * This method advances the game state of one turn
     */
    public void newTurn(){
        if(this.board.getTurn() >= TURNS_PER_AGE){
            this.board.setTurn(1);
            this.board.setAge(this.board.getAge()+1);
            getLog().log(Level.INFO, () -> "Advancing into new age, number: " + this.board.getAge());
        } else {
            this.board.setTurn(this.board.getTurn()+1);
            getLog().log(Level.INFO, () -> "Advancing into new turn, number: " + this.board.getTurn());
        }
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
            ArrayList<Card> cards = cardsDeck.pullCardsForTower(color, this.board.getAge());
            if(!cards.isEmpty()){
                getLog().info("Clearing "+color+" tower");
                this.board.getTowers().get(color).clear();
                getLog().log(Level.INFO,"Adding cards to "+color+" tower");
                this.board.getTowers().get(color).addCards(cards);
            }
        });
        getLog().info("Allotting family members to players");
        this.players.forEach(player ->
                FM_COLORS.forEach(color ->
                        player.addFamilyMember((new FamilyMember(color, player.getColor())))));
        this.board.rollDices();

        getLog().info("Resetting activated leaders");
        this.players.forEach(player -> player.getActivatedLeaders()
                .forEach(leaderCard -> ((ActivableLeader)leaderCard).setActivated(false)));
        this.players.forEach(player -> player.getDiceOverride().clear());

        getLog().info("[NEW_TURN_SETUP_END]");
    }

    /**
     * This method checks if a player is able to put a family member in a tower on a specified floor.
     * it checks if that floor is occupied, if there are others family members of that player in the tower and
     * if the player has enough strength to perform the action
     * @param towerColor the destination tower's color
     * @param floorNumber the destination floor's number
     * @param fm the family member performing the action
     * @return true if the moved is allowed by game rules
     */
    @JsonIgnore
    public boolean isTowerMoveAllowed(String towerColor, int floorNumber, FamilyMember fm) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        if (destination.isOccupied()) return false;
        if (!destination.hasCard()) return false;
        if (!canPickCard(towerColor, fm)) return false;
        FamilyMember slot = null;
        for (int i = 0; i < TOWER_HEIGHT; i++, slot = this.board.getTowers().get(towerColor).getFloor(i).getFamilyMemberSlot()) {
            if (slot != null && slot.getOwnerColor().equals(fm.getOwnerColor()) && !fm.getDiceColor().equals(NEUTRAL_COLOR))
                return false;
        }
        return true;
    }

    /**
     * this method tells if a player (fot from it's family member) can take one more card
     * @param fm the family member performing the action
     * @return true if the user is able to pick the card under the game rules
     */
     private boolean canPickCard(String towerColor, FamilyMember fm) {
        Player pl = getPlayerFromColor(fm.getOwnerColor());
        if (pl.getCardsOfColor(towerColor).size() >= 6) return false;
        if (pl.getResources().getBattlePoints() >=
                PLAYER_TERRITORIES_REQ[pl.getCardsOfColor(GREEN_COLOR).size()] && !playerHasActiveLeader(15, pl))
            return false;
        return true;
     }

    /**
     * This method checks if a specified move into a tower is affordable by the player performing the move.
     * it also checks if is a purple card and has an optional cost to pick the card
     * @param towerColor the destination tower's color
     * @param floorNumber the destination floor's number
     * @param fm the family member performing the action
     * @return this.getPlayerFromColor(fm.getOwnerColor()
     */
    @JsonIgnore
    public boolean isTowerMoveAffordable(String towerColor, int floorNumber, FamilyMember fm) {
        Player pl = this.getPlayerFromColor(fm.getOwnerColor());
        Floor destination = this.getTower(towerColor).getFloor(floorNumber);
        Assets cardCost = destination.getCardSlot().getCost().subtractToZero
                        (pl.getPickDiscount(towerColor));
        Assets additionalCost = new Assets();
        Assets playerAssets = new Assets(pl.getResources());
        if (this.isTowerOccupied(towerColor) && playerHasActiveLeader(3, getPlayerFromColor(fm.getOwnerColor())) &&
                pl.getResources().getCoins() > COINS_TO_ENTER_OCCUPIED_TOWER)
            cardCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        else return false;
        if (this.isTowerOccupied(towerColor) && !playerAssets.isGreaterOrEqual(additionalCost))
            return false;
        if (this.servantsForTowerAction(fm, towerColor, floorNumber) > playerAssets.getServants())
            return false;
        if (this.isPlayerTowerBonusAllowed(this.getPlayerFromColor(fm.getOwnerColor())))
            playerAssets.add(this.apllyExcommMalus(destination.getInstantBonus(),pl));
        if (playerAssets.isGreaterOrEqual(cardCost))
            return true;
        return false;
    }

    @JsonIgnore
    public boolean isCardAffordable(Card card, Player actor, String towerColor, Assets optionalPickDiscount) {
        Assets cardCost = card.getCost().subtractToZero
                (actor.getPickDiscount(towerColor).subtractToZero(optionalPickDiscount));
        if (playerHasActiveLeader(20, actor)) cardCost.subtractToZero(MIRANDOLA_PICK_BONUS);
        return (actor.getResources().isGreaterOrEqual(cardCost));
    }

    @JsonIgnore
    public boolean isPurpleCardAffordable(PurpleCard card, Player pl){
        return (card.getOptionalBpRequirement() > 0 && pl.getResources().getVictoryPoints() > card.getOptionalBpRequirement());
    }

    /**
     * This method makes the actual tower move
     * @param towerColor the destination tower's color
     * @param floorNumber the destination floor's number
     * @param fm the fm performing the move
     * @param servantsDeployed the amount of servants spent to perform the action
     * @param useBp is true if the player wants to pay the purple card with battle points
     * @return the picked card
     */
    public Card towerMove(String towerColor, int floorNumber, FamilyMember fm, int servantsDeployed, boolean useBp) {
        Player actor = this.getPlayerFromColor(fm.getOwnerColor());
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        Card card = destination.pullCard();
        Assets actionCost = (useBp) ? new Assets().addServants(((PurpleCard)card).getOptionalBpCost()) :
                new Assets(card.getCost()).addServants(servantsDeployed);
        if (playerHasActiveLeader(20, actor)) actionCost.subtractToZero(MIRANDOLA_PICK_BONUS);
        if(this.isTowerOccupied(towerColor))
            actionCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        destination.setFamilyMemberSlot(actor.pullFamilyMember(fm.getDiceColor()));
        actionCost.subtractToZero(actor.getPickDiscount(towerColor));
        if (this.isPlayerTowerBonusAllowed(actor)) giveAssetsToPlayer(destination.getInstantBonus(), actor);
        removeAssetsFromPlayer(actionCost, actor);
        actor.addCard(card, towerColor);
        return card;
    }

    /**
     * This method makes the actual fast tower move.
     * @param towerColor the destination tower's color
     * @param floorNumber the destination floor's number
     * @param servantsDeployed the amount of servants spent to perform the action
     * @param useBp is true if the player wants to pay the purple card with battle points
     */
    public void fastTowerMove(String towerColor, int floorNumber, int servantsDeployed, boolean useBp, Player actor,
                              Assets optionalPickDiscount) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        Card card = destination.pullCard();
        Assets actionCost = (useBp) ? new Assets().addServants(((PurpleCard)card).getOptionalBpCost()) :
                new Assets(card.getCost()).addServants(servantsDeployed);
        if (playerHasActiveLeader(20, actor)) actionCost.subtractToZero(MIRANDOLA_PICK_BONUS);
        if(this.isTowerOccupied(towerColor))
            actionCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        if(this.isPlayerTowerBonusAllowed(actor))
            actionCost.subtractToZero(actor.getPickDiscount(towerColor).subtractToZero(
                    this.apllyExcommMalus(optionalPickDiscount, actor)));
        actor.getResources().subtract(actionCost);
        actor.addCard(card, towerColor);
        //return card;
    }

    /**
     * This method checks if a player can perform a fast tower move, also if it can afford to pick the card.
     * @param towerColor the destination tower's color
     * @param floor the destination floor's color
     * @return if the fast tower move is allowed
     */
    @JsonIgnore
    public boolean isFastTowerMoveAllowed(String towerColor, int floor, Player pl, Assets optionalPickDiscount) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floor);
        if (!destination.hasCard()) return false;
        Assets actionCost = destination.getCardSlot().getCost();
        if (playerHasActiveLeader(20, pl)) actionCost.subtractToZero(MIRANDOLA_PICK_BONUS);
        if (this.isTowerOccupied(towerColor) && pl.getResources().getCoins() > COINS_TO_ENTER_OCCUPIED_TOWER)
            actionCost.addCoins(COINS_TO_ENTER_OCCUPIED_TOWER);
        else return false;
        Assets availableAssets = new Assets(pl.getResources()
                .add(pl.getPickDiscount(towerColor).add(optionalPickDiscount).add(this.apllyExcommMalus(destination.getInstantBonus(),pl))));
        if (actionCost.isGreaterOrEqual(availableAssets))
            return false;
        return true;
    }

    /**
     * This method checks if any player has entered a specified tower
     * @param towerColor the color of the tower
     * @return boolean
     */
    @JsonIgnore
    public boolean isTowerOccupied(String towerColor) {
        for (int i = 1; i <= TOWER_HEIGHT; i++)
            if (this.board.getTowers().get(towerColor).getFloor(i).isOccupied())
                return true;
        return false;
    }

    /**
     * This method tells if the player can enter the harvest site with the provided family member
     * @param fm the family member involved in the action
     * @return if the move is allowed by the game rules
     */
    @JsonIgnore
    public boolean isHarvestMoveAllowed(FamilyMember fm) {
        if(this.players.size() == 2 && (!this.board.getHarvest().isEmpty() ||
                playerHasActiveLeader(2, getPlayerFromColor(fm.getOwnerColor()))))
            return false;
        for (FamilyMember f : this.board.getHarvest())
            if (f.getOwnerColor().equals(fm.getOwnerColor()) && ((f.getDiceColor().equals(NEUTRAL_COLOR)) ==
                    (fm.getDiceColor().equals(NEUTRAL_COLOR))))
                return false;
        if (servantsForHarvestAction(getPlayerFromColor(fm.getOwnerColor()),fm, 0) > getPlayerFromColor(fm.getOwnerColor()).getResources().getServants())
            return false;
        return true;
    }

    /**
     * Performs a move to the harvest site.
     * @param fm the family member used in this action
     * @param servantsDeployed  the amount of servants spent to perform the action
     */
    public void harvestMove(FamilyMember fm, int servantsDeployed) {
        addToHarvest(fm);
        getPlayerFromColor(fm.getOwnerColor()).pullFamilyMember(fm.getDiceColor());
        Assets actionCost = new Assets().addServants(servantsDeployed);
        removeAssetsFromPlayer(actionCost, getPlayerFromColor(fm.getOwnerColor()));
    }

    /**
     * Performs a move to the harvest site.
     * @param fm the family member used in this action
     * @param servantsDeployed  the amount of servants spent to perform the action
     */
    public void productionMove(FamilyMember fm, int servantsDeployed) {
        addToProduction(fm);
        getPlayerFromColor(fm.getOwnerColor()).pullFamilyMember(fm.getDiceColor());
        Assets actionCost = new Assets().addServants(servantsDeployed);
        removeAssetsFromPlayer(actionCost, getPlayerFromColor(fm.getOwnerColor()));
    }

    /**
     * returns the amount of servants required to perform an harvest action:
     * @param fm the family member involved in the action
     * @param baseStr the strength provided by the fast action bonus instead of the family member's one
     * @return the amount of servants needed by the player to perform the production action
     */
    public int servantsForProductionAction(Player pl, FamilyMember fm, int baseStr) {
        int baseStrength = (fm == null) ? baseStr : this.getFmStrength(fm);
        int actionCost = (this.board.getHarvest().size() <= PRODUCTION_DEFAULTSPACE_SIZE &&
                !playerHasActiveLeader(2, pl))
                ? PRODUCTION_DEFAULT_STR : PRODUCTION_STR_MALUS;
        int actionStr = baseStrength + pl.getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(pl)) servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * returns the amount of servants required to perform an harvest action:
     * @param fm the family member involved in the action
     * @param baseStr the strength provided by the fast action bonus instead of the family member's one
     * @return the amount of servants needed by the player to perform the harvest action
     */
    public int servantsForHarvestAction(Player pl, FamilyMember fm, int baseStr) {
        int baseStrength = (fm == null) ? baseStr : this.getFmStrength(fm);
        int actionCost = (this.board.getHarvest().size() <= HARVEST_DEFAULTSPACE_SIZE &&
                !playerHasActiveLeader(2, pl))
                ? HARVEST_DEFAULT_STR : HARVEST_STR_MALUS;
        int actionStr = baseStrength + pl.getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(pl)) servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * Calculates the strength to perform an harvest action. If family member is null, it means that this
     * is a fast action provided by an immediate effect of a card.
     * @param fm the family member involved in the action
     * @param servantsDeployed the servants spent to perform the action
     * @param tmpActionStr the action strength provided by a bonus
     * @return the calculated action strength
     */
    public int calcHarvestActionStr(Player pl, FamilyMember fm, int servantsDeployed, int tmpActionStr) {
        int baseStr = (fm == null) ? tmpActionStr : this.getFmStrength(fm);
        if (isPlayerServantsExcommunicated(pl))
            servantsDeployed /= 2; //it should be always a even number, because the client doubled the amount of servants while deploying
        if (this.board.getHarvest().size() >= HARVEST_DEFAULTSPACE_SIZE  &&
                !playerHasActiveLeader(2, pl)) baseStr -= HARVEST_STR_MALUS;
        return baseStr+servantsDeployed+pl.getStrengths().getHarvestBonus();
    }

    /**
     * Calculates the strength to perform a production action. If family member is null, it means that this
     * is a fast action provided by an immediate effect of a card.
     * @param fm the family member involved in the action
     * @param servantsDeployed the servants spent to perform the action
     * @param tmpActionStr the action strength provided by a bonus
     * @return the calculated action strength
     */
    public int calcProductionActionStr(Player actor, FamilyMember fm, int servantsDeployed, int tmpActionStr) {
        int baseStr = (fm == null) ? tmpActionStr : this.getFmStrength(fm);
        if (isPlayerServantsExcommunicated(actor))
            servantsDeployed /= 2;
        if (this.board.getProduction().size() >= PRODUCTION_DEFAULTSPACE_SIZE  &&
                !playerHasActiveLeader(2, getPlayerFromColor(fm.getOwnerColor()))) baseStr -= PRODUCTION_STR_MALUS;
        return baseStr+servantsDeployed+actor.getStrengths().getProductionBonus();
    }

    /**
     * This method tells if a production move is allowed by game rules.
     * @param fm the family member involved in the action
     * @return true if the action is allowed, false otherwise
     */
    @JsonIgnore
    public boolean isProductionMoveAllowed(FamilyMember fm) {
        if(this.players.size() == 2 && (!this.board.getProduction().isEmpty() ||
                playerHasActiveLeader(2, getPlayerFromColor(fm.getOwnerColor()))))
            return false;
        for (FamilyMember f : this.board.getProduction())
            if (f.getOwnerColor().equals(fm.getOwnerColor()) && ((f.getDiceColor().equals(NEUTRAL_COLOR)) ==
                    (fm.getDiceColor().equals(NEUTRAL_COLOR))))
                return false;
        if (servantsForProductionAction(getPlayerFromColor(fm.getOwnerColor()), fm, 0) > getPlayerFromColor(fm.getOwnerColor()).getResources().getServants())
            return false;
        return true;
    }

    public void addToHarvest(FamilyMember fm) {
        this.board.getHarvest().add(fm);
    }

    public void addToProduction(FamilyMember fm) {
        this.board.getProduction().add(fm);
    }

    @JsonIgnore
    public Tower getTower(String color){
        return this.board.getTowers().get(color);
    }

    /**
     * This method generates a playing order, following the game rules
     * @return an ordered list of player usern
     */
    @JsonIgnore
    public ArrayList<String> getNewPlayerOrder() {
        ArrayList<FamilyMember> fms = this.board.getCouncil().getFamilyMembers();
        ArrayList<String> councilPlayers = new ArrayList<>();
        for (FamilyMember fm : fms) {
            Player pl = getPlayerFromColor(fm.getOwnerColor());
            if (!councilPlayers.contains(pl.getNickname()))
                councilPlayers.add(pl.getNickname());
        }
        for (Player pl : this.players) {
            if (!councilPlayers.contains(pl.getNickname()))
                councilPlayers.add(pl.getNickname());
        }
        return councilPlayers;
    }

    /**
     * This method calculates the amount of servants that a player needs to perform a tower action
     * @param fm the family member involved in the action
     * @param towerColor the destination tower's color
     * @param floor the destination floor's number
     * @return the amount of servants that the player has to spend to perform the action
     */
    public int servantsForTowerAction(FamilyMember fm,String towerColor, int floor) {
        int actionStr = this.getFmStrength(fm)
                + this.getPlayerFromColor(fm.getOwnerColor()).getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * This method calculates the amount of servants that a player needs to perform a fast tower action
     * @param actionBonus the bonus provided by the action effect
     * @param towerColor the destination tower's color
     * @param floor the destination floor's number
     * @param pl the player performing the fast action
     * @return the amount of servants that the player has to spend to perform the action
     */
    public int servantsForFastTowerAction(int actionBonus, String towerColor, int floor, Player pl) {
        int actionStr = actionBonus
                + pl.getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(pl))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * This method calculates the amount of servants that a player needs to enter the market
     * @param fm the family member involved in the action
     * @return the amount of servants that the player has to spend to perform the action
     */
    public int servantsForMarketAction(FamilyMember fm) {
        int actionStr = this.getFmStrength(fm);
        int servants = MARKET_ACTION_COST - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * this method tell if the user can enter the market
     * @param fm the family member involved in the action
     * @param position the market slot position
     * @return if the player can perform the action
     */
    public boolean isMarketMoveAllowed(FamilyMember fm, int position) {
        Market market = this.getBoard().getMarket();
        return market.isPositionAvailable(position) || (market.isPositionOccupied(position) &&
                playerHasActiveLeader(2, getPlayerFromColor(fm.getOwnerColor())));
    }

    /**
     * This method performs the actual market move
     * @param fm the family member involved in the action
     * @param position the market slot position
     * @param servantsDeployed the servants spent to perform the action
     */
    public void marketMove(FamilyMember fm, int position, int servantsDeployed) {
        Player actor = getPlayerFromColor(fm.getOwnerColor());
        actor.pullFamilyMember(fm.getDiceColor());
        Assets actionCost = new Assets();
        actionCost.addServants(servantsDeployed);
        removeAssetsFromPlayer(actionCost, actor);
        this.board.getMarket().addFamilyMember(fm, position);
        Object bonus = this.board.getMarket().getBonuses(position);
        if (bonus instanceof Integer) {
            controllerCallback.giveCouncilFavors((int) bonus);
        } else if (bonus instanceof Assets) {
            giveAssetsToPlayer((Assets)bonus, actor);
        }
    }

    /**
     * This method calculates the amount of servants the player has to spend to enter the council
     * @param fm the fm involved in the action
     * @return the amount of servants to enter the council
     */
    public int servantsForCouncilAction(FamilyMember fm) {
        int actionStr = this.getFmStrength(fm);
        int servants = COUNCIL_ACTION_COST - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    /**
     * this method performs the actual council move
     * @param fm the fm to put in the council
     * @param servantsDeployed the servants spent to perform the action
     */
    public void councilMove(FamilyMember fm, int servantsDeployed) {
        Player actor = getPlayerFromColor(fm.getOwnerColor());
        actor.pullFamilyMember(fm.getDiceColor());
        board.getCouncil().addFamilyMember(fm);
        Assets actionCost = new Assets();
        actionCost.addServants(servantsDeployed);
        removeAssetsFromPlayer(actionCost, actor);
        giveAssetsToPlayer(board.getCouncil().getCouncilBonus(), actor);
        controllerCallback.giveCouncilFavors(board.getCouncil().getFavorsAmount());
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void removeAssetsFromPlayer(Assets assets, Player pl) {
        pl.setResources(pl.getResources().subtract(assets));
    }

    /**
     * this method gives an amount of assets to the player, it also applies eventual excommunication maluses
     * @param assets the assets that will be given to the player
     * @param pl the recipient player
     */
    public void giveAssetsToPlayer(Assets assets, Player pl) {
        pl.setResources(pl.getResources().add(apllyExcommMalus(assets, pl)));
    }

    /**
     * This method applies a malus to the provided asset, if the player has been excommunicated
     * @param assets the assets to apply the excommunication to
     * @param pl the player to check
     * @return the asset with the excommunication applied
     */
    public Assets apllyExcommMalus(Assets assets, Player pl) {
        Excommunication firstAgeExcomm = board.getExcommunications().get(0);
        if (firstAgeExcomm instanceof AssetsExcommunication && firstAgeExcomm.getExcommunicated().contains(pl.getColor()))
            return assets.subtractToZero(((AssetsExcommunication) firstAgeExcomm).getMalus());
        return assets;
    }

    /**
     * this method gets the assets chosen by the user as council favors and gives them to the player.
     * @param pl the recipient player
     * @param choices the chosen favors
     */
    public void giveFavors(Player pl, ArrayList<Integer> choices) throws ControllerException {
        if (choices.size() > this.board.getCouncil().getFavorBonuses().size())
            throw new ControllerException("Error on giving favors to player, maybe too many?");
        Set<Integer> set = new HashSet<>(choices);
        if(set.size() < choices.size()) throw new ControllerException("Error on giving favors to player, duplicates!");
        choices.forEach(choice -> giveAssetsToPlayer(this.board.getCouncil().getFavorBonuses().get(choice),pl));
    }

    /**
     * this method calculates the single bonuses a player can have at the and of the game.
     * These bonuses are not influenced by previous excommunications (maybe assets)
     */
    public void calcEndGameBonus(Player pl) {
        if (!isPlayerEndCardExcommunicated(pl, GREEN_COLOR)){
            pl.setResources(pl.getResources()
                    .addVictoryPoints(ENDGAME_GREEN_CARDS_VP_BONUS[pl.getCardsOfColor(GREEN_COLOR).size()]));
        }
        if (!isPlayerEndCardExcommunicated(pl, BLUE_COLOR)){
            pl.setResources(pl.getResources()
                    .addVictoryPoints(ENDGAME_BLUE_CARDS_VP_BONUS[pl.getCardsOfColor(BLUE_COLOR).size()]));
        }
        // here we add an assets bonus, other assets except victory points will be counted in the next control
        if (!isPlayerEndCardExcommunicated(pl, PURPLE_COLOR)){
            pl.getCardsOfColor(PURPLE_COLOR).forEach(card -> pl.setResources(
                    pl.getResources().add(((PurpleCard)card).getEndgameBonus())));
        }
        pl.setResources(pl.getResources().addVictoryPoints(pl.getResources().sumAll()/ENDGAME_VP_ASSETS_DIVIDER));
    }

    /**
     * this method is called at the end of the game and adds victory points to the first two players that have
     * the highest battle points (first two on the military track)
     */
    public void applyVpOnBpRank() {
        HashMap<Player, Integer> milTrack = new HashMap<>(players.stream().collect(
                Collectors.toMap (player -> player, player -> player.getResources().getBattlePoints())));
        ArrayList<Integer> bpScores = new ArrayList<>(milTrack.values());
        Collections.sort(bpScores, Collections.reverseOrder());
        ArrayList<Player> firstPlayers = new ArrayList<>(milTrack.entrySet().stream()
                .filter(pl -> pl.getValue().equals(bpScores.get(0)))
                .map(Map.Entry::getKey).collect(Collectors.toList()));
        firstPlayers.forEach(pl -> pl.setResources(pl.getResources().addVictoryPoints(ENDGAME_FIRSTVP_BONUS)));
        if (firstPlayers.size() < 2) {
            ArrayList<Player> secondPlayers = new ArrayList<>(milTrack.entrySet().stream()
                    .filter(pl -> pl.getValue().equals(bpScores.get(1)))
                    .map(Map.Entry::getKey).collect(Collectors.toList()));
            secondPlayers.forEach(pl -> pl.setResources(pl.getResources().addVictoryPoints(ENDGAME_SECONDVP_BONUS)));
        }
    }


    // ------------------------------------------------------------------------ Excommunications

    /**
     * This method returns if a player has enough faith points in order not to be excommunicated
     * @param pl the player
     * @return the answer
     */
    public boolean isNotExcommunicable(Player pl) {
        return pl.getResources().getFaithPoints() >= FIRST_EXCOMM_FP+this.board.getAge()-1;
    }

    /**
     * @return an excommunication based on the game's age
     */
    @JsonIgnore
    public Excommunication getExcommunication(){
        return this.board.getExcommunications().get(this.board.getAge());
    }

    @JsonIgnore
    public Excommunication getExcommunication(int age) {
        return this.board.getExcommunications().stream().filter(excomm -> excomm.getAge() == age)
                .findFirst().orElse(null);
    }

    public void excommunicatePlayer (Player pl) {
        Excommunication ex = this.board.getExcommunicationsByAge(this.board.getAge());
        ex.addExcommunicated(pl);
        if (ex instanceof StrengthsExcommunication) pl.setStrengths(pl.getStrengths()
                .add(((StrengthsExcommunication) ex).getMalus()));
        else if (ex instanceof EndGameAssetsExcommunication) {
            EndGameAssetsExcommunication excomm = (EndGameAssetsExcommunication) ex;
            if (excomm.getProductionCardCostMalus() != null) {
                int costAccumulator = 0;
                for (Card card : pl.getCardsOfColor(YELLOW_COLOR))
                    costAccumulator += (card.getCost().getWood() + card.getCost().getStone());
                pl.setResources(pl.getResources()
                        .subtractToZero(excomm.getProductionCardCostMalus().multiply(costAccumulator)));
            } else if (excomm.getOnAssetsMalus(1) == null) {
                pl.setResources(pl.getResources().subtractToZero(excomm.getOnAssetsMalus(0)
                        .multiply(pl.getResources().sumAll())));
            } else {
                pl.setResources(pl.getResources().subtractToZero(excomm.getOnAssetsMalus(0)
                        .multiply(pl.getResources().divide(excomm.getOnAssetsMalus(1)))));
            }
        }
    }

    @JsonIgnore
    private boolean isPlayerTowerBonusAllowed(Player pl) {
        Excommunication secondAgeExcomm = board.getExcommunications().get(0);
        return !(secondAgeExcomm instanceof AssetsExcommunication &&
                secondAgeExcomm.getExcommunicated().contains(pl.getColor()));
    }

    @JsonIgnore
    private boolean isPlayerServantsExcommunicated(Player pl) {
        Excommunication secondAgeExcomm = board.getExcommunications().get(1);
        return (secondAgeExcomm instanceof ServantsExcommunication &&
                secondAgeExcomm.getExcommunicated().contains(pl.getColor()));
    }

    @JsonIgnore
    public boolean isPlayerRoundExcommunicated(Player pl) {
        Excommunication secondAgeExcomm = board.getExcommunications().get(1);
        return (secondAgeExcomm instanceof RoundExcommunication &&
                secondAgeExcomm.getExcommunicated().contains(pl.getColor()));
    }

    //-------------------------------------------------------------------------------- LEADERS

    @JsonIgnore
    private boolean isPlayerEndCardExcommunicated(Player pl, String color){
        Excommunication endGameExcomm = board.getExcommunications().get(2);
        return (endGameExcomm instanceof EndGameCardsExcommunication &&
                endGameExcomm.getExcommunicated().contains(pl.getColor()) &&
                ((EndGameCardsExcommunication) endGameExcomm).getBlockedCardColor().equals(color));
    }

    public void giveLeaderToPlayer(int leaderId, Player pl) {
        pl.addLeader(Leaders.getLeaderById(leaderId));
    }

    public boolean isLeaderDeployable(int leaderId, Player pl) {
        LeaderCard leader = pl.getLeaderById(leaderId);
        if (leader.isDeployed()) return false;
        if (leader.getAssetsRequirement() != null && leader.getAssetsRequirement().isGreaterOrEqual(pl.getResources())) return false;
        if (leader.getCardsRequirement() != null)
            for (String color : leader.getCardsRequirement().keySet()) {
                if (leader.getCardsRequirement().get(color) > pl.getCardsOfColor(color).size())
                    return false;
                else if (leader.getLeaderCardId() == 16 && leader.getCardsRequirement().get(color) <=
                        pl.getCardsOfColor(color).size())
                    return true;
            }
        return true;
    }

    public void deployLeader(int leaderId, Player pl) {
        pl.getLeaderById(leaderId).setDeployed(true);
    }

    public void discardLeader(int leaderId, Player pl) {
        pl.getLeaderById(leaderId).setDiscarded(true);
    }

    public boolean isLeaderActivable(int leaderId, Player pl) {
        if (!(pl.getLeaderById(leaderId) instanceof ActivableLeader)) return false;
        ActivableLeader leader = (ActivableLeader)pl.getLeaderById(leaderId);
        return (leader.isDeployed() && !leader.isActivated());
    }

    public boolean isLeaderDiscardable(int leaderId, Player pl) {
        return (pl.getLeaderById(leaderId) != null && !pl.getLeaderById(leaderId).isDiscarded());
    }

    public ArrayList<String> getAllDeployedLeaders() {
        ArrayList<String> leaderNames = new ArrayList<>();
        players.forEach(player -> leaderNames.addAll(player.getDeployedLeaders().stream()
                .map(LeaderCard::getCardName).collect(Collectors.toList())));
        return leaderNames;
    }

    public void replaceLeader(LeaderCard leader, Player pl) {
        pl.getLeaderCards().set(pl.getLeaderCards().indexOf(pl.getLeaderById(13)),leader);
    }

    public boolean playerHasActiveLeader(int id, Player pl) {
        LeaderCard leader = pl.getLeaderById(id);
        if (leader == null) return false;
        if (leader instanceof PermanentLeader && leader.isDeployed())
            return true;
        if (leader instanceof ActivableLeader && leader.isDeployed() && ((ActivableLeader) leader).isActivated())
            return true;
        return false;
    }

}