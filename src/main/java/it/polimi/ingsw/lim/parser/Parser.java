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


/**
 * Created by FabCars.
 */
public class Parser {

    public static final String MARKET = "MARKET";
    public static final String FAITH_TRACK = "FAITH";
    public static final String COUNCIL_FAVOUR = "FAVOUR";

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
    }

    /**
     * this method parse an Assets' type from Json file
     *
     * @param assetsToParse is a JsonNode element "link" to the assets in JSon.txt
     * @return an Assets' Object type after parsing
     */
    public static Assets parseAssets(JsonNode assetsToParse) {
        Assets tmpParseAssets = new Assets(
                assetsToParse.path("coin").asInt(),
                assetsToParse.path("wood").asInt(),
                assetsToParse.path("stone").asInt(),
                assetsToParse.path("servants").asInt(),
                assetsToParse.path("faithPoints").asInt(),
                assetsToParse.path("battlePoints").asInt(),
                assetsToParse.path("victoryPoints").asInt()
        );
        return tmpParseAssets;
    }

    /**
     * this method parse an array of assets
     *
     * @param arrayAssetsNode is the node "link" to the array in JsonFile
     * @return an array of Assets
     */
    public static Assets[] parseArrayAssets(JsonNode arrayAssetsNode, int assetsNum) {
        Assets[] assets = new Assets[assetsNum]; //TODO: rendere configurabile il numero di piani (e quindi di assets presenti nell'array)
        Iterator<JsonNode> arrayAssetsIterator = arrayAssetsNode.getElements();
        int i = 0;
        while (arrayAssetsIterator.hasNext()) {
            arrayAssetsNode = arrayAssetsIterator.next();
            assets[i] = parseAssets(arrayAssetsNode);
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
    public static Strengths parseStrengths(JsonNode strengthsToParse) {
        Strengths tmpParseStrengths = new Strengths(
                strengthsToParse.path("harvestBonus").asInt(),
                strengthsToParse.path("productionBonus").asInt(),
                strengthsToParse.path("greenBonus").asInt(),
                strengthsToParse.path("blueBonus").asInt(),
                strengthsToParse.path("yellowBonus").asInt(),
                strengthsToParse.path("purpleBonus").asInt(),
                strengthsToParse.path("blackBonus").asInt()
        );
        return tmpParseStrengths;
    }

    public static HashMap<String, Assets[]> boardAssetsParser(String PathToConfiguratorBonusesAssetsFile)
            throws IOException{
        HashMap<String, Assets[]> bonuses = new HashMap<String, Assets[]>();

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(PathToConfiguratorBonusesAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path("bonuses");
        bonuses.put(GREEN_COLOR, parseArrayAssets(bonusesNode.path("towerBonus").path("greenTowerBonus"), TOWER_HEIGHT));
        bonuses.put(BLUE_COLOR, parseArrayAssets(bonusesNode.path("towerBonus").path("blueTowerBonus"), TOWER_HEIGHT));
        bonuses.put(YELLOW_COLOR, parseArrayAssets(bonusesNode.path("towerBonus").path("yellowTowerBonus"), TOWER_HEIGHT));
        bonuses.put(PURPLE_COLOR, parseArrayAssets(bonusesNode.path("towerBonus").path("purpleTowerBonus"), TOWER_HEIGHT));
        bonuses.put(BLACK_COLOR, parseArrayAssets(bonusesNode.path("towerBonus").path("blackTowerBonus"), TOWER_HEIGHT));
        bonuses.put(MARKET, parseArrayAssets(bonusesNode.path("marketBonus"), MARKET_MAX_SIZE));
        bonuses.put(FAITH_TRACK, parseArrayAssets(bonusesNode.path("faithBonus"), FAITH_TRACK_LENGTH));
        bonuses.put(COUNCIL_FAVOUR, parseArrayAssets(bonusesNode.path("councilFavoursBonus"), COUNCIL_FAVUORS_TYPES));

        return bonuses;
    }

    /**
     * this method parse an ArrayList of ImmediateEffect from Json file
     *
     * @param immediateEffectType is a JsonNode element "link" to the immediateEffectType in JSon.txt
     * @return an ArrayList of ImmediateEffect after parsing
     */
    public static ArrayList<ImmediateEffect> parseImmediateEffect(JsonNode immediateEffectType) {
        //use to check if an ImmediateEffect exists
        boolean immediateEffectExist = false;

        ArrayList<ImmediateEffect> immediateEffects = new ArrayList<ImmediateEffect>();

        //check they type of the immediateEffect
        if (immediateEffectType.path("actionEffect").isContainerNode()) {
            immediateEffectExist = true;

            Strengths tmpActionStrengths = null;
            if (immediateEffectType.path("actionEffect").path("actionEffectStrength").isContainerNode()) {
                JsonNode actionEffectStrength = immediateEffectType.path("ActionEffect").path("actionEffectStrength");
                tmpActionStrengths = parseStrengths(actionEffectStrength);
            }

            Assets tmpActionEffectDiscount = null;
            if (immediateEffectType.path("ActionEffect").path("actionEffectAssets").isContainerNode()) {
                JsonNode actionEffectDiscount = immediateEffectType.path("ActionEffect").path("actionEffectAssets");
                tmpActionEffectDiscount = parseAssets(actionEffectDiscount);
            }

            ActionEffect actionEffect = new ActionEffect(tmpActionStrengths, tmpActionEffectDiscount);
            immediateEffects.add(actionEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("assetsEffect").isContainerNode()) {
            immediateEffectExist = true;

            JsonNode assetsEffectBonus = immediateEffectType.path("assetsEffect").path("assetsEffectBonus");
            Assets tmpAssetsEffectBonus = parseAssets(assetsEffectBonus);

            AssetsEffect assetsEffect = new AssetsEffect(tmpAssetsEffectBonus);
            immediateEffects.add(assetsEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("councilFavorsEffectAmount").isInt()) { //TODO check
            immediateEffectExist = true;

            int tmpCouncilFavorsEffectAmount = immediateEffectType.path("councilFavorsEffectAmount").asInt();

            CouncilFavorsEffect councilFavorsEffect = new CouncilFavorsEffect(tmpCouncilFavorsEffectAmount);
            immediateEffects.add(councilFavorsEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("cardMultipliedEffect").isContainerNode()) {
            immediateEffectExist = true;

            JsonNode cardMultiopliedEffectBonus = immediateEffectType.path("cardMultipliedEffect").path("cardMultiopliedEffectBonus");
            Assets tmpCardMultiopliedEffectBonus = parseAssets(cardMultiopliedEffectBonus);

            JsonNode cardMultiopliedEffectMultiplierColor = immediateEffectType.path("cardMultipliedEffect").path("cardMultiopliedEffectMultiplierColor");
            String tmpCardMultiopliedEffectMultiplierColor = cardMultiopliedEffectMultiplierColor.asText();

            CardMultipliedEffect cardMultipliedEffect = new CardMultipliedEffect(tmpCardMultiopliedEffectBonus, tmpCardMultiopliedEffectMultiplierColor);
            immediateEffects.add(cardMultipliedEffect);
        } else {
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("assetsMultipliedEffect").isContainerNode()) {
            immediateEffectExist = true;

            JsonNode assetsMultipliedEffectBonus = immediateEffectType.path("assetsMultipliedEffect").path("assetsMultipliedEffectBonus");
            Assets tmpAssetsMultipliedEffectBonus = parseAssets(assetsMultipliedEffectBonus);

            JsonNode assetsMultipliedEffectMultiplier = immediateEffectType.path("assetsMultipliedEffect").path("assetsMultipliedEffectMultiplier");
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
     * this method get a path to the configurator card file and return an HashMap which contain all game's card.
     * The single card is built using the constructor of the single card type (some cards instance are nullable others
     * not nullable (e.g. name, age, color))
     *
     * @param pathToConfiguratorCardFile is the path to the config card file (from the root or from the working
     *                                   directory)
     * @return an HashMap of card containing all game's card
     */
    public static ArrayList<HashMap<String, ArrayList<Card>>> cardParser(String pathToConfiguratorCardFile)
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
        JsonNode cardNode = rootNode.path("card");
        Iterator<JsonNode> cardIterator = cardNode.getElements();

        //start to parse one by one the card
        while (cardIterator.hasNext()) {

            //get all info from file
            cardNode = cardIterator.next();
            //JsonNode cardId = cardNode.path("cardId");
            if (!(cardNode.path("cardName").isTextual())) {
                throw new InvalidCardException("Card Name is not nullable");
            }
            JsonNode cardName = cardNode.path("cardName");
            if (!(cardNode.path("cardAge").isInt())) {
                throw new InvalidCardException("Card Age is not nullable");
            }
            JsonNode cardAge = cardNode.path("cardAge");
            JsonNode cardType = cardNode.path("cardType");
            if (!(cardNode.path("cardType").isTextual())) {
                throw new InvalidCardException("Card Type is not nullable");
            }
            Assets tmpCardAssetsCost = null;
            if (cardNode.path("cardAssetsCost").isContainerNode()) {
                JsonNode cardAssetsCost = cardNode.path("cardAssetsCost");
                tmpCardAssetsCost = parseAssets(cardAssetsCost);
            }

            ArrayList<ImmediateEffect> immediateEffects = null;
            if (cardNode.path("cardImmediateEffect").isContainerNode()) {
                JsonNode cardImmediateEffect = cardNode.path("cardImmediateEffect");
                immediateEffects = parseImmediateEffect(cardImmediateEffect);
            }
            //select the constructor of card in base of the cardType (green,blue,yellow,purple,black)
            switch (cardType.asText()) {
                case "greenCard":
                    Assets tmpGreenHarvestResult = null;
                    if (cardNode.path("greenHarvestResult").isContainerNode()) {
                        JsonNode greenHarvestResult = cardNode.path("greenHarvestResult");
                        tmpGreenHarvestResult = parseAssets(greenHarvestResult);
                    }

                    Strengths tmpGreenActionStrengths = null;
                    if (cardNode.path("greenActionStrengths").isContainerNode()) {
                        JsonNode greenActionStrengths = cardNode.path("greenActionStrengths");
                        tmpGreenActionStrengths = parseStrengths(greenActionStrengths);
                    }

                    GreenCard greenCard = new GreenCard(
                            cardName.asText(),
                            cardAge.asInt(),
                            tmpCardAssetsCost,
                            immediateEffects,
                            tmpGreenHarvestResult,
                            tmpGreenActionStrengths
                    );
                    cards.get(cardAge.asInt() - 1).get(GREEN_COLOR).add(greenCard);
                    break;
                case "yellowCard":
                    ArrayList<Assets> tmpYellowProductionCostList = new ArrayList<Assets>();
                    ArrayList<Assets> tmpYellowProductionResultList = new ArrayList<Assets>();
                    JsonNode yellowProductionNode = cardNode.path("yellowProduction");
                    Iterator<JsonNode> yellowProductionEffectIterator = yellowProductionNode.getElements();

                    while (yellowProductionEffectIterator.hasNext()) {
                        yellowProductionNode = yellowProductionEffectIterator.next();

                        if (yellowProductionNode.path("yellowProductionCost").isContainerNode()) {
                            JsonNode yellowProductionCost = yellowProductionNode.path("yellowProductionCost");
                            Assets tmpYellowProductionCost = parseAssets(yellowProductionCost);
                            tmpYellowProductionCostList.add(tmpYellowProductionCost);
                        }

                        if (yellowProductionNode.path("yellowProductionResult").isContainerNode()) {
                            JsonNode yellowProductionResult = yellowProductionNode.path("yellowProductionResult");
                            Assets tmpYellowProductionResult = parseAssets(yellowProductionResult);
                            tmpYellowProductionCostList.add(tmpYellowProductionResult);
                        }
                    }

                    Strengths tmpYellowActionStrengths = null;
                    if (cardNode.path("yellowActionStrengths").isContainerNode()) {
                        JsonNode yellowActionStrengths = cardNode.path("yellowActionStrengths");
                        tmpYellowActionStrengths = parseStrengths(yellowActionStrengths);
                    }

                    String tmpYellowBonusMultiplier = null;
                    if (cardNode.path("yellowBonusMultiplier").isTextual()) {//TODO check
                        tmpYellowBonusMultiplier = cardNode.path("yellowBonusMultiplier").asText();
                    }

                    YellowCard yellowCard = new YellowCard(
                            cardName.asText(),
                            cardAge.asInt(),
                            tmpCardAssetsCost,
                            immediateEffects,
                            tmpYellowProductionCostList,
                            tmpYellowProductionResultList,
                            tmpYellowActionStrengths,
                            tmpYellowBonusMultiplier
                    );
                    cards.get(cardAge.asInt() - 1).get(YELLOW_COLOR).add(yellowCard);
                    break;
                case "blueCard":
                    Strengths tmpBluePermanentBonus = null;
                    if (cardNode.path("bluePermanentBonus").isContainerNode()) {
                        JsonNode bluePermanentBonus = cardNode.path("bluePermanentBonus");
                        tmpBluePermanentBonus = parseStrengths(bluePermanentBonus);
                    }

                    Assets tmpBlueGreenDiscount = null;
                    if (cardNode.path("blueGreenDiscount").isContainerNode()) {
                        JsonNode blueGreenDiscount = cardNode.path("blueGreenDiscount");
                        tmpBlueGreenDiscount = parseAssets(blueGreenDiscount);
                    }

                    Assets tmpBlueBlueDiscount = null;
                    if (cardNode.path("blueBlueDiscount").isContainerNode()) {
                        JsonNode blueBlueDiscount = cardNode.path("blueBlueDiscount");
                        tmpBlueBlueDiscount = parseAssets(blueBlueDiscount);
                    }

                    Assets tmpBlueYellowDiscount = null;
                    if (cardNode.path("blueYellowDiscount").isContainerNode()) {
                        JsonNode blueYellowDiscount = cardNode.path("blueYellowDiscount");
                        tmpBlueYellowDiscount = parseAssets(blueYellowDiscount);
                    }

                    Assets tmpBluePurpleDiscount = null;
                    if (cardNode.path("bluePurpleDiscount").isContainerNode()) {
                        JsonNode bluePurpleDiscount = cardNode.path("bluePurpleDiscount");
                        tmpBluePurpleDiscount = parseAssets(bluePurpleDiscount);
                    }

                    Assets tmpBlueBlackDiscount = null;
                    if (cardNode.path("blueBlackDiscount").isContainerNode()) {
                        JsonNode blueBlackDiscount = cardNode.path("blueBlackDiscount");
                        tmpBlueBlackDiscount = parseAssets(blueBlackDiscount);
                    }

                    boolean tmpBlueTowerBonusAllowed = true;
                    if (cardNode.path("blueTowerBonusAllowed").isBoolean()) { //TODO check
                        tmpBlueTowerBonusAllowed = cardNode.path("blueTowerBonusAllowed").asBoolean();
                    }

                    BlueCard blueCard = new BlueCard(
                            cardName.asText(),
                            cardAge.asInt(),
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
                    cards.get(cardAge.asInt() - 1).get(BLUE_COLOR).add(blueCard);
                    break;
                case "purpleCard":
                    JsonNode purpleEndGameBonus = cardNode.path("purpleEndGameBonus");
                    Assets tmpPurpleEndGameBonus = parseAssets(purpleEndGameBonus);

                    int tmpPurpleOptionalBattlePointsRequirement = 0;
                    if (cardNode.path("purpleOptionalBattlePointsRequirement").isInt()) {
                        tmpPurpleOptionalBattlePointsRequirement = cardNode.path("purpleOptionalBattlePointsRequirement").asInt();
                    }

                    int tmpPurpleOptionalBattlePointsCost = 0;
                    if (cardNode.path("purpleOptionalBattlePointsCost").isInt()) {
                        tmpPurpleOptionalBattlePointsCost = cardNode.path("purpleOptionalBattlePointsCost").asInt();
                    }

                    PurpleCard purpleCard = new PurpleCard(
                            cardName.asText(),
                            cardAge.asInt(),
                            tmpCardAssetsCost,
                            immediateEffects,
                            tmpPurpleEndGameBonus,
                            tmpPurpleOptionalBattlePointsRequirement,
                            tmpPurpleOptionalBattlePointsCost
                    );
                    cards.get(cardAge.asInt() - 1).get(PURPLE_COLOR).add(purpleCard);
                    break;

                case "blackCard":
                    BlackCard blackCard = new BlackCard(
                            cardName.asText(),
                            cardAge.asInt(),
                            tmpCardAssetsCost,
                            immediateEffects
                    );
                    cards.get(cardAge.asInt() - 1).get(BLACK_COLOR).add(blackCard);
                    break;

                default:
                    throw new InvalidCardException("Card Type is not recognized");
             }
        }

        return cards;
    }

    public static Assets parseCouncilBonus(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets councilBonus = null;
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path("bonuses");
        councilBonus = parseAssets(bonusesNode.path("councilBonus"));
        return councilBonus;
    }

    public static int parseCouncilFavours(String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        int councilFavours = 0;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path("bonuses");
        councilFavours = bonusesNode.path("councilFavours").asInt();

        return councilFavours;
    }

    public static Assets parseStartingGameBonus (String pathToConfiguratorBonusAssetsFile)
            throws IOException{
        Assets startingGameBonus = null;

        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToConfiguratorBonusAssetsFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode bonusesNode = rootNode.path("bonuses");
        startingGameBonus = parseAssets(bonusesNode.path("startingGameBonus"));
        return startingGameBonus;
    }

    public static Excommunication parseSingleExcommunication (JsonNode excommunicationNode)
            throws InvalidExcommunicationException{
        if(!(excommunicationNode.path("excommunicationType").isTextual())){
            throw new InvalidExcommunicationException("Excommunication Type is not nullable");
        }
        switch (excommunicationNode.path("excommunicationType").asText()){
            case "assetsMalusExcommunicationType":
                Assets tmpAssetsMalus = parseAssets(excommunicationNode.path("excommunicationType").path("assetsMalusExcommunicationType"));
                return new AssetsExcommunication(tmpAssetsMalus);
            case "strengthMalusExcommunication":
                Strengths tmpStrengthMalus = parseStrengths(excommunicationNode.path("excommunicationType").path("strengthMalusExcommunication"));
                return new StrengthsExcommunication(tmpStrengthMalus);
            case "marketExcommunication":
                return new MarketExcommunication();
            case "servantsExcommunication":
                return new ServantsExcommunication();
            case "turnExcommunication":
                return new TurnExcommunication();
            case "endGameExcommunication":
                String tmpBlockedCardColor = excommunicationNode.path("excommunicationType").path("endGameExcommunication").asText();
                Assets tmpProductionCardCostMalus = parseAssets(excommunicationNode.path("excommunicationType").path("productionCardCostMalus"));
                Assets[] tmpOnAssetsMalus = parseArrayAssets(excommunicationNode.path("excommunicationType").path("onAssetsMalus"),1);//TODO modificare il numero di slot dell'array
                return new EndGameExcommunication(tmpBlockedCardColor, tmpProductionCardCostMalus, tmpOnAssetsMalus);
            default:
                throw new InvalidExcommunicationException("Excommunication Type is not recognized");
        }
    }

    public static ArrayList<Excommunication> parseArrayExcommunication (JsonNode arrayExcommunicationNode)
            throws InvalidExcommunicationException {
        ArrayList<Excommunication> tmpArrayExcommunication = new ArrayList<Excommunication>();
        Iterator<JsonNode> arrayExcommunicationIterator = arrayExcommunicationNode.getElements();
        while(arrayExcommunicationIterator.hasNext()){
            arrayExcommunicationNode = arrayExcommunicationIterator.next();
            Excommunication tmpSingleExcommunication = parseSingleExcommunication(arrayExcommunicationNode);
            tmpArrayExcommunication.add(tmpSingleExcommunication);
        }
        return tmpArrayExcommunication;
    }

    public static HashMap<Integer, ArrayList<Excommunication>> parseExcommunications (String pathToExcommunicationsConfiguratorFile)
            throws IOException, InvalidExcommunicationException {
        HashMap<Integer, ArrayList<Excommunication>> tmpExcommunications = new HashMap<>();
        //read JSon all file data
        byte[] jsonData = Files.readAllBytes(Paths.get(pathToExcommunicationsConfiguratorFile));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //create the cardNode and the cardIterator (used for iterating through the JSon's array of card)
        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode excommunicationNode = rootNode.path("excommunications");
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
    public static Parser parser(String pathToDirectory)
            throws IOException, InvalidCardException, InvalidExcommunicationException{
        Parser parser = new Parser();
        getLog().info("Try to parse Cards from file: " + pathToDirectory + "configuratorCardFile.json");
        parser.setCards(cardParser(pathToDirectory + "configuratorCardFile.json"));
        getLog().info("Cards parsed.");
        getLog().info("Try to parse Assets Bonuses from file: " + pathToDirectory + "configuratorBonusesAssetsFile.json");
        parser.setBoardAssetsBonuses(boardAssetsParser(pathToDirectory + "configuratorBonusesAssetsFile.json"));
        parser.setCouncilBonus(parseCouncilBonus(pathToDirectory + "configuratorBonusesAssetsFile.json"));
        parser.setCouncilFavors(parseCouncilFavours(pathToDirectory + "configuratorBonusesAssetsFile.json"));
        parser.setStartingGameBonus(parseStartingGameBonus((pathToDirectory + "configuratorBonusesAssetsFile.json")));
        getLog().info("Assets Bonuses parsed.");
        getLog().info("Try to parse Excommunication from file: " + pathToDirectory + "configuratorExcommunicationsFile.json");
        parser.setExcommunications(parseExcommunications(pathToDirectory + "configuratorExcommunicationsFile.json"));
        getLog().info("Excommunications parsed.");
        return parser;
    }
}