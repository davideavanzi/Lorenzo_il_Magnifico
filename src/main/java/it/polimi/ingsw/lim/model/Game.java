package it.polimi.ingsw.lim.model;
import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.cards.Card;
import it.polimi.ingsw.lim.model.cards.PurpleCard;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.leaders.LeaderCard;
import it.polimi.ingsw.lim.parser.Parser;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

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
        this.controllerCallback = controllerCallback;
        this.randomGenerator = new Random();
    }

    public Game(){
        this.players = new ArrayList<>();
        this.availablePlayerColors = new ArrayList<>(PLAYER_COLORS);
        this.randomGenerator = new Random();
    }


    public void setBoard(Board board){
        this.board = board;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    public void setCardsDeck(CardsDeck cardsDeck){
        this.cardsDeck = cardsDeck;
    }

    public void setAvailablePlayerColors(List<String> availablePlayerColors){
        this.availablePlayerColors = availablePlayerColors;
    }

    public CardsDeck getCardsDeck (){
        return cardsDeck;
    }

    public List<String> getAvailablePlayerColors(){
        return availablePlayerColors;
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
     *
     */
    private List<String> availablePlayerColors;

    /**
     * reference to the controller handling this game instance
     */
    @JsonIgnore
    private GameController controllerCallback;
    @JsonIgnore
    private Random randomGenerator;

    // ############################################################# METHODS AHEAD



    //TODO:implement leadercard effect here
    public int getFmStrength(FamilyMember fm) {
        if (fm.getDiceColor().equals(NEUTRAL_COLOR)) return NEUTRAL_FM_STRENGTH;
        return this.board.getDice().get(fm.getDiceColor())+getPlayerFromColor(fm.getOwnerColor())
                .getStrengths().getDiceBonus().get(fm.getDiceColor());
    }

    /**
     * This method sets up the game after it is created by the constructor.
     */
    public void setUpGame(Parser parsedGame) throws GameSetupException {
        getLog().info("[GAME SETUP BEGIN]");
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
        int i = 0;
        for (Assets bonus : parsedGame.getFaithTrackBonuses())
            this.board.getFaithTrack()[i] = bonus;
        for (i = 1; i <= AGES_NUMBER; i++) {
            this.board.addExcommunication(parsedGame.getExcommunications().get(i)
                    .get(randomGenerator.nextInt(parsedGame.getExcommunications().get(i).size())));
        }
        //TODO: When we allot leadercards?
        /*
         * Distribute initial resources starting from the first player,
         * the following will have a coin more than the player before them.
         */
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
                DICE_COLORS.forEach(color ->
                        player.addFamilyMember((new FamilyMember(color, player.getColor())))));
        this.board.rollDices();
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

    @JsonIgnore
    public int getAge() { return  this.board.getAge(); }
    @JsonIgnore
    public int getTurn() { return  this.board.getTurn(); }

    public ArrayList<Player> getPlayers(){ return this.players; }

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
        //TODO: useless? add servants in the count?
        /*
        if (destination.getActionCost() >
                this.getFmStrength(fm) +
                        getPlayerFromColor(fm.getOwnerColor()).getStrengths().getTowerStrength(towerColor))
            return false; */
        return true;
    }

    /**
     * this method tells if a player (fot from it's family member) can take one more card
     * @param fm
     * @return
     */
     private boolean canPickCard(String towerColor, FamilyMember fm) {
        Player pl = getPlayerFromColor(fm.getOwnerColor());
        if (pl.getCardsOfColor(towerColor).size() >= 6) return false;
        if (pl.getResources().getBattlePoints() >=
                PLAYER_TERRITORIES_REQ[pl.getCardsOfColor(GREEN_COLOR).size()-1])
            return false;
        return true;
     }

    /**
     * This method checks if a specified move into a tower is affordable by the player performing the move.
     * it also checks if is a purple card and has an optional cost to pick the card
     * @param towerColor
     * @param floorNumber
     * @param fm
     * @return this.getPlayerFromColor(fm.getOwnerColor()
     */
    @JsonIgnore
    public boolean isTowerMoveAffordable(String towerColor, int floorNumber, FamilyMember fm) {
        Player pl = this.getPlayerFromColor(fm.getOwnerColor());
        Floor destination = this.getTower(towerColor).getFloor(floorNumber);
        //Checking Costs
        Assets cardCost = destination.getCardSlot().getCost().subtractToZero
                        (pl.getPickDiscount(towerColor));
        Assets additionalCost = new Assets();
        Assets playerAssets = new Assets(pl.getResources());
        if (this.isTowerOccupied(towerColor) && pl.getResources().getCoins() > COINS_TO_ENTER_OCCUPIED_TOWER)
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
        return (actor.getResources().isGreaterOrEqual(cardCost));
    }
    @JsonIgnore
    public boolean isPurpleCardAffordable(PurpleCard card, Player pl){
        return (card.getOptionalBpRequirement() > 0 && pl.getResources().getVictoryPoints() > card.getOptionalBpRequirement());
    }

    /**
     * This method makes the actual tower move
     * @param towerColor
     * @param floorNumber
     * @param fm
     * @param servantsDeployed
     * @param useBp
     * @return the picked card
     */
    public Card towerMove(String towerColor, int floorNumber, FamilyMember fm, int servantsDeployed, boolean useBp) {
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
        if (this.isPlayerTowerBonusAllowed(actor)) giveAssetsToPlayer(destination.getInstantBonus(), actor);
        actor.getResources().subtract(actionCost);
        actor.addCard(card, towerColor);
        return card;
    }

    public void fastTowerMove(String towerColor, int floorNumber, int servantsDeployed, boolean useBp, Player actor,
                              Assets optionalPickDiscount) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floorNumber);
        Card card = destination.pullCard();
        //Action cost is different whether the player wants to pay the card's cost or the purple's card bp cost.
        //TODO: ???? .adds(pp
        Assets actionCost = (useBp) ? new Assets().addServants(((PurpleCard)card).getOptionalBpCost()) :
                new Assets(card.getCost()).addServants(servantsDeployed);
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
     * @param towerColor
     * @param floor
     * @return
     */
    @JsonIgnore
    public boolean isFastTowerMoveAllowed(String towerColor, int floor, Player pl, Assets optionalPickDiscount) {
        Floor destination = this.board.getTowers().get(towerColor).getFloor(floor);
        if (!destination.hasCard()) return false;
        Assets actionCost = destination.getCardSlot().getCost();
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
     * @param fm
     * @return boolean
     */
    @JsonIgnore
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
        //TODO: pick deployed family member?
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
        int baseStrength = (fm == null) ? baseStr : this.getFmStrength(fm);
        int actionCost = (this.board.getHarvest().size() <= PRODUCTION_DEFAULTSPACE_SIZE)
                ? PRODUCTION_DEFAULT_STR : PRODUCTION_STR_MALUS;
        int actionStr = baseStrength +
                getPlayerFromColor(fm.getOwnerColor()).getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
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
        int baseStrength = (fm == null) ? baseStr : this.getFmStrength(fm);
        int actionCost = (this.board.getHarvest().size() <= HARVEST_DEFAULTSPACE_SIZE)
                ? HARVEST_DEFAULT_STR : HARVEST_STR_MALUS;
        int actionStr = baseStrength +
                getPlayerFromColor(fm.getOwnerColor()).getStrengths().getHarvestBonus();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
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
        int baseStr = (fm == null) ? tmpActionStr : this.getFmStrength(fm);
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servantsDeployed /= 2; //it should be always a even number, because the client doubled the amount of servants while deploying
        if (this.board.getHarvest().size() <= HARVEST_DEFAULTSPACE_SIZE) baseStr -= HARVEST_STR_MALUS;
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
    public int calcProductionActionStr(Player actor, FamilyMember fm, int servantsDeployed, int tmpActionStr) {
        int baseStr = (fm == null) ? tmpActionStr : this.getFmStrength(fm);
        if (isPlayerServantsExcommunicated(actor))
            servantsDeployed /= 2;
        if (this.board.getProduction().size() <= PRODUCTION_DEFAULTSPACE_SIZE) baseStr -= PRODUCTION_STR_MALUS;
        return baseStr+servantsDeployed+actor.getStrengths().getProductionBonus();
    }
    @JsonIgnore
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

    @JsonIgnore
    public Tower getTower(String color){
        return this.board.getTowers().get(color);
    }

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

    @JsonIgnore
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
        int actionStr = this.getFmStrength(fm)
                + this.getPlayerFromColor(fm.getOwnerColor()).getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    public int servantsForFastTowerAction(int actionBonus,String towerColor, int floor, Player pl) {
        int actionStr = actionBonus
                + pl.getStrengths().getTowerStrength(towerColor);
        int actionCost = this.board.getTowers().get(towerColor).getFloor(floor).getActionCost();
        int servants = actionCost - actionStr;
        if (isPlayerServantsExcommunicated(pl))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    public int servantsForMarketAction(FamilyMember fm) {
        int actionStr = this.getFmStrength(fm);
        int servants = MARKET_ACTION_COST - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    public boolean isMarketMoveAllowed(FamilyMember fm, int position) {
        Market market = this.getBoard().getMarket();
        return market.isPositionAvailable(position) && market.isPositionOccupied(position);
    }

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

    public int servantsForCouncilAction(FamilyMember fm) {
        int actionStr = this.getFmStrength(fm);
        int servants = COUNCIL_ACTION_COST - actionStr;
        if (isPlayerServantsExcommunicated(getPlayerFromColor(fm.getOwnerColor())))
            servants *= 2;
        return (servants > 0) ? -servants : 0;
    }

    public void councilMove(FamilyMember fm, int servantsDeployed) {
        Player actor = getPlayerFromColor(fm.getOwnerColor());
        actor.pullFamilyMember(fm.getDiceColor());
        Assets actionCost = new Assets();
        actionCost.addServants(servantsDeployed);
        removeAssetsFromPlayer(actionCost, actor);
        giveAssetsToPlayer(board.getCouncil().getCouncilBonus(), actor);
        controllerCallback.giveCouncilFavors(board.getCouncil().getFavorsAmount());
    }

    public Board getBoard() {
        return board;
    }

    public void removeAssetsFromPlayer(Assets assets, Player pl) {
        pl.setResources(pl.getResources().subtract(assets));
    }

    /**
     * this method gives an amount of assets to the player, it also applies eventual excommunication maluses
     * @param assets
     * @param pl
     */
    public void giveAssetsToPlayer(Assets assets, Player pl) {
        //Player pl = getPlayerFromColor(player);
        pl.setResources(pl.getResources().add(apllyExcommMalus(assets, pl)));
    }

    public Assets apllyExcommMalus(Assets assets, Player pl) {
        Excommunication firstAgeExcomm = board.getExcommunications().get(0);
        if (firstAgeExcomm instanceof AssetsExcommunication && firstAgeExcomm.getExcommunicated().contains(pl.getColor()))
            return assets.subtractToZero(((AssetsExcommunication) firstAgeExcomm).getMalus());
        return assets;
    }

    /**
     * this method gets the assets chosen by the user as council favors and gives them to the player.
     * @param pl
     * @param choices
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
        applyVpOnBpRank();
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
        //get players with highest score
        ArrayList<Player> firstPlayers = new ArrayList<Player>(milTrack.entrySet().stream()
                .filter(pl -> pl.getValue().equals(bpScores.get(0)))
                .map(Map.Entry::getKey).collect(Collectors.toList()));
        if (firstPlayers.size() > 1)
            firstPlayers.forEach(pl -> pl.setResources(pl.getResources().addVictoryPoints(ENDGAME_FIRSTVP_BONUS)));
        else {
        ArrayList<Player> secondPlayers = new ArrayList<>(milTrack.entrySet().stream()
                .filter(pl -> pl.getValue().equals(bpScores.get(0)))
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
        Excommunication ex = this.board.getExcommunications().get(this.board.getAge());
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

    @JsonIgnore
    private boolean isPlayerEndCardExcommunicated(Player pl, String color){
        Excommunication endGameExcomm = board.getExcommunications().get(2);
        return (endGameExcomm instanceof EndGameCardsExcommunication &&
                endGameExcomm.getExcommunicated().contains(pl.getColor()) &&
                ((EndGameCardsExcommunication) endGameExcomm).getBlockedCardColor().equals(color));
    }

    public boolean isLeaderDeployable(int leaderId, Player pl) {
        LeaderCard leader = pl.getLeaderById(leaderId);
        if (leader.getAssetsRequirement().isGreaterOrEqual(pl.getResources())) return false;

        for (String color : leader.getCardsRequirement().keySet()) {
            if (leader.getCardsRequirement().get(color) > pl.getCardsOfColor(color).size())
                return false;
            else if (leader.getLeaderCardId() == 16 && leader.getCardsRequirement().get(color) <=
                    pl.getCardsOfColor(color).size())
                return true;
        }
        return true;
    }

    @JsonIgnore
    public ArrayList<FamilyMember> getHarvest() {
        return this.board.getHarvest();
    }
    @JsonIgnore
    public ArrayList<FamilyMember> getProduction() {
        return this.board.getProduction();
    }
}