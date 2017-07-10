package it.polimi.ingsw.lim.controller;

import it.polimi.ingsw.lim.exceptions.BadRequestException;
import it.polimi.ingsw.lim.exceptions.ControllerException;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import it.polimi.ingsw.lim.model.leaders.Leaders;
import it.polimi.ingsw.lim.model.leaders.PermanentLeader;
import it.polimi.ingsw.lim.parser.Parser;
import it.polimi.ingsw.lim.parser.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.model.leaders.Leaders.*;
import static it.polimi.ingsw.lim.utils.Log.getLog;

/**
 * This class is the main game controller.
 */
public class GameController {
    private Game game;
    private Room roomCallback;
    private ArrayList<ArrayList<Object[]>> currentProductionOptions;
    private PendingProduction pendingProduction;
    private Strengths fastActionStr;
    private Assets optPickDiscount;
    private User fastActor;
    private PendingTowerMove pendingTowerMove;
    private ArrayList<String> pendingLeaderCopy;

    /**
     * constructor
     * @param roomCallback the room that the game controller belong to
     */
    public GameController(Room roomCallback) { this.roomCallback = roomCallback; }

    /**
     * dummy constructor
     */
    public GameController() { game = new Game(); }


    public Game getGame(){
        return this.game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    /**
     * This method checks if all parsed data are ok with current game settings.
     * - Development cards number for each color and age = tower_height * turns_per_age
     * - At least one excommunication for every age from first_excomm_fp to ages_number
     * - Tower bonuses consistent with tower heights.
     * - Market bonuses consistent.
     * - Council bonuses consistent.
     * - Faith track bonuses corresponding to faith track length
     * TODO: do we have to check if every card is valid?
     */
    private boolean validateParsedData (Parser parsedGame){
        for (int age = 1; age <= AGES_NUMBER; age++)
            for (String color : DEFAULT_TOWERS_COLORS)
                if((parsedGame.getCard(age, color).size() < TURNS_PER_AGE * TOWER_HEIGHT))
                    return false;
        int excommunicationAge = parsedGame.getExcommunications().size();
        if(excommunicationAge < AGES_NUMBER){
            return false;
        }
        if(parsedGame.getFaithTrackBonuses().length != FAITH_TRACK_LENGTH)
            return false;
        for (String color : DEFAULT_TOWERS_COLORS)
            if(parsedGame.getTowerbonuses(color).length != TOWER_HEIGHT)
                return false;
        int marketSize = parsedGame.getMarketBonuses().length;
        if(
            (marketSize < 2) ||
            (marketSize < 4 && this.roomCallback.getUsersList().size() == 4) ||
            (marketSize < 5 && this.roomCallback.getUsersList().size() == 5)
        )
            return false;
        if(parsedGame.getCouncilFavourBonuses().length < 3){ /* at least 3 slots (the maximum num of different favours that a player can pick) */
            return false;
        }
        return true;
    }

    /**
     * This method creates an empty instance of the game.
     * Room must have players before instantiating game instance
     */
    public void createGame() {
        if(!restartGame(roomCallback)){
            this.game = new Game(this);
            roomCallback.getUsersList().forEach(user ->
                    user.setPlayer(game.addPlayer(user.getUsername())));
            Collections.shuffle(game.getPlayers());
            String defaultPath = "default/";
            Parser parsedGame = new Parser();
            try {
                parsedGame.parser(CONFIGS_PATH + defaultPath);
            } catch (Exception e) {
                getLog().severe("PARSER ERROR:\n" + e.getMessage());
            }
            getLog().info("Validating game data with current settings.");
            if (validateParsedData(parsedGame))
                getLog().severe("[ERROR] - parsed data are incorrect");
            //building game
            getLog().info("Setting up game with parsed data");
            try {
                game.setUpGame(parsedGame);
            } catch (GameSetupException e) {
                getLog().severe(e.getMessage());
            }
            game.setUpTurn();
        }
    }

    /**
     * this method try to resume a game from a file
     * @param roomCallback the room that the game controller belong to
     * @return true if the game is successfully restored false otherwise
     */
    public boolean restartGame(Room roomCallback) {
        try {
            this.game = new Game();
            this.game = Writer.gameReader(roomCallback.getId());
            return true;
        }catch (IOException e){
            getLog().log(Level.SEVERE, "Couldn't restart game!");
            return false;
        }
    }

    /**
     * This methods moves a family member in a tower, checking if the action is legal.
     * @param fm
     * @param towerColor
     * @param floor
     */
    public void moveInTower (FamilyMember fm, String towerColor, int floor, int servantsDeployed)
            throws BadRequestException {
        User actor = roomCallback.getUser(this.game.getPlayerFromColor(fm.getOwnerColor()).getNickname());
        getLog().log(Level.INFO, "Player "+actor.getPlayer().getNickname()+
                " is trying to enter "+towerColor+" tower at floor number "+floor+" with the "
                +fm.getDiceColor()+" family member of value "+this.game.getFmStrength(fm));
        if(this.game.isTowerMoveAllowed(towerColor, floor, fm)){
            if(this.game.isTowerMoveAffordable(towerColor, floor, fm)){
                Card card = this.game.getTower(towerColor).getFloor(floor).getCardSlot();
                boolean cardAffordable = this.game.isCardAffordable(card, actor.getPlayer(), towerColor, null);
                boolean purpleAffordable = card instanceof PurpleCard &&
                        this.game.isPurpleCardAffordable((PurpleCard)card, actor.getPlayer());
                if (cardAffordable || purpleAffordable) {
                    boolean useBp = false;
                    int servantsForTowerAction = this.game.servantsForTowerAction(fm, towerColor, floor);
                    if (servantsDeployed < servantsForTowerAction || servantsDeployed >
                            actor.getPlayer().getResources().getServants())
                        throw new BadRequestException("You did not set the right amount of servants to deploy " +
                                "to perform the action");
                    if (cardAffordable && purpleAffordable) {
                        //let the client choose and save the action state.
                        this.pendingTowerMove = new PendingTowerMove(towerColor, floor, fm, servantsDeployed, actor);
                        actor.askForOptionalBpPick();
                    } else {
                        //in this case only one of the two paying methods is available
                        if (purpleAffordable) useBp = true;
                        this.game.towerMove(towerColor, floor, fm, servantsDeployed, useBp);
                        activatePickedCard(card, actor);
                        roomCallback.broadcastMessage
                                ("Player "+actor.getUsername()+" has picked a card from the "+towerColor+" tower");
                        roomCallback.fmPlaced();
                    }
                } else {
                    getLog().log(Level.INFO, "But the card is not affordable");
                    throw new BadRequestException("the card is not affordable");

                }
            } else {
                getLog().log(Level.INFO, "But the move is not affordable");
                throw new BadRequestException("the tower move is not affordable");

            }
        } else {
            getLog().log(Level.INFO, "But the move is not allowed");
            throw new BadRequestException("the tower move is not allowed");
        }
    }

    /**
     * This method continues a tower move interrupted to ask the actor whether he wanted to pay the ordinary cost or
     * with his battle points
     * @param useBp the decision of the user, it's true if the he wants to pay in battle points
     */
    public void confirmTowerMove(boolean useBp) throws BadRequestException {
        Card card = this.game.towerMove(pendingTowerMove.tower,
                pendingTowerMove.floor,
                pendingTowerMove.fm,
                pendingTowerMove.servantsDeployed, useBp);
        activatePickedCard(card, pendingTowerMove.actor);
        roomCallback.broadcastMessage
                ("Player "+pendingTowerMove.actor.getUsername()+" has picked a card from the "+pendingTowerMove.tower+" tower");
        roomCallback.fmPlaced();
    }

    /**
     * this method activate the immediate effect of a card piked by user
     * @param card the card to be activated
     * @param actor the user who picked the card
     */
    public void activatePickedCard(Card card, User actor){
        //Activate before all resources bonus, then actions.
        card.getImmediateEffects().stream().filter(ie -> ie instanceof AssetsEffect
                || ie instanceof AssetsMultipliedEffect || ie instanceof CardMultipliedEffect
                || ie instanceof CouncilFavorsEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
        card.getImmediateEffects().stream().filter(ie -> ie instanceof ActionEffect)
                .forEach(ie -> EffectHandler.activateImmediateEffect(ie, actor));
        if (card instanceof BlueCard) CardHandler.activateBlueCard((BlueCard)card, actor.getPlayer());
    }

    /**
     * This method performs the actual harvest move.
     * It does not require a reference to the remote user as the harvest has not costs to choose.
     * @param fm the family member deployed for the action
     */
    public void moveInHarvest (FamilyMember fm, int servantsDeployed) throws BadRequestException {
        if(this.game.isHarvestMoveAllowed(fm)){ //If
            Player actor = this.game.getPlayerFromColor(fm.getOwnerColor());
            int servantsForHarvestAction = this.game.servantsForHarvestAction(actor, fm, 0);
            this.game.giveAssetsToPlayer(actor.getDefaultHarvestBonus(), actor);
            if (servantsDeployed < servantsForHarvestAction ||
                    servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants()) {
                throw new BadRequestException("Wrong amount of servants deployed for harvest move");
            }
            this.game.harvestMove(fm, servantsDeployed);
            int actionStrength = game.calcHarvestActionStr(actor, fm, servantsDeployed, 0);
            for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(GREEN_COLOR)) {
                GreenCard activeCard = (GreenCard) card;
                if (activeCard.getActionStrength().getHarvestBonus() <= actionStrength)
                    CardHandler.activateGreenCard(activeCard, roomCallback.getPlayingUser());
            }
            roomCallback.broadcastMessage
                    ("Player "+actor.getNickname()+" performed an harvest action of value: "+actionStrength);
            roomCallback.fmPlaced();
        }
    }

    /**
     * this method is called when a player go to production with a family member, and (optional) using servants
     * @param fm the family member put in production
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if the move is not valid
     */
    public void moveInProduction (FamilyMember fm, int servantsDeployed) throws BadRequestException {
        User actor = roomCallback.getPlayingUser(); //only the playing user can perform the action
        if(!this.game.isProductionMoveAllowed(fm))
            throw new BadRequestException("Production move not allowed");
        int servantsForProductionAction = this.game.servantsForProductionAction(actor.getPlayer(), fm, 0);
        if (servantsDeployed < servantsForProductionAction ||
                servantsDeployed > this.game.getPlayerFromColor(fm.getOwnerColor()).getResources().getServants())
            throw new BadRequestException("Not enough servants to perform production action");
        this.pendingProduction = new PendingProduction();
        pendingProduction.add(actor.getPlayer().getDefaultProductionBonus());
        int actionStrength = game.calcProductionActionStr(actor.getPlayer(), fm, servantsDeployed, 0);
        for (Card card: game.getPlayerFromColor(fm.getOwnerColor()).getCardsOfColor(YELLOW_COLOR)) {
            YellowCard activeCard = (YellowCard) card;
            if (activeCard.getActionStrength().getProductionBonus() <= actionStrength)
                CardHandler.activateYellowCard(activeCard, actor, this);
        }
        if (this.currentProductionOptions.isEmpty()) {
            //don't ask user, complete directly production skipping excomm malus (already given)
            game.productionMove(fm, servantsDeployed);
            actor.getPlayer().setResources(actor.getPlayer().
                    getResources().add(pendingProduction.getAssetsAccumulator()));
            if (pendingProduction.getCouncilFavorsAccumulator() > 0)
                giveCouncilFavors(pendingProduction.getCouncilFavorsAccumulator());
        } else {
            actor.askForProductionOptions(currentProductionOptions);
        }
    }

    /**
     * this method is called when a player go to market with a family member, and (optional) using servants
     * @param fm the family member put in the market
     * @param marketSlot is the position in the market in which the user intend to move to
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if the provided data are not valid to perform the action
     */
    public void moveInMarket(FamilyMember fm, int marketSlot, int servantsDeployed) throws BadRequestException {
        if (!game.isMarketMoveAllowed(fm, marketSlot))
            throw new BadRequestException("Market move not allowed");
        if (game.servantsForMarketAction(fm) > servantsDeployed)
            throw new BadRequestException("Not enough servants to perform market move");
        game.marketMove(fm, marketSlot, servantsDeployed);
        roomCallback.broadcastMessage
                ("Player "+game.getPlayerFromColor(fm.getOwnerColor()).getNickname()+
                        " has entered the market in the slot number "+marketSlot);
        roomCallback.fmPlaced();
    }

    /**
     * this method is called when a player go to council with a family member, and (optional) using servants
     * @param fm the family member put in the council
     * @param servantsDeployed the num of the servants deployed by the user
     * @throws BadRequestException if the provided servants amount is invalid
     */
    public void moveInCouncil(FamilyMember fm, int servantsDeployed) throws BadRequestException {
        if (!(game.servantsForCouncilAction(fm) > servantsDeployed))
            throw new BadRequestException("Not enough servants to enter council");
        game.councilMove(fm, servantsDeployed);
        roomCallback.broadcastMessage
                ("Player "+game.getPlayerFromColor(fm.getOwnerColor()).getNickname()+
                        " has entered the council");
        roomCallback.fmPlaced();
    }

    /**
     * This method completes a production move that was hanging waiting for the user's choice of production options
     * @param choices the list of choices made by the user
     * @throws BadRequestException if the choices are not correct
     */
    public void confirmProduction(ArrayList<Integer> choices) throws BadRequestException {
        if (choices.size() != currentProductionOptions.size())
            throw new BadRequestException("Production move not allowed, wrong choices number!");
        currentProductionOptions.forEach(option -> {
            Assets costOption = ((Assets)option.get(choices.indexOf(option))[0]);
            Object resultOption = option.get(choices.indexOf(option))[1];
                if (costOption.isGreaterOrEqual(roomCallback.getPlayingUser().getPlayer().getResources())){
                    this.game.removeAssetsFromPlayer(costOption, roomCallback.getPlayingUser().getPlayer());
                    if (resultOption instanceof Assets)
                        pendingProduction.add((Assets)option.get(choices.indexOf(option))[1]);
                    if (resultOption instanceof Integer)
                        pendingProduction.add((int)option.get(choices.indexOf(option))[1]);
                }
        });
        fastActor.getPlayer().setResources(fastActor.getPlayer().getResources()
                .add(pendingProduction.getAssetsAccumulator()));
        if (pendingProduction.getCouncilFavorsAccumulator() > 0)
            giveCouncilFavors(pendingProduction.getCouncilFavorsAccumulator());
        roomCallback.broadcastMessage
                ("Player "+fastActor.getUsername()+" performed a production action");
        roomCallback.fmPlaced();
    }

    /**
     * this method adds the available options of a yellow card to choose
     * @param options all available production options of the card
     */
    void addProductionOptions(ArrayList<Object[]> options) {
        this.currentProductionOptions.add(options);
    }

    void addBonusToAccumulator(Assets bonus) {
        this.pendingProduction.add(this.game.apllyExcommMalus(bonus,
                roomCallback.getPlayingUser().getPlayer()));
    }

    void addBonusToAccumulator(int amount) {
        this.pendingProduction.add(amount);
    }



    //FAST ACTIONS:

    void beginFastProduction(Strengths str, User actor) {
        this.fastActionStr = str;
        this.fastActor = actor;
        actor.notifyFastProduction(str.getProductionBonus());
    }

    void beginFastHarvest(Strengths str, User actor) {
        this.fastActionStr = str;
        this.fastActor = actor;
        actor.notifyFastHarvest(str.getProductionBonus());
    }

    /**
     * Tells the user that can perform a fast tower move, sending him all tower colors that the bonus
     * allows to enter.
     * @param str the strength to perform the action
     * @param optPickDiscount an optional pick discount to take the card
     * @param actor the actor that will perform the fast action
     */
    void beginFastTowerMove(Strengths str, Assets optPickDiscount, User actor) {
        this.fastActionStr = str;
        this.optPickDiscount = optPickDiscount;
        this.fastActor = actor;
        Map<String, Integer> activeBonuses = str.getTowerStrength().entrySet().stream()
                .filter(option -> option.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        HashMap<String, Integer> availableTowers = new HashMap<>(activeBonuses);
        actor.notifyFastTowerMove(availableTowers, optPickDiscount);
    }


    public void performFastHarvest(int servantsDeployed, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        int servantsForHarvestAction = this.game.servantsForHarvestAction(actor.getPlayer(), null, fastActionStr.getHarvestBonus());
        if (servantsForHarvestAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForHarvestAction)
            throw new BadRequestException("Wrong amount of servants deployed to perform fast harvest action");
        this.game.giveAssetsToPlayer(fastActor.getPlayer().getDefaultHarvestBonus(), fastActor.getPlayer());
        int actionStrength = game.calcHarvestActionStr(actor.getPlayer(),null, servantsDeployed, fastActionStr.getHarvestBonus());
        for (Card card: fastActor.getPlayer().getCardsOfColor(GREEN_COLOR)) {
            GreenCard activeCard = (GreenCard) card;
            if (activeCard.getActionStrength().getHarvestBonus() <= actionStrength)
                CardHandler.activateGreenCard(activeCard, fastActor);
        }
    }


    public void performFastProduction(int servantsDeployed, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        int servantsForProductionAction = this.game.servantsForProductionAction(actor.getPlayer(), null, fastActionStr.getHarvestBonus());
        if (servantsForProductionAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForProductionAction)
            throw new BadRequestException("Wrong amount of servants deployed to perform fast production action");
        this.pendingProduction = new PendingProduction();
        pendingProduction.add(actor.getPlayer().getDefaultProductionBonus());
        int actionStrength = game.calcProductionActionStr
                (actor.getPlayer(),null, servantsDeployed, fastActionStr.getProductionBonus());
        for (Card card: actor.getPlayer().getCardsOfColor(YELLOW_COLOR)) {
            YellowCard activeCard = (YellowCard) card;
            if (activeCard.getActionStrength().getProductionBonus() <= actionStrength)
                CardHandler.activateYellowCard(activeCard, actor, this);
        }
        if (this.currentProductionOptions.isEmpty()) {
            //don't ask user, complete directly production skipping excomm malus (already given)
            actor.getPlayer().setResources(actor.getPlayer().getResources()
                    .add(pendingProduction.getAssetsAccumulator()));
            game.removeAssetsFromPlayer(new Assets().addServants(servantsDeployed),actor.getPlayer());
            if (pendingProduction.getCouncilFavorsAccumulator() > 0)
                giveCouncilFavors(pendingProduction.getCouncilFavorsAccumulator());
        } else {
            actor.askForProductionOptions(currentProductionOptions);
        }
    }

    /**
     * This method performs a fast tower action, activated by an immediate effect, with the data provided by the user
     * @param servantsDeployed the amount of servants deployed in the action
     * @param towerColor the destination tower color
     * @param floor the destination tower floor
     * @param actor the actor performing the fast tower move
     * @throws BadRequestException
     */
    public void performFastTowerMove(int servantsDeployed, String towerColor, int floor, User actor)
            throws BadRequestException {
        int servantsForTowerAction = this.game.servantsForFastTowerAction(fastActionStr
                .getTowerStrength(towerColor), towerColor, floor, actor.getPlayer());
        if (servantsForTowerAction > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed > fastActor.getPlayer().getResources().getServants() ||
                servantsDeployed < servantsForTowerAction)
            throw new BadRequestException("Wrong amount of servants deployed to perform fast tower move");
        if (!this.game.isFastTowerMoveAllowed(towerColor, floor,actor.getPlayer(), optPickDiscount) ||
                !fastActor.getUsername().equals(actor.getUsername()))
            throw new BadRequestException("Fast tower move not valid");
        Card card = this.game.getTower(towerColor).getFloor(floor).getCardSlot();
        boolean cardAffordable = this.game.isCardAffordable(card, actor.getPlayer(), towerColor, optPickDiscount);
        boolean purpleAffordable = card instanceof PurpleCard &&
                this.game.isPurpleCardAffordable((PurpleCard)card, actor.getPlayer());
        if (cardAffordable || purpleAffordable) {
            boolean useBp = false;
            if (cardAffordable && purpleAffordable) {
                //let the client choose and save the action state.
                this.pendingTowerMove = new PendingTowerMove(towerColor, floor, servantsDeployed, actor);
                actor.askForOptionalBpPick();
            } else {
                //in this case only one of the two paying methods is available, it is automatically chosen
                if (purpleAffordable) useBp = true;
                this.game.fastTowerMove(towerColor, floor, servantsDeployed, useBp, actor.getPlayer(), optPickDiscount);
                activatePickedCard(card, actor);
                roomCallback.broadcastMessage
                        ("Player "+actor.getUsername()+" has picked a card from the "+towerColor+" tower");
            }
        } else {
            throw new BadRequestException("Destination card is not affordable");
        }
    }

    public void deployLeader(int id, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        if (!game.isLeaderDeployable(id, actor.getPlayer()))
            throw new BadRequestException("Leader not deployable");
        if (Leaders.getLeaderById(id) instanceof PermanentLeader)
            LeaderHandler.activatePermanentLeader(id, actor);
        game.deployLeader(id, actor.getPlayer());
    }

    public void activateLeader(int id, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        if (!game.isLeaderActivable(id, actor.getPlayer()))
            throw new BadRequestException("Leader not activable");
        LeaderHandler.activateActivableLeader(id, actor);
    }

    public void discardLeader(int id, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        if (!game.isLeaderDiscardable(id, actor.getPlayer()))
            throw new BadRequestException("Leader not discardable");
        game.discardLeader(id, actor.getPlayer());
        giveCouncilFavors(1);
    }

    public void setPendingLeaderCopy(ArrayList<String> pendingLeaderCopy) {
        this.pendingLeaderCopy = pendingLeaderCopy;
    }

    public void applyLeaderFmBonus(String fmColor, User actor) throws BadRequestException {
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        actor.getPlayer().getDiceOverride().put(fmColor, MONTEFELTRO_FM_BONUS);
    }

    public void applyLeaderCopyChoice(int choice, User actor) throws BadRequestException{
        if (!actor.getUsername().equals(fastActor.getUsername()))
            throw new BadRequestException("Wrong user");
        game.replaceLeader(getLeaderByName(pendingLeaderCopy.get(choice)), actor.getPlayer());
    }

    public void giveLeadersToUser(ArrayList<Integer> leaderIds, User recipient) {
        leaderIds.forEach(id -> game.giveLeaderToPlayer(id, recipient.getPlayer()));
    }

    public void giveCouncilFavors(int amount) {
        roomCallback.getPlayingUser().askForCouncilFavor(amount);
    }

    public void performCfActivation(ArrayList<Integer> choices) throws BadRequestException {
        try {
            this.game.giveFavors(roomCallback.getPlayingUser().getPlayer(), choices);
        } catch (ControllerException e) {
            throw new BadRequestException("Wrong favors choices", e);
        }

    }
    /**
     * This method has to be called at the beginning of the game,
     * otherwise it generates an order that might be incorrect
     * @return the generated list of nicknames, in the correct order
     */
    ArrayList<String> getPlayOrder() {
        return game.getNewPlayerOrder();
    }

    void startNewTurn(){
        this.game.newTurn();
        this.game.setUpTurn();
    }

    int[] getTime() {
        return new int[] {this.game.getAge(), this.game.getTurn()};
    }

    public Board getBoard() {
        return this.game.getBoard();
    }

    public Room getRoomCallback() {
        return roomCallback;
    }

    ArrayList<Player> buildRanking() {
        game.getPlayers().forEach(player -> game.calcEndGameBonus(player));
        game.applyVpOnBpRank();
        HashMap<Player, Integer> playerWithPoints = new HashMap<>(game.getPlayers().stream().collect(
                Collectors.toMap(player -> player, player -> player.getResources().getVictoryPoints())));
        ArrayList<Integer> orderPoints = new ArrayList<>(playerWithPoints.values());
        Collections.sort(orderPoints, Collections.reverseOrder());
        return new ArrayList<>(playerWithPoints.entrySet().stream()
                .filter(pl -> pl.getValue().equals(orderPoints.get(0)))
                .map(Map.Entry::getKey).collect(Collectors.toList()));
    }

    void applyEndGameExcomm() {
        ArrayList<Player> playersToExcomm = new ArrayList<>
                (game.getPlayers().stream().filter(player -> game.isNotExcommunicable(player))
                        .collect(Collectors.toList()));
        playersToExcomm.forEach(player -> game.excommunicatePlayer(player));
        game.getPlayers().forEach(player -> {
            int fp = player.getResources().getFaithPoints();
            player.resetFaithPoints();
            game.giveAssetsToPlayer(game.getBoard().getFaithTrack()[fp], player);
            if (game.playerHasActiveLeader(14, player)) game.giveAssetsToPlayer(SISTOIV_CHURCH_BONUS, player);});
    }

    public void applyExcommunication(ArrayList<Player> toExcommunicate) {
        toExcommunicate.forEach(player -> game.excommunicatePlayer(player));
        ArrayList<Player> notToExcomm = new ArrayList<>(game.getPlayers());
        notToExcomm.removeAll(toExcommunicate);
        notToExcomm.forEach(player -> {
            int fp = player.getResources().getFaithPoints();
            player.resetFaithPoints();
            game.giveAssetsToPlayer(game.getBoard().getFaithTrack()[fp], player);
            if (game.playerHasActiveLeader(14, player)) game.giveAssetsToPlayer(SISTOIV_CHURCH_BONUS, player);});
    }

    private class PendingTowerMove {
        String tower;
        int floor;
        FamilyMember fm;
        int servantsDeployed;
        User actor;

        PendingTowerMove(String tower, int floor, FamilyMember fm, int servantsDeployed, User actor) {
            this.tower = tower;
            this.floor = floor;
            this.fm = fm;
            this.servantsDeployed = servantsDeployed;
            this.actor = actor;
        }

        PendingTowerMove(String tower, int floor, int servantsDeployed, User actor) {
            this.tower = tower;
            this.floor = floor;
            this.servantsDeployed = servantsDeployed;
            this.actor = actor;
        }
    }

    private class PendingProduction {
        Assets assetsAccumulator;
        int councilFavorsAccumulator;

        PendingProduction() {
            this.assetsAccumulator = new Assets();
            this.councilFavorsAccumulator = 0;
        }

        void add(Assets assets) {
            this.assetsAccumulator = this.assetsAccumulator.add(assets);
        }

        void add(int amount) {
            this.councilFavorsAccumulator += amount;
        }

        Assets getAssetsAccumulator() {
            return assetsAccumulator;
        }

        int getCouncilFavorsAccumulator() {
            return councilFavorsAccumulator;
        }
    }
}
