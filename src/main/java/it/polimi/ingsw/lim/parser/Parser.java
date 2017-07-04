package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.exceptions.*;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.parser.KeyConst.*;


/**
 * Created by FabCars. The task of this class is to parse all game info (e.g. cards, excommunications, bonuses) from
 * some files. The directory that contain all the file, is given by the caller.
 */
public class Parser {

    private ArrayList<HashMap<String, ArrayList<Card>>> cards = new ArrayList<>();
    private HashMap<String, Assets[]> boardAssetsBonuses = new HashMap<>();
    private HashMap<Integer, ArrayList<Excommunication>> excommunications = new HashMap<>();
    private ArrayList<Assets> boardPlayersProductionBonus = new ArrayList<>();
    private ArrayList<Assets> boardPlayersHarvestBonus = new ArrayList<>();
    private int councilFavors;
    private Assets councilBonus;
    private Assets startingGameBonus;
    private int timerStartGame;
    private int timerPlayMove;
    private Object[] marketBonuses = new Object[MARKET_MAX_SIZE];

    //setters
    public void setTimers(int timerStartGame, int timerPlayMove){
        this.timerStartGame = timerStartGame;
        this.timerPlayMove = timerPlayMove;
    }

    public void setStartingGameBonus(Assets startingGameBonus){
        this.startingGameBonus = startingGameBonus;
    }

    public void setCards(ArrayList<HashMap<String, ArrayList<Card>>> cards) {
        this.cards = cards;
    }

    public void setBoardAssetsBonuses(String key, Assets[] assets) {
        this.boardAssetsBonuses.put(key, assets);
    }

    public void setBoardAssetsBonuses(HashMap<String, Assets[]> boardAssetsBonuses) {
        this.boardAssetsBonuses = boardAssetsBonuses;
    }

    public void setCouncilFavors(int councilFavors) {
        this.councilFavors = councilFavors;
    }

    public void setCouncilBonus(Assets councilBonus) {
        this.councilBonus = councilBonus;
    }

    public void setExcommunications(HashMap<Integer, ArrayList<Excommunication>> excommunications){
        this.excommunications = excommunications;
    }
    
    public void setBoardPlayersProductionBonus (ArrayList<Assets> boardPlayersProductionBonus){
        this.boardPlayersProductionBonus = boardPlayersProductionBonus;
    }

    public void setBoardPlayersHarvestBonus (ArrayList<Assets> boardPlayersHarvestBonus){
        this.boardPlayersHarvestBonus = boardPlayersHarvestBonus;
    }

    public void setMarketBonuses (Object[] marketBonuses){
        this.marketBonuses = marketBonuses;
    }

    //getters
    public int getTimerStartGame(){
        return this.timerStartGame;
    }

    public int getTimerPlayMove(){
        return this.timerPlayMove;
    }

    public ArrayList<HashMap<String, ArrayList<Card>>> getCards() {
        return this.cards;
    }

    public ArrayList<Card> getCard(int age, String key) {
        return this.cards.get(age - 1).get(key); //age-1 because the first age cards are in the 0th element of the ArrayList
    }

    public HashMap<String, ArrayList<Card>> getCard(int age) {
        return this.cards.get(age - 1);
    }

    public Assets[] getTowerbonuses(String key) {
        return this.boardAssetsBonuses.get(key);
    }

    public Object[] getMarketBonuses() {
        return this.marketBonuses;
    }

    public Assets[] getFaithTrackBonuses() {
        return this.boardAssetsBonuses.get(FAITH_TRACK);
    }

    public Assets[] getCouncilFavourBonuses(){
        return this.boardAssetsBonuses.get(COUNCIL_FAVOUR);
    }

    public int getCouncilFavors() {
        return this.councilFavors;
    }

    public Assets getCouncilBonus() {
        return this.councilBonus;
    }

    public Assets getStartingGameBonus() {
        return this.startingGameBonus;
    }

    public HashMap<Integer, ArrayList<Excommunication>> getExcommunications(){
        return this.excommunications;
    }

    public ArrayList<Assets> getBoardPlayersProductionBonus (){
        return this.boardPlayersProductionBonus;
    }
    
    public ArrayList<Assets> getBoardPlayersHarvestBonus(){
        return  this.boardPlayersHarvestBonus;
    }

    /**
     * this method parse an Assets' type from Json file
     *
     * @param assetsToParse is a JsonNode element "link" to the assets to parse in JSon file
     * @return an Assets' Object type after parsing
     */
    private static Assets parseAssets(JsonNode assetsToParse) {
        return new Assets(
                assetsToParse.path(COIN).asInt(),
                assetsToParse.path(WOOD).asInt(),
                assetsToParse.path(STONE).asInt(),
                assetsToParse.path(SERVANTS).asInt(),
                assetsToParse.path(FAITH_POINTS).asInt(),
                assetsToParse.path(BATTLE_POINTS).asInt(),
                assetsToParse.path(VICTORY_POINTS).asInt()
        );
    }

    /**
     * this method parse an array of assets (into an ArrayList<>)
     * @param arrayAssetsNode is the node "link" to the array in JsonFile
     * @return an ArrayList of Assets
     */
    private static ArrayList<Assets> parseArrayAssets (JsonNode arrayAssetsNode){
        ArrayList<Assets> assets = new ArrayList<>();
        JsonNode tmpArrayAssetsNode = arrayAssetsNode;
        Iterator<JsonNode> arrayAssetsIterator = tmpArrayAssetsNode.getElements();
        while (arrayAssetsIterator.hasNext()) {
            tmpArrayAssetsNode = arrayAssetsIterator.next();
            assets.add(parseAssets(tmpArrayAssetsNode));
        }
        return assets;
    }

    /**
     * this method parse an array of assets (into an array)
     *
     * @param arrayAssetsNode is the node "link" to the array in JsonFile
     * @param assetsNum is the dimension of the array of Assets
     * @return an array of Assets
     */
    private static Assets[] parseArrayAssets(JsonNode arrayAssetsNode, int assetsNum) {
        JsonNode tmpArrayAssetsNode = arrayAssetsNode;
        Assets[] assets = new Assets[assetsNum];
        Iterator<JsonNode> arrayAssetsIterator = tmpArrayAssetsNode.getElements();
        int i = 0;
        while (arrayAssetsIterator.hasNext()) {
            tmpArrayAssetsNode = arrayAssetsIterator.next();
            assets[i] = parseAssets(tmpArrayAssetsNode);
            i++;
        }
        return assets;
    }

    /**
     * this method parses an Strengths' type from Json file
     *
     * @param strengthsToParse is a JsonNode element "link" to the strength in JSon file
     * @return a Strengths' Object type after parsing
     */
    private static Strengths parseStrengths(JsonNode strengthsToParse) {
        return new Strengths(
                strengthsToParse.path(HARVEST_BONUS).asInt(),
                strengthsToParse.path(PRODUCTION_BONUS).asInt(),
                strengthsToParse.path(GREEN_BONUS).asInt(),
                strengthsToParse.path(BLUE_BONUS).asInt(),
                strengthsToParse.path(YELLOW_BONUS).asInt(),
                strengthsToParse.path(PURPLE_BONUS).asInt(),
                strengthsToParse.path(BLACK_BONUS).asInt(),
                strengthsToParse.path(WHITE_DICE_BONUS).asInt(),
                strengthsToParse.path(BLACK_DICE_BONUS).asInt(),
                strengthsToParse.path(ORANGE_DICE_BONUS).asInt()
        );
    }

    /**
     * the task of this method is to parse all board assets bonuses (TowerBonuses, MarketBonuses, FaithTrackBonuses,
     * CouncilFavourBonuses) from a JsonFile
     * @param pathToConfiguratorBonusesAssetsFile is the path to the config bonuses assets file (from the root or from
     *                                            the working directory)
     * @return an HashMap<String, Assets[]> that contain all the bonuses previously listed
     * @throws IOException if in the given path do not exist the file needed
     */
    private static HashMap<String, Assets[]> boardAssetsParser(String pathToConfiguratorBonusesAssetsFile)
            throws IOException{
        HashMap<String, Assets[]> bonuses = new HashMap<>();

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusesAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        bonuses.put(GREEN_COLOR, parseArrayAssets(bonusesNode.path(TOWER_BONUS).path(GREEN_TOWER_BONUS), TOWER_HEIGHT));
        bonuses.put(BLUE_COLOR, parseArrayAssets(bonusesNode.path(TOWER_BONUS).path(BLUE_TOWER_BONUS), TOWER_HEIGHT));
        bonuses.put(YELLOW_COLOR, parseArrayAssets(bonusesNode.path(TOWER_BONUS).path(YELLOW_TOWER_BONUS), TOWER_HEIGHT));
        bonuses.put(PURPLE_COLOR, parseArrayAssets(bonusesNode.path(TOWER_BONUS).path(PURPLE_TOWER_BONUS), TOWER_HEIGHT));
        bonuses.put(BLACK_COLOR, parseArrayAssets(bonusesNode.path(TOWER_BONUS).path(BLACK_TOWER_BONUS), TOWER_HEIGHT));
        bonuses.put(FAITH_TRACK, parseArrayAssets(bonusesNode.path(FAITH_BONUS), FAITH_TRACK_LENGTH));
        bonuses.put(COUNCIL_FAVOUR, parseArrayAssets(bonusesNode.path(COUNCIL_FAVOUR_BONUS), COUNCIL_FAVUORS_TYPES));
        return bonuses;
    }

    private static Object[] marketBonusParser(String pathToConfiguratorBonusesAssetsFile)
            throws IOException{
        Object[] market;
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusesAssetsFile));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        market = parseArrayMarketBonuses(bonusesNode.path(MARKET_BONUS), MARKET_MAX_SIZE);
        return market;
    }

    private static Object[] parseArrayMarketBonuses(JsonNode marketToParse, int marketSize){
        Object[] market = new Object[marketSize];
        Iterator<JsonNode> arrayMarketIterator = marketToParse.getElements();
        int i = 0;
        while (arrayMarketIterator.hasNext()) {
            marketToParse = arrayMarketIterator.next();
            if(marketToParse.path(COUNCIL_BONUS).isInt()){
                market[i] = marketToParse.path(COUNCIL_BONUS).asInt();
            }
            else {
                market[i] = parseAssets(marketToParse);
            }
            i++;
        }
        return market;
    }

    /**
     * this method parse an ArrayList of ImmediateEffect from Json file
     *
     * @param immediateEffectType is a JsonNode element "link" to the immediateEffectType in JSon file
     * @return an ArrayList of ImmediateEffect after parsing
     */
    private static ArrayList<ImmediateEffect> parseImmediateEffect(JsonNode immediateEffectType) {
        //use to check if an ImmediateEffect exists
        boolean immediateEffectExist = false;

        ArrayList<ImmediateEffect> immediateEffects = new ArrayList<>();

        //check the type of the immediateEffect
        if (immediateEffectType.path(ACTION_EFFECT).isContainerNode()) {
            immediateEffectExist = true;

            Strengths tmpActionStrengths = new Strengths();
            if (immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_STRENGTH).isContainerNode()) {
                JsonNode actionEffectStrength = immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_STRENGTH);
                tmpActionStrengths = parseStrengths(actionEffectStrength);
            }

            Assets tmpActionEffectDiscount = new Assets();
            if (immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_ASSETS).isContainerNode()) {
                JsonNode actionEffectDiscount = immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_ASSETS);
                tmpActionEffectDiscount = parseAssets(actionEffectDiscount);
            }

            ActionEffect actionEffect = new ActionEffect(tmpActionStrengths, tmpActionEffectDiscount);
            immediateEffects.add(actionEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path(ASSETS_EFFECT).isContainerNode()) {
            immediateEffectExist = true;

            JsonNode assetsEffectBonus = immediateEffectType.path(ASSETS_EFFECT).path(ASSETS_EFFECT_BONUS);
            Assets tmpAssetsEffectBonus = parseAssets(assetsEffectBonus);

            AssetsEffect assetsEffect = new AssetsEffect(tmpAssetsEffectBonus);
            immediateEffects.add(assetsEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path(COUNCIL_FAVOURS_EFFECT_AMOUNT).isInt()) {
            immediateEffectExist = true;

            int tmpCouncilFavorsEffectAmount = immediateEffectType.path(COUNCIL_FAVOURS_EFFECT_AMOUNT).asInt();

            CouncilFavorsEffect councilFavorsEffect = new CouncilFavorsEffect(tmpCouncilFavorsEffectAmount);
            immediateEffects.add(councilFavorsEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path(CARD_MULTIPLIED_EFFECT).isContainerNode()) {
            immediateEffectExist = true;

            JsonNode cardMultipliedEffectBonus = immediateEffectType.path(CARD_MULTIPLIED_EFFECT).path(CARD_MULTIPLIED_EFFECT_BONUS);
            Assets tmpCardMultipliedEffectBonus = parseAssets(cardMultipliedEffectBonus);

            JsonNode cardMultipliedEffectMultiplierColor = immediateEffectType.path(CARD_MULTIPLIED_EFFECT).path(CARD_MULTIPLIED_EFFECT_MULTIPLIER_COLOR);
            String tmpCardMultipliedEffectMultiplierColor = cardMultipliedEffectMultiplierColor.asText();

            CardMultipliedEffect cardMultipliedEffect = new CardMultipliedEffect(tmpCardMultipliedEffectBonus, tmpCardMultipliedEffectMultiplierColor);
            immediateEffects.add(cardMultipliedEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path(ASSETS_MULTIPLIED_EFFECT).isContainerNode()) {
            immediateEffectExist = true;

            JsonNode assetsMultipliedEffectBonus = immediateEffectType.path(ASSETS_MULTIPLIED_EFFECT).path(ASSETS_MULTIPLIED_EFFECT_BONUS);
            Assets tmpAssetsMultipliedEffectBonus = parseAssets(assetsMultipliedEffectBonus);

            JsonNode assetsMultipliedEffectMultiplier = immediateEffectType.path(ASSETS_MULTIPLIED_EFFECT).path(ASSETS_MULTIPLIED_EFFECT_MULTIPLIER);
            Assets tmpAssetsMultipliedEffectMultiplier = parseAssets(assetsMultipliedEffectMultiplier);

            AssetsMultipliedEffect assetsMultipliedEffect = new AssetsMultipliedEffect(tmpAssetsMultipliedEffectBonus, tmpAssetsMultipliedEffectMultiplier);
            immediateEffects.add(assetsMultipliedEffect);
        }
        else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }
        if (!(immediateEffectExist)) {
            //TODO: set immediate effect to NULL
        }
        return immediateEffects;
    }

    /**
     * the task of this method is to parse a GreenCard from a Json file
     * @param cardName is the name of the card (parsed previously)
     * @param cardAge is the age of the card (parsed previously)
     * @param tmpCardAssetsCost is the cost of the card (parsed previously)
     * @param immediateEffects is an ArrayList that contain all the immediate effect of the card (parsed previously)
     * @param cardNode is the node where the GreenCard details starts in JsonFile
     * @return a new built GreenCard
     */
    private static GreenCard parseGreenCard
    (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode){
        Assets tmpGreenHarvestResult = new Assets();
        if (cardNode.path(GREEN_HARVEST_RESULT).isContainerNode()) {
            JsonNode greenHarvestResult = cardNode.path(GREEN_HARVEST_RESULT);
            tmpGreenHarvestResult = parseAssets(greenHarvestResult);
        }

        Strengths tmpGreenActionStrengths = new Strengths();
        if (cardNode.path(GREEN_ACTION_STRENGTHS).isContainerNode()) {
            JsonNode greenActionStrengths = cardNode.path(GREEN_ACTION_STRENGTHS);
            tmpGreenActionStrengths = parseStrengths(greenActionStrengths);
        }

        return new GreenCard(
                cardName,
                cardAge,
                tmpCardAssetsCost,
                immediateEffects,
                tmpGreenHarvestResult,
                tmpGreenActionStrengths
        );
    }

    /**
     * the task of this method is to parse a BlueCard from a Json file
     * @param cardName is the name of the card (parsed previously)
     * @param cardAge is the age of the card (parsed previously)
     * @param tmpCardAssetsCost is the cost of the card (parsed previously)
     * @param immediateEffects is an ArrayList that contain all the immediate effect of the card (parsed previously)
     * @param cardNode is the node where the BlueCard details starts in JsonFile
     * @return a new built BlueCard
     */
    private static BlueCard parseBlueCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode) {
        Strengths tmpBluePermanentBonus = new Strengths();
        if (cardNode.path(BLUE_PERMANENT_BONUS).isContainerNode()) {
            JsonNode bluePermanentBonus = cardNode.path(BLUE_PERMANENT_BONUS);
            tmpBluePermanentBonus = parseStrengths(bluePermanentBonus);
        }

        Assets tmpBlueGreenDiscount = new Assets();
        if (cardNode.path(BLUE_GREEN_DISCOUNT).isContainerNode()) {
            JsonNode blueGreenDiscount = cardNode.path(BLUE_GREEN_DISCOUNT);
            tmpBlueGreenDiscount = parseAssets(blueGreenDiscount);
        }

        Assets tmpBlueBlueDiscount = new Assets();
        if (cardNode.path(BLUE_BLUE_DISCOUNT).isContainerNode()) {
            JsonNode blueBlueDiscount = cardNode.path(BLUE_BLUE_DISCOUNT);
            tmpBlueBlueDiscount = parseAssets(blueBlueDiscount);
        }

        Assets tmpBlueYellowDiscount = new Assets();
        if (cardNode.path(BLUE_YELLOW_DISCOUNT).isContainerNode()) {
            JsonNode blueYellowDiscount = cardNode.path(BLUE_YELLOW_DISCOUNT);
            tmpBlueYellowDiscount = parseAssets(blueYellowDiscount);
        }

        Assets tmpBluePurpleDiscount = new Assets();
        if (cardNode.path(BLUE_PURPLE_DISCOUNT).isContainerNode()) {
            JsonNode bluePurpleDiscount = cardNode.path(BLUE_PURPLE_DISCOUNT);
            tmpBluePurpleDiscount = parseAssets(bluePurpleDiscount);
        }

        Assets tmpBlueBlackDiscount = new Assets();
        if (cardNode.path(BLUE_BLACK_DISCOUNT).isContainerNode()) {
            JsonNode blueBlackDiscount = cardNode.path(BLUE_BLACK_DISCOUNT);
            tmpBlueBlackDiscount = parseAssets(blueBlackDiscount);
        }

        boolean tmpBlueTowerBonusAllowed = true;
        if (cardNode.path(BLUE_TOWER_BONUS_ALLOWED).isBoolean()) {
            tmpBlueTowerBonusAllowed = cardNode.path(BLUE_TOWER_BONUS_ALLOWED).asBoolean();
        }

        return new BlueCard(
                cardName,
                cardAge,
                tmpCardAssetsCost,
                immediateEffects,
                tmpBluePermanentBonus,
                tmpBlueGreenDiscount,
                tmpBlueBlueDiscount,
                tmpBlueYellowDiscount,
                tmpBluePurpleDiscount,
                tmpBlueBlackDiscount,
                tmpBlueTowerBonusAllowed
        );
    }

    /**
     * the task of this method is to parse a YellowCard from a Json file
     * @param cardName is the name of the card (parsed previously)
     * @param cardAge is the age of the card (parsed previously)
     * @param tmpCardAssetsCost is the cost of the card (parsed previously)
     * @param immediateEffects is an ArrayList that contain all the immediate effect of the card (parsed previously)
     * @param cardNode is the node where the YellowCard details starts in JsonFile
     * @return a new built YellowCard
     */
    private static YellowCard parseYellowCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode) {
        ArrayList<Assets> tmpYellowProductionCostList = new ArrayList<>();
        ArrayList<Assets> tmpYellowProductionResultList = new ArrayList<>();
        if(cardNode.path(YELLOW_PRODUCTION).isContainerNode()) {
            JsonNode yellowProductionNode = cardNode.path(YELLOW_PRODUCTION);
            Iterator<JsonNode> yellowProductionEffectIterator = yellowProductionNode.getElements();

            while (yellowProductionEffectIterator.hasNext()) {
                yellowProductionNode = yellowProductionEffectIterator.next();

                if (yellowProductionNode.path(YELLOW_PRODUCTION_COST).isContainerNode()) {
                    JsonNode yellowProductionCost = yellowProductionNode.path(YELLOW_PRODUCTION_COST);
                    Assets tmpYellowProductionCost = parseAssets(yellowProductionCost);
                    tmpYellowProductionCostList.add(tmpYellowProductionCost);
                }
                if (yellowProductionNode.path(YELLOW_PRODUCTION_RESULT).isContainerNode()) {
                    JsonNode yellowProductionResult = yellowProductionNode.path(YELLOW_PRODUCTION_RESULT);
                    Assets tmpYellowProductionResult = parseAssets(yellowProductionResult);
                    tmpYellowProductionResultList.add(tmpYellowProductionResult);
                }
            }
        }
        Strengths tmpYellowActionStrengths = new Strengths();
        if (cardNode.path(YELLOW_ACTION_STRENGTHS).isContainerNode()) {
            JsonNode yellowActionStrengths = cardNode.path(YELLOW_ACTION_STRENGTHS);
            tmpYellowActionStrengths = parseStrengths(yellowActionStrengths);
        }

        String tmpYellowBonusMultiplier = ""; //todo empty string costante?
        if (cardNode.path(YELLOW_BONUS_MULTIPLIER).isTextual()) {
            tmpYellowBonusMultiplier = cardNode.path(YELLOW_BONUS_MULTIPLIER).asText();
        }

        return new YellowCard(
                cardName,
                cardAge,
                tmpCardAssetsCost,
                immediateEffects,
                tmpYellowProductionCostList,
                tmpYellowProductionResultList,
                tmpYellowActionStrengths,
                tmpYellowBonusMultiplier
        );
    }

    /**
     * the task of this method is to parse a PurpleCard from a Json file
     * @param cardName is the name of the card (parsed previously)
     * @param cardAge is the age of the card (parsed previously)
     * @param tmpCardAssetsCost is the cost of the card (parsed previously)
     * @param immediateEffects is an ArrayList that contain all the immediate effect of the card (parsed previously)
     * @param cardNode is the node where the PurpleCard details starts in JsonFile
     * @return a new built PurpleCard
     */
    private static PurpleCard parsePurpleCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode) {
        JsonNode purpleEndGameBonus = cardNode.path(PURPLE_END_GAME_BONUS);
        Assets tmpPurpleEndGameBonus = parseAssets(purpleEndGameBonus);

        int tmpPurpleOptionalBattlePointsRequirement = 0;
        if (cardNode.path(PURPLE_OPTIONAL_BATTLE_POINTS_REQUIREMENT).isInt()) {
            tmpPurpleOptionalBattlePointsRequirement = cardNode.path(PURPLE_OPTIONAL_BATTLE_POINTS_REQUIREMENT).asInt();
        }

        int tmpPurpleOptionalBattlePointsCost = 0;
        if (cardNode.path(PURPLE_OPTIONAL_BATTLE_POINTS_COST).isInt()) {
            tmpPurpleOptionalBattlePointsCost = cardNode.path(PURPLE_OPTIONAL_BATTLE_POINTS_COST).asInt();
        }

       return new PurpleCard(
                cardName,
                cardAge,
                tmpCardAssetsCost,
                immediateEffects,
                tmpPurpleEndGameBonus,
                tmpPurpleOptionalBattlePointsRequirement,
                tmpPurpleOptionalBattlePointsCost
        );
    }

    /**
     * the task of this method is to parse a BlackCard from a Json file
     * @param cardName is the name of the card (parsed previously)
     * @param cardAge is the age of the card (parsed previously)
     * @param tmpCardAssetsCost is the cost of the card (parsed previously)
     * @param immediateEffects is an ArrayList that contain all the immediate effect of the card (parsed previously)
     * @return a new built BlackCard
     */
    private static BlackCard parseBlackCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects) {
        return new BlackCard(
                cardName,
                cardAge,
                tmpCardAssetsCost,
                immediateEffects
        );
    }

    /**
     * this method get a path to the configurator card file and return an ArrayList which contain all game's card.
     * The single card is built using the constructor of the single card type (some cards instance are nullable others
     * not nullable (e.g. name, age, color))
     * @param pathToConfiguratorCardFile is the path to the config card file (from the root or from the working
     *                                   directory)
     * @return an ArrayList of card containing all game's card
     * @throws IOException if in the given path do not exist the file needed
     * @throws InvalidCardException if even just one of the not nullable variables is null or invalid
     */
    private static ArrayList<HashMap<String, ArrayList<Card>>> cardParser(String pathToConfiguratorCardFile)
            throws IOException, InvalidCardException{

        //the ArrayList where store all the game's card
        ArrayList<HashMap<String, ArrayList<Card>>> cards = new ArrayList<>();

        //creating hashmaps
        for (int i = 0; i < AGES_NUMBER; i++) {
            HashMap<String, ArrayList<Card>> tmpCards = new HashMap<>();
            tmpCards.put(GREEN_COLOR, new ArrayList<>());
            tmpCards.put(BLUE_COLOR, new ArrayList<>());
            tmpCards.put(YELLOW_COLOR, new ArrayList<>());
            tmpCards.put(PURPLE_COLOR, new ArrayList<>());
            tmpCards.put(BLACK_COLOR, new ArrayList<>());
            cards.add(i, tmpCards);
        }

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorCardFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode cardNode = rootNode.path(CARD);
        Iterator<JsonNode> cardIterator = cardNode.getElements();

        //start to parse one by one the card
        while (cardIterator.hasNext()) {

            //get all info from file
            cardNode = cardIterator.next();
            if (!(cardNode.path(CARD_NAME).isTextual()) || (cardNode.path(CARD_NAME).asText().isEmpty())) {
                throw new InvalidCardException("Card Name is not nullable");
            }
            JsonNode cardName = cardNode.path(CARD_NAME);
            if (!(cardNode.path(CARD_AGE).isInt())) {
                throw new InvalidCardException("Card Age is not nullable");
            }
            JsonNode cardAge = cardNode.path(CARD_AGE);
            JsonNode cardType = cardNode.path(CARD_TYPE);
            if (!(cardNode.path(CARD_TYPE).isTextual())) {
                throw new InvalidCardException("Card Type is not nullable");
            }
            Assets tmpCardAssetsCost = new Assets();
            if (cardNode.path(CARD_ASSETS_COST).isContainerNode()) {
                JsonNode cardAssetsCost = cardNode.path(CARD_ASSETS_COST);
                tmpCardAssetsCost = parseAssets(cardAssetsCost);
            }

            ArrayList<ImmediateEffect> immediateEffects = new ArrayList<>();
            if (cardNode.path(CARD_IMMEDIATE_EFFECT).isContainerNode()) {
                JsonNode cardImmediateEffect = cardNode.path(CARD_IMMEDIATE_EFFECT);
                immediateEffects = parseImmediateEffect(cardImmediateEffect);
            }

            //select the constructor of card in base of the cardType (green,blue,yellow,purple,black)
            switch (cardType.asText()) {
                case GREEN_CARD:
                    GreenCard greenCard = parseGreenCard
                            (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(GREEN_COLOR).add(greenCard);
                    break;
                case BLUE_CARD:
                    BlueCard blueCard = parseBlueCard
                            (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(BLUE_COLOR).add(blueCard);
                    break;
                case YELLOW_CARD:
                    YellowCard yellowCard = parseYellowCard
                        (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(YELLOW_COLOR).add(yellowCard);
                    break;
                case PURPLE_CARD:
                    PurpleCard purpleCard = parsePurpleCard
                            (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(PURPLE_COLOR).add(purpleCard);
                    break;

                case BLACK_CARD:
                    BlackCard blackCard = parseBlackCard(cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects);
                    cards.get(cardAge.asInt() - 1).get(BLACK_COLOR).add(blackCard);
                    break;

                default:
                    throw new InvalidCardException("Card Type is not recognized");
             }
        }

        return cards;
    }

    /**
     * the task of this method is to parse the assets given to a player that "goes" to the council (NOT the favour, but
     * only the bonuses given in addition to the favour)
     * @param pathToConfiguratorBonusAssetsFile is the path to the config bonuses assets file (from the root or from the
     *                                          working directory)
     * @return an Assets Object that contain the bonus previously described
     * @throws IOException if in the given path do not exist the file needed
     */
    private static Assets parseCouncilBonus(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets councilBonus;
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        councilBonus = parseAssets(bonusesNode.path(COUNCIL_BONUS));
        return councilBonus;
    }

    /**
     * the task of this method is to take from a JsonFile the number (int) of the favour taken by a player that "goes"
     * to the council (ONLY the number of favours, NOT the favours)
     * to the council
     * @param pathToConfiguratorBonusAssetsFile is the path to the config bonuses assets file (from the root or from the
     *                                          working directory)
     * @return an int (the number of he favour taken by a player that "goes" to the council)
     * @throws IOException if in the given path do not exist the file needed
     */
    private static int parseCouncilFavours(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        int councilFavours;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        councilFavours = bonusesNode.path(COUNCIL_FAVOURS).asInt();

        return councilFavours;
    }

    /**
     * the task of this method is to parse starting game bonus (the bonus given to a player when the game starts)
     * @param pathToConfiguratorBonusAssetsFile is the path to the config bonuses assets file (from the root or from the
     *                                          working directory)
     * @return an Assets type (the bonus given to a player when the game starts)
     * @throws IOException if in the given path do not exist the file needed
     */
    private static Assets parseStartingGameBonus (String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets startingGameBonus;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        startingGameBonus = parseAssets(bonusesNode.path(STARTING_GAME_BONUS));
        return startingGameBonus;
    }

    /**
     * the task of this method is to parse one single excommunication (the excommunicationType is not nullable)
     * @param excommunicationNode is the "link" to the single excommunication in JsonFile
     * @return the excommunication type selected based on the type indicated in the JsonFile
     * @throws InvalidExcommunicationException if even just one of the not nullable variables is null or invalid
     */
    private static Excommunication parseSingleExcommunication (JsonNode excommunicationNode)
            throws InvalidExcommunicationException{
        if(!(excommunicationNode.path(EXCOMMUNICATION_TYPE).isTextual())){
            throw new InvalidExcommunicationException("Excommunication Type is not nullable");
        }
        switch (excommunicationNode.path(EXCOMMUNICATION_TYPE).asText()){
            case ASSETS_MALUS_EXCOMMUNICATION:
                Assets tmpAssetsMalus = parseAssets(excommunicationNode.path(ASSETS_MALUS_EXCOMMUNICATION));
                return new AssetsExcommunication(tmpAssetsMalus);
            case STRENGTH_MALUS_EXCOMMUNICATION:
                Strengths tmpStrengthMalus = parseStrengths(excommunicationNode.path(STRENGTH_MALUS_EXCOMMUNICATION));
                return new StrengthsExcommunication(tmpStrengthMalus);
            case MARKET_EXCOMMUNICATION:
                return new MarketExcommunication();
            case SERVANTS_EXCOMMUNICATION:
                return new ServantsExcommunication();
            case TURN_EXCOMMUNICATION:
                return new TurnExcommunication();
            case END_GAME_EXCOMMUNICATION:
                String tmpBlockedCardColor = excommunicationNode.path(END_GAME_EXCOMMUNICATION).asText();
                Assets tmpProductionCardCostMalus = parseAssets
                        (excommunicationNode.path(PRODUCTION_CARD_COST_MALUS));
                Assets[] tmpOnAssetsMalus = parseArrayAssets
                        (excommunicationNode.path(ON_ASSETS_MALUS),2);//TODO modificare il numero di slot dell'array
                return new EndGameExcommunication(tmpBlockedCardColor, tmpProductionCardCostMalus, tmpOnAssetsMalus);
            default:
                throw new InvalidExcommunicationException("Excommunication Type is not recognized");
        }
    }

    /**
     * the task of this method is to parse an array of excommunications (the array contains all the excommunication of
     * one age)
     * @param arrayExcommunicationNode is the "link" to array of excommunications in JsonFile
     * @return an ArrayList contain all the excommunication of that age
     * @throws InvalidExcommunicationException if even just one of the not nullable variables is null or invalid
     */
    private static ArrayList<Excommunication> parseArrayExcommunication (JsonNode arrayExcommunicationNode)
            throws InvalidExcommunicationException {
        JsonNode tmpArrayExcommunicationNode = arrayExcommunicationNode;
        ArrayList<Excommunication> tmpArrayExcommunication = new ArrayList<>();
        Iterator<JsonNode> arrayExcommunicationIterator = tmpArrayExcommunicationNode.getElements();
        while(arrayExcommunicationIterator.hasNext()){
            tmpArrayExcommunicationNode = arrayExcommunicationIterator.next();
            Excommunication tmpSingleExcommunication = parseSingleExcommunication(tmpArrayExcommunicationNode);
            tmpArrayExcommunication.add(tmpSingleExcommunication);
        }
        return tmpArrayExcommunication;
    }

    /**
     * the task of this method is to parse all the game excommunications (and create an HashMap of excommunications
     * where the key is the Integer that indicate the age and the value is an ArrayList of Excommunication contain all
     * the excommunications of that age)
     * @param pathToExcommunicationsConfiguratorFile is the path to the config excommunications file (from the root or
     *                                               from the working directory)
     * @return an HashMap of excommunications
     *         (where the key is the Integer that indicate the age and the value is an ArrayList of Excommunication
     *         contain all the excommunications of that age)
     * @throws IOException if in the given path do not exist the file needed
     * @throws InvalidExcommunicationException if even just one of the not nullable variables is null or invalid
     */
    private static HashMap<Integer, ArrayList<Excommunication>> parseExcommunications (String pathToExcommunicationsConfiguratorFile)
            throws IOException, InvalidExcommunicationException {
        HashMap<Integer, ArrayList<Excommunication>> tmpExcommunications = new HashMap<>();
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToExcommunicationsConfiguratorFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the excommunicationNode and the excommunicationIterator (used for iterating through the JSon's array of Excommunication)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode excommunicationNode = rootNode.path(EXCOMMUNICATIONS);
        Iterator<JsonNode> excommunicationIterator = excommunicationNode.getElements();

        int i = 0;
        while (excommunicationIterator.hasNext()) {
            excommunicationNode = excommunicationIterator.next();
            ArrayList<Excommunication> tmpExcommunicationAge = parseArrayExcommunication (excommunicationNode);
            i++;
            tmpExcommunications.put(i, tmpExcommunicationAge);
        }
        return tmpExcommunications;
    }

    /**
     * the task of this method is to parse the assets of the player board (production bonus)
     * @param pathToConfiguratorBonusAssetsFile is the path to the config bonus assets file (from the root or
     *                                          from the working directory)
     * @return ArrayList of assets containing the player board assets bonus
     * @throws IOException if in the given path do not exist the file needed
     */
    private static ArrayList<Assets> parseBoardPlayerProductionBonus (String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);

        return parseArrayAssets(bonusesNode.path(BOARD_PLAYER_PRODUCTION_BONUS));
    }

    /**
     * the task of this method is to parse the assets of the player board (harvest bonus)
     * @param pathToConfiguratorBonusAssetsFile is the path to the config bonus assets file (from the root or
     *                                          from the working directory)
     * @return ArrayList of assets containing the player board assets bonus
     * @throws IOException if in the given path do not exist the file needed
     */
    private static ArrayList<Assets> parseBoardPlayerHarvestBonus (String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);

        return parseArrayAssets(bonusesNode.path(BOARD_PLAYER_HARVEST_BONUS));
    }

    /**
     * the task of this method is to parse a timer (from a Json File). this timer will be used when a new player create
     * a new room (is the waiting time for the new player that wants to join this room). it must be at least 60 second
     * @param pathToConfiguratorTimersFile is the path to the config timers file (from the root or
     *                                      from the working directory)
     * @return an int (value of the timer in second)
     * @throws IOException if in the given path do not exist the file needed
     * @throws InvalidTimerException if the time is not specified or invalid
     */
    private static int parseTimerStartGame (String pathToConfiguratorTimersFile)
            throws IOException, InvalidTimerException{
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorTimersFile));

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode timersNode = rootNode.path(TIMERS);
        if(timersNode.path(START_GAME_TIMER).isInt()){
            //by default at least 60 seconds
            if(timersNode.path(START_GAME_TIMER).asInt() >= 60) {
                return timersNode.path(START_GAME_TIMER).asInt();
            }
        }
        throw new InvalidTimerException("Start Game Timer invalid");
    }

    /**
     * the task of this method is to parse a timer (from a Json File). this timer will be used during the turn of a
     * player (is the maximum time to play a move). it must be at least 60 second.
     * @param pathToConfiguratorTimersFile is the path to the config timers file (from the root or
     *                                      from the working directory)
     * @return an int (value of the timer in second)
     * @throws IOException if in the given path do not exist the file needed
     * @throws InvalidTimerException if the time is not specified or invalid
     */
    private static int parseTimerPlayMove (String pathToConfiguratorTimersFile)
            throws IOException, InvalidTimerException {
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorTimersFile));

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode timersNode = rootNode.path(TIMERS);
        if (timersNode.path(PLAY_MOVE_TIMER).isInt()) {
            //by default at least 60 seconds
            if (timersNode.path(PLAY_MOVE_TIMER).asInt() >= 60) {
                return timersNode.path(PLAY_MOVE_TIMER).asInt();
            }
        }
        throw new InvalidTimerException("Play Move Timer invalid");
    }

    /**
     * @param pathToDirectory
     * @return a parsed game (object Parser)
     * @throws IOException if in the given path do not exist the file needed
     * @throws InvalidCardException if even just one of the not nullable variables is null or invalid
     * @throws InvalidExcommunicationException if even just one of the not nullable variables is null or invalid
     * @throws InvalidTimerException if even just one of the timer is not valid (or null)
     */
    public Parser parser(String pathToDirectory)
            throws IOException, InvalidCardException, InvalidExcommunicationException, InvalidTimerException{
        getLog().info("[PARSER]: Try to parse Cards from file: ".concat(pathToDirectory).concat(CONFIGURATOR_CARD_FILE_NAME));
        this.setCards(cardParser(pathToDirectory.concat(CONFIGURATOR_CARD_FILE_NAME)));
        getLog().info("[PARSER]: Cards parsed.");
        getLog().info("[PARSER]: Try to parse Assets Bonuses from file: ".concat(pathToDirectory).concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME));
        this.setBoardAssetsBonuses(boardAssetsParser(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setCouncilBonus(parseCouncilBonus(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setCouncilFavors(parseCouncilFavours(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setStartingGameBonus(parseStartingGameBonus(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setBoardPlayersProductionBonus(parseBoardPlayerProductionBonus(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setBoardPlayersHarvestBonus(parseBoardPlayerHarvestBonus(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        this.setMarketBonuses(marketBonusParser(pathToDirectory.concat(CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        getLog().info("[PARSER]: Assets Bonuses parsed.");
        getLog().info("[PARSER]: Try to parse Excommunication from file: ".concat(pathToDirectory).concat(CONFIGURATOR_EXCOMMUNICATION_FILE_NAME));
        this.setExcommunications(parseExcommunications(pathToDirectory.concat(CONFIGURATOR_EXCOMMUNICATION_FILE_NAME)));
        getLog().info("[PARSER]: Excommunications parsed.");
        this.setTimers(parseTimerStartGame(pathToDirectory.concat(CONFIGURATOR_TIMERS_FILE_NAME)), parseTimerPlayMove(pathToDirectory.concat(CONFIGURATOR_TIMERS_FILE_NAME)));
        return this;
    }
}