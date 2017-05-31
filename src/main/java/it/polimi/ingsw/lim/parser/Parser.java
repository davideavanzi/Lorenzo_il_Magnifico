package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.model.*;
import it.polimi.ingsw.lim.exceptions.*;
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
import static it.polimi.ingsw.lim.parser.ParserKeyConst.*;


/**
 * Created by FabCars.
 */
public class Parser {

    private ArrayList<HashMap<String, ArrayList<Card>>> cards = new ArrayList<>();
    private HashMap<String, Assets[]> boardAssetsBonuses = new HashMap<>();
    private HashMap<Integer, ArrayList<Excommunication>> excommunications = new HashMap<>();
    private int councilFavors;
    private Assets councilBonus;
    private Assets startingGameBonus;

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

    public ArrayList<HashMap<String, ArrayList<Card>>> getCards() {
        return this.cards;
    }

    public ArrayList<Card> getCard(int age, String key) {
        return this.cards.get(age - 1).get(key);
    }

    public HashMap<String, ArrayList<Card>> getCard(int age) {
        return this.cards.get(age - 1);
    }

    //use only for tower
    public Assets[] getTowerbonuses(String key) {
        return this.boardAssetsBonuses.get(key);
    }

    public Assets[] getMarketBonuses() {
        return this.boardAssetsBonuses.get(MARKET);
    }

    public Assets[] getFaithTrackbonuses() {
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
    } //TODO controlla se serve davvero il this nei get

    /**
     * this method parse an Assets' type from Json file
     *
     * @param assetsToParse is a JsonNode element "link" to the assets in JSon.txt
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
     * this method parse an array of assets
     *
     * @param arrayAssetsNode is the node "link" to the array in JsonFile
     * @return an array of Assets
     */
    private static Assets[] parseArrayAssets(JsonNode arrayAssetsNode, int assetsNum) {
        JsonNode tmpArrayAssetsNode = arrayAssetsNode;
        Assets[] assets = new Assets[assetsNum]; //TODO: rendere configurabile il numero di piani (e quindi di assets presenti nell'array)
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
     * this method parse an Strengths' type from Json file
     *
     * @param strengthsToParse is a JsonNode element "link" to the strength in JSon.txt
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
                strengthsToParse.path(BLACK_BONUS).asInt()
        );
    }

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
        bonuses.put(MARKET, parseArrayAssets(bonusesNode.path(MARKET_BONUS), MARKET_MAX_SIZE));
        bonuses.put(FAITH_TRACK, parseArrayAssets(bonusesNode.path(FAITH_BONUS), FAITH_TRACK_LENGTH));
        bonuses.put(COUNCIL_FAVOUR, parseArrayAssets(bonusesNode.path(COUNCIL_FAVOUT_BONUS), COUNCIL_FAVUORS_TYPES));

        return bonuses;
    }

    /**
     * this method parse an ArrayList of ImmediateEffect from Json file
     *
     * @param immediateEffectType is a JsonNode element "link" to the immediateEffectType in JSon.txt
     * @return an ArrayList of ImmediateEffect after parsing
     */
    private static ArrayList<ImmediateEffect> parseImmediateEffect(JsonNode immediateEffectType) {
        //use to check if an ImmediateEffect exists
        boolean immediateEffectExist = false;

        ArrayList<ImmediateEffect> immediateEffects = new ArrayList<>();

        //check they type of the immediateEffect
        if (immediateEffectType.path(ACTION_EFFECT).isContainerNode()) {
            immediateEffectExist = true;

            Strengths tmpActionStrengths = null;
            if (immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_STRENGTH).isContainerNode()) {
                JsonNode actionEffectStrength = immediateEffectType.path(ACTION_EFFECT).path(ACTION_EFFECT_STRENGTH);
                tmpActionStrengths = parseStrengths(actionEffectStrength);
            }

            Assets tmpActionEffectDiscount = null;
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

    private static GreenCard parseGreenCard
    (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode){
        Assets tmpGreenHarvestResult = null;
        if (cardNode.path(GREEN_HARVEST_RESULT).isContainerNode()) {
            JsonNode greenHarvestResult = cardNode.path(GREEN_HARVEST_RESULT);
            tmpGreenHarvestResult = parseAssets(greenHarvestResult);
        }

        Strengths tmpGreenActionStrengths = null;
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

    private static BlueCard parseBlueCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode) {
        Strengths tmpBluePermanentBonus = null;
        if (cardNode.path(BLUE_PERMANET_BONUS).isContainerNode()) {
            JsonNode bluePermanentBonus = cardNode.path(BLUE_PERMANET_BONUS);
            tmpBluePermanentBonus = parseStrengths(bluePermanentBonus);
        }

        Assets tmpBlueGreenDiscount = null;
        if (cardNode.path(BLUE_GREEN_DISCOUNT).isContainerNode()) {
            JsonNode blueGreenDiscount = cardNode.path(BLUE_GREEN_DISCOUNT);
            tmpBlueGreenDiscount = parseAssets(blueGreenDiscount);
        }

        Assets tmpBlueBlueDiscount = null;
        if (cardNode.path(BLUE_BLUE_DISCOUNT).isContainerNode()) {
            JsonNode blueBlueDiscount = cardNode.path(BLUE_BLUE_DISCOUNT);
            tmpBlueBlueDiscount = parseAssets(blueBlueDiscount);
        }

        Assets tmpBlueYellowDiscount = null;
        if (cardNode.path(BLUE_YELLOW_DISCOUNT).isContainerNode()) {
            JsonNode blueYellowDiscount = cardNode.path(BLUE_YELLOW_DISCOUNT);
            tmpBlueYellowDiscount = parseAssets(blueYellowDiscount);
        }

        Assets tmpBluePurpleDiscount = null;
        if (cardNode.path(BLUE_PURPLE_DISCOUNT).isContainerNode()) {
            JsonNode bluePurpleDiscount = cardNode.path(BLUE_PURPLE_DISCOUNT);
            tmpBluePurpleDiscount = parseAssets(bluePurpleDiscount);
        }

        Assets tmpBlueBlackDiscount = null;
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

    private static YellowCard parseYellowCard
            (String cardName, int cardAge, Assets tmpCardAssetsCost, ArrayList<ImmediateEffect> immediateEffects, JsonNode cardNode) {
        ArrayList<Assets> tmpYellowProductionCostList = new ArrayList<>();
        ArrayList<Assets> tmpYellowProductionResultList = new ArrayList<>();
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
                tmpYellowProductionCostList.add(tmpYellowProductionResult);
            }
        }

        Strengths tmpYellowActionStrengths = null;
        if (cardNode.path(YELLOW_ACTION_STRENGTHS).isContainerNode()) {
            JsonNode yellowActionStrengths = cardNode.path(YELLOW_ACTION_STRENGTHS);
            tmpYellowActionStrengths = parseStrengths(yellowActionStrengths);
        }

        String tmpYellowBonusMultiplier = null;
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
     * this method get a path to the configurator card file and return an HashMap which contain all game's card.
     * The single card is built using the constructor of the single card type (some cards instance are nullable others
     * not nullable (e.g. name, age, color))
     *
     * @param pathToConfiguratorCardFile is the path to the config card file (from the root or from the working
     *                                   directory)
     * @return an HashMap of card containing all game's card
     */
    private static ArrayList<HashMap<String, ArrayList<Card>>> cardParser(String pathToConfiguratorCardFile)
            throws IOException, InvalidCardException{

        //the ArrayList where store all the game's card
        ArrayList<HashMap<String, ArrayList<Card>>> cards = new ArrayList<>();

        //creating hashmap
        for (int i = 0; i < AGES_NUMBER; i++) {
            HashMap<String, ArrayList<Card>> tmpCards = new HashMap<>();
            tmpCards.put(GREEN_COLOR, new ArrayList<Card>());
            tmpCards.put(BLUE_COLOR, new ArrayList<Card>());
            tmpCards.put(YELLOW_COLOR, new ArrayList<Card>());
            tmpCards.put(PURPLE_COLOR, new ArrayList<Card>());
            tmpCards.put(BLACK_COLOR, new ArrayList<Card>());
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
            if (!(cardNode.path(CARD_NAME).isTextual())) {
                throw new InvalidCardException("Card Name is not nullable");
            }
            JsonNode cardName = cardNode.path("cardName");
            if (!(cardNode.path(CARD_AGE).isInt())) {
                throw new InvalidCardException("Card Age is not nullable");
            }
            JsonNode cardAge = cardNode.path("cardAge");
            JsonNode cardType = cardNode.path("cardType");
            if (!(cardNode.path(CARD_TYPE).isTextual())) {
                throw new InvalidCardException("Card Type is not nullable");
            }
            Assets tmpCardAssetsCost = null;
            if (cardNode.path(CARD_ASSETS_COST).isContainerNode()) {
                JsonNode cardAssetsCost = cardNode.path("cardAssetsCost");
                tmpCardAssetsCost = parseAssets(cardAssetsCost);
            }

            ArrayList<ImmediateEffect> immediateEffects = null;
            if (cardNode.path(CARD_IMMEDIATE_EFFECT).isContainerNode()) {
                JsonNode cardImmediateEffect = cardNode.path("cardImmediateEffect");
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
                    YellowCard yellowCard = parseYellowCard
                            (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(YELLOW_COLOR).add(yellowCard);
                    break;
                case YELLOW_CARD:
                    BlueCard blueCard = parseBlueCard
                            (cardName.asText(), cardAge.asInt(), tmpCardAssetsCost, immediateEffects, cardNode);
                    cards.get(cardAge.asInt() - 1).get(BLUE_COLOR).add(blueCard);
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

    private static Assets parseCouncilBonus(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets councilBonus;
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        councilBonus = parseAssets(bonusesNode.path(COUNCIL_BONUS));
        return councilBonus;
    }

    private static int parseCouncilFavours(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        int councilFavours;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        councilFavours = bonusesNode.path(COUNCIL_FAVOURS).asInt();

        return councilFavours;
    }

    private static Assets parseStartingGameBonus (String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets startingGameBonus;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path(BONUSES);
        startingGameBonus = parseAssets(bonusesNode.path(STARTING_GAME_BONUS));
        return startingGameBonus;
    }

    private static Excommunication parseSingleExcommunication (JsonNode excommunicationNode)
            throws InvalidExcommunicationException{
        if(!(excommunicationNode.path(EXCOMMUNICATION_TYPE).isTextual())){
            throw new InvalidExcommunicationException("Excommunication Type is not nullable");
        }
        switch (excommunicationNode.path(EXCOMMUNICATION_TYPE).asText()){
            case ASSETS_MALUS_EXCOMMUNICATION_TYPE:
                Assets tmpAssetsMalus = parseAssets(excommunicationNode.path(EXCOMMUNICATION_TYPE).path("assetsMalusExcommunicationType"));
                return new AssetsExcommunication(tmpAssetsMalus);
            case STRENGTH_MALUS_EXCOMMUNICATION:
                Strengths tmpStrengthMalus = parseStrengths(excommunicationNode.path(EXCOMMUNICATION_TYPE).path("strengthMalusExcommunication"));
                return new StrengthsExcommunication(tmpStrengthMalus);
            case MARKET_EXCOMMUNICATION:
                return new MarketExcommunication();
            case SERVANTS_EXCOMMUNICATION:
                return new ServantsExcommunication();
            case TURN_EXCOMMUNICATION:
                return new TurnExcommunication();
            case END_GAME_EXCOMMUNICATION:
                String tmpBlockedCardColor = excommunicationNode.path(EXCOMMUNICATION_TYPE).path("endGameExcommunication").asText();
                Assets tmpProductionCardCostMalus = parseAssets
                        (excommunicationNode.path(EXCOMMUNICATION_TYPE).path(PRODUCTION_CARD_COST_MALUS));
                Assets[] tmpOnAssetsMalus = parseArrayAssets
                        (excommunicationNode.path(EXCOMMUNICATION_TYPE).path(ON_ASSETS_MALUS),1);//TODO modificare il numero di slot dell'array
                return new EndGameExcommunication(tmpBlockedCardColor, tmpProductionCardCostMalus, tmpOnAssetsMalus);
            default:
                throw new InvalidExcommunicationException("Excommunication Type is not recognized");
        }
    }

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

    private static HashMap<Integer, ArrayList<Excommunication>> parseExcommunications (String pathToExcommunicationsConfiguratorFile)
            throws IOException, InvalidExcommunicationException {
        HashMap<Integer, ArrayList<Excommunication>> tmpExcommunications = new HashMap<>();
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToExcommunicationsConfiguratorFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode excommunicationNode = rootNode.path(EXCOMMUNICATIONS);
        Iterator<JsonNode> excommunicationIterator = excommunicationNode.getElements();

        //start to parse one by one the card
        int i = 0;
        while (excommunicationIterator.hasNext()) {
            //get all info from file
            excommunicationNode = excommunicationIterator.next();
            ArrayList<Excommunication> tmpExcommunicationAge = parseArrayExcommunication (excommunicationNode);
            i++;
            tmpExcommunications.put(i, tmpExcommunicationAge);
        }
        return tmpExcommunications;
    }

    /**
     * TODO:see params and return
     */
    public Parser parser(String pathToDirectory)
            throws IOException, InvalidCardException, InvalidExcommunicationException{
        getLog().info("Try to parse Cards from file: " + pathToDirectory + CONFIGURATOR_CARD_FILE_NAME);
        this.setCards(cardParser(pathToDirectory + CONFIGURATOR_CARD_FILE_NAME));
        getLog().info("Cards parsed.");
        getLog().info("Try to parse Assets Bonuses from file: " + pathToDirectory + CONFIGURATOR_BONUS_ASSETS_FILE_NAME);
        this.setBoardAssetsBonuses(boardAssetsParser(pathToDirectory + CONFIGURATOR_BONUS_ASSETS_FILE_NAME));
        this.setCouncilBonus(parseCouncilBonus(pathToDirectory + CONFIGURATOR_BONUS_ASSETS_FILE_NAME));
        this.setCouncilFavors(parseCouncilFavours(pathToDirectory + CONFIGURATOR_BONUS_ASSETS_FILE_NAME));
        this.setStartingGameBonus(parseStartingGameBonus((pathToDirectory + CONFIGURATOR_BONUS_ASSETS_FILE_NAME)));
        getLog().info("Assets Bonuses parsed.");
        getLog().info("Try to parse Excommunication from file: " + pathToDirectory + CONFIGURATOR_EXCOMMUNICATION_FILE_NAME);
        this.setExcommunications(parseExcommunications(pathToDirectory + CONFIGURATOR_EXCOMMUNICATION_FILE_NAME));
        getLog().info("Excommunications parsed.");
        return this;
    }
}