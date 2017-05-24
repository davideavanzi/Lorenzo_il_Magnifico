package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.model.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static it.polimi.ingsw.lim.parser.Parser.*;
import static it.polimi.ingsw.lim.Settings.*;

/**
 * Created by FabCars. The task of this class is to parse from a file all the game card, so that they could be
 * personalized by players.
 */

public class CardParser {


    /**
     * this method parse an ArrayList of ImmediateEffect from Json file
     * @param immediateEffectType is a JsonNode element "link" to the immediateEffectType in JSon.txt
     * @return an ArrayList of ImmediateEffect after parsing
     */
    public static ArrayList<ImmediateEffect> parseImmediateEffect(JsonNode immediateEffectType){
        //TODO: manca da gestire il caso di effetti dello stesso tipo nella stessa carta, forse conviene gestirlo nel file JSon come un array e qui con un iterator.hasNext()
        //use to check if an ImmediateEffect exists
        boolean immediateEffectExist = false;

        ArrayList<ImmediateEffect> immediateEffects = new ArrayList<ImmediateEffect>();

        //check they type of the immediateEffect
        if (immediateEffectType.path("ActionEffect").isContainerNode()) {
            immediateEffectExist = true;
            Strengths tmpGreenActionStrengths = null;
            if(immediateEffectType.path("ActionEffect").path("greenActionStrengths").isContainerNode()) {
                JsonNode greenActionStrengths = immediateEffectType.path("ActionEffect").path("greenActionStrengths");
                tmpGreenActionStrengths = parseStrengths(greenActionStrengths);
            }

            Assets tmpActionEffectDiscount = null;
            if(immediateEffectType.path("ActionEffect").path("actionEffectAssets").isContainerNode()) {
                JsonNode actionEffectDiscount = immediateEffectType.path("ActionEffect").path("actionEffectAssets");
                tmpActionEffectDiscount = parseAssets(actionEffectDiscount);
            }

            ActionEffect actionEffect = new ActionEffect(tmpGreenActionStrengths, tmpActionEffectDiscount);
            immediateEffects.add(actionEffect);
        }
        else{
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("assetsEffect").isContainerNode()){
            immediateEffectExist = true;

            JsonNode assetsEffectBonus = immediateEffectType.path("assetsEffect").path("assetsEffectBonus");
            Assets tmpAssetsEffectBonus= parseAssets(assetsEffectBonus);

            AssetsEffect assetsEffect = new AssetsEffect(tmpAssetsEffectBonus);
            immediateEffects.add(assetsEffect);
        }
        else{
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("councilFavorsEffect").isContainerNode()){
            immediateEffectExist = true;

            JsonNode councilFavorsEffectAmount = immediateEffectType.path("councilFavorsEffect").path("councilFavorsEffectAmount");
            int tmpCouncilFavorsEffectAmount = councilFavorsEffectAmount.asInt();

            CouncilFavorsEffect councilFavorsEffect = new CouncilFavorsEffect(tmpCouncilFavorsEffectAmount);
            immediateEffects.add(councilFavorsEffect);
        }
        else{
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("cardMultipliedEffect").isContainerNode()){
            immediateEffectExist = true;

            JsonNode cardMultiopliedEffectBonus = immediateEffectType.path("cardMultipliedEffect").path("cardMultiopliedEffectBonus");
            Assets tmpCardMultiopliedEffectBonus= parseAssets(cardMultiopliedEffectBonus);

            JsonNode cardMultiopliedEffectMultiplierColor = immediateEffectType.path("cardMultipliedEffect").path("cardMultiopliedEffectMultiplierColor");
            String tmpCardMultiopliedEffectMultiplierColor = cardMultiopliedEffectMultiplierColor.asText();

            CardMultipliedEffect cardMultipliedEffect = new CardMultipliedEffect(tmpCardMultiopliedEffectBonus, tmpCardMultiopliedEffectMultiplierColor);
            immediateEffects.add(cardMultipliedEffect);
        }
        else{
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (immediateEffectType.path("assetsMultipliedEffect").isContainerNode()){
            immediateEffectExist = true;

            JsonNode assetsMultipliedEffectBonus = immediateEffectType.path("assetsMultipliedEffect").path("assetsMultipliedEffectBonus");
            Assets tmpAssetsMultipliedEffectBonus = parseAssets(assetsMultipliedEffectBonus);

            JsonNode assetsMultipliedEffectMultiplier = immediateEffectType.path("assetsMultipliedEffect").path("assetsMultipliedEffectMultiplier");
            Assets tmpAssetsMultipliedEffectMultiplier = parseAssets(assetsMultipliedEffectMultiplier);

            AssetsMultipliedEffect assetsMultipliedEffect = new AssetsMultipliedEffect(tmpAssetsMultipliedEffectBonus, tmpAssetsMultipliedEffectMultiplier);
            immediateEffects.add(assetsMultipliedEffect);
        }
        else{
            //TODO: use the constructor to set NULL immediateEffectType effect
        }

        if (!(immediateEffectExist)){
            //TODO: set immediate effect to NULL
        }

        return immediateEffects;
    }

    /**
     * this method get a path to the configurator card file and return an ArrayList which contain all game's card.
     * The single card is built using the constructor of the single card type (some cards instance are nullable others
     * not nullable (e.g. name, age, color))
     * @param pathToConfiguratorCardFile is the path to the config card file (from the root or from the working
     *                                   directory)
     * @return an ArrayList of card containing all game's card
     */

    //TODO: chi gestisce l'eccezione di cardAge o Cardcolor = null?
    public static HashMap<String, ArrayList<Card>>[] cardParser (String pathToConfiguratorCardFile){

        //the ArrayList where store all the game's card
        HashMap<String, ArrayList<Card>>[] cards = new HashMap[AGES_NUMBER];
        ArrayList<Card> greenCards = new ArrayList<>();
        ArrayList<Card> blueCards = new ArrayList<>();
        ArrayList<Card> yellowCards = new ArrayList<>();
        ArrayList<Card> purpleCards = new ArrayList<>();
        ArrayList<Card> blackCards = new ArrayList<>();
        //creating hashmap
        for(int i = 0; i < AGES_NUMBER; i++){
            cards[i].put(GREEN_COLOR, greenCards);
            cards[i].put(BLUE_COLOR, blueCards);
            cards[i].put(YELLOW_COLOR, yellowCards);
            cards[i].put(PURPLE_COLOR, purpleCards);
            cards[i].put(BLACK_COLOR, blackCards);
        }

        try {
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
                JsonNode cardName = cardNode.path("cardName");
                JsonNode cardAge = cardNode.path("cardAge");
                JsonNode cardType = cardNode.path("cardType");

                Assets tmpCardAssetsCost = null;
                if (cardNode.path("cardAssetsCost").isContainerNode()) {
                    JsonNode cardAssetsCost = cardNode.path("cardAssetsCost");
                    tmpCardAssetsCost = parseAssets(cardAssetsCost);
                }
                ArrayList<ImmediateEffect> immediateEffects = null;
                if(cardNode.path("cardImmediateEffect").isContainerNode()) {
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
                        cards[cardAge.asInt() - 1].get("GREEN").add(greenCard);
                        break;
                    case "yellowCard":
                        ArrayList<Assets> tmpYellowProductionCostList = new ArrayList<Assets>();
                        ArrayList<Assets> tmpYellowProductionResultList = new ArrayList<Assets>();
                        Iterator<JsonNode> yellowProductionEffectIterator = cardNode.path("yellowProduction").getElements();
                        while (yellowProductionEffectIterator.hasNext()) {
                            yellowProductionEffectIterator.next();
                            if(cardNode.path("yellowProductionCost").isContainerNode()) {
                                JsonNode yellowProductionCost = cardNode.path("yellowProductionCost");
                                Assets tmpYellowProductionCost = parseAssets(yellowProductionCost);
                                tmpYellowProductionCostList.add(tmpYellowProductionCost);
                            }
                            if (cardNode.path("yellowProductionResult").isContainerNode()) {
                                JsonNode yellowProductionResult = cardNode.path("yellowProductionResult");
                                Assets tmpYellowProductionResult = parseAssets(yellowProductionResult);
                                tmpYellowProductionCostList.add(tmpYellowProductionResult);
                            }
                        }
                        Strengths tmpYellowActionStrenghts = null;
                        if (cardNode.path("yellowActionStrenghts").isContainerNode()) {
                            JsonNode yellowActionStrenghts = cardNode.path("yellowActionStrenghts");
                            tmpYellowActionStrenghts = parseStrengths(yellowActionStrenghts);
                        }
                        String tmpYellowBonusMultiplier = null;
                        if (cardNode.path("yellowBonusMultiplier").isContainerNode()){
                            tmpYellowBonusMultiplier = cardNode.path("yellowBonusMultiplier").asText();
                        }
                        YellowCard yellowCard = new YellowCard(
                                cardName.asText(),
                                cardAge.asInt(),
                                tmpCardAssetsCost,
                                immediateEffects,
                                tmpYellowProductionCostList,
                                tmpYellowProductionResultList,
                                tmpYellowActionStrenghts,
                                tmpYellowBonusMultiplier
                        );
                        cards[cardAge.asInt() - 1].get(YELLOW_COLOR).add(yellowCard);
                        break;
                    case "blueCard":
                        Strengths tmpBluePermanentBonus = null;
                        if(cardNode.path("bluePermanentBonus").isContainerNode()) {
                            JsonNode bluePermanentBonus = cardNode.path("bluePermanentBonus");
                            tmpBluePermanentBonus = parseStrengths(bluePermanentBonus);
                        }
                        Assets tmpBlueGreenDiscount = null;
                        if(cardNode.path("blueGreenDiscount").isContainerNode()) {
                            JsonNode blueGreenDiscount = cardNode.path("blueGreenDiscount");
                            tmpBlueGreenDiscount = parseAssets(blueGreenDiscount);
                        }
                        Assets tmpBlueBlueDiscount = null;
                        if(cardNode.path("blueBlueDiscount").isContainerNode()) {
                            JsonNode blueBlueDiscount = cardNode.path("blueBlueDiscount");
                            tmpBlueBlueDiscount = parseAssets(blueBlueDiscount);
                        }
                        Assets tmpBlueYellowDiscount = null;
                        if(cardNode.path("blueYellowDiscount").isContainerNode()) {
                            JsonNode blueYellowDiscount = cardNode.path("blueYellowDiscount");
                            tmpBlueYellowDiscount = parseAssets(blueYellowDiscount);
                        }
                        Assets tmpBluePurpleDiscount = null;
                        if(cardNode.path("bluePurpleDiscount").isContainerNode()) {
                            JsonNode bluePurpleDiscount = cardNode.path("bluePurpleDiscount");
                            tmpBluePurpleDiscount = parseAssets(bluePurpleDiscount);
                        }
                        Assets tmpBlueBlackDiscount = null;
                        if(cardNode.path("blueBlackDiscount").isContainerNode()) {
                            JsonNode blueBlackDiscount = cardNode.path("blueBlackDiscount");
                            tmpBlueBlackDiscount = parseAssets(blueBlackDiscount);
                        }
                        boolean tmpBlueTowerBonusAllowed = true;
                        if (cardNode.path("blueTowerBonusAllowed").isContainerNode()) {
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
                        cards[cardAge.asInt() - 1].get(BLUE_COLOR).add(blueCard);
                        break;
                    case "purpleCard":
                        JsonNode purpleEndGameBonus = cardNode.path("purpleEndGameBonus");
                        Assets tmpPurpleEndGameBonus = parseAssets(purpleEndGameBonus);
                        int tmpPurpleOptionalBattlePointsRequirement = 0;
                        if (cardNode.path("purpleOptionalBattlePointsRequirement").isContainerNode()) {
                            tmpPurpleOptionalBattlePointsRequirement = cardNode.path("purpleOptionalBattlePointsRequirement").asInt();
                        }
                        int tmpPurpleOptionalBattlePointsCost = 0;
                        if (cardNode.path("purpleOptionalBattlePointsCost").isContainerNode()) {
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
                        cards[cardAge.asInt() - 1].get(PURPLE_COLOR).add(purpleCard);
                        break;

                    case "blackCard":
                        BlackCard blackCard = new BlackCard(
                                cardName.asText(),
                                cardAge.asInt(),
                                tmpCardAssetsCost,
                                immediateEffects
                        );
                        cards[cardAge.asInt() - 1].get(BLACK_COLOR).add(blackCard);
                        break;

                    default:
                        //TODO: define notExistingCardTypeException exception
                        break;
                }
            }
        } catch (Exception e) { //TODO:better use of exception
            e.printStackTrace();
        }
        return cards;
    }
}
