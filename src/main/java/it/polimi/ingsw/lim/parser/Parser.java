package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Card;
import it.polimi.ingsw.lim.model.Excommunication;
import it.polimi.ingsw.lim.model.Strengths;
import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.parser.CardParser.*;


/**
 * Created by fabri on 23/05/17.
 */
public class Parser {
    public Parser(){
    }

    private static final String MARKET_BONUS = "MARKET";
    private static final String FAITH_BONUS = "FAITH";
    private HashMap<String, ArrayList<Card>>[] cards = new HashMap[AGES_NUMBER];
    private HashMap<String, Assets[]> boardAssetsBonus = new HashMap<>();
    private int councilFavors;
    private Assets councilBonus;

    public void setCards(HashMap<String, ArrayList<Card>>[] cards) {
        this.cards = cards;
    }

    public void setBoardAssetsBonus (String key, Assets[] assets){
        boardAssetsBonus.put(key, assets);
    }

    public void setCouncilFavors(int councilFavors) {
        this.councilFavors = councilFavors;
    }

    public void setCouncilBonus(Assets councilBonus) {
        this.councilBonus = councilBonus;
    }

    public HashMap<String, ArrayList<Card>>[] getCards() {
        return cards;
    }

    public HashMap<String, ArrayList<Card>> getAgeCards(int age) {
        return cards[age];
    }

    //use only for tower
    public Assets[] getTowerBonus (String key){
        return boardAssetsBonus.get(key);
    }

    public Assets[] getMarketBonuses (){
        return boardAssetsBonus.get(MARKET_BONUS);
    }

    public Assets[] getFaithTrackBonus (){
        return boardAssetsBonus.get(FAITH_BONUS);
    }

    public int getCouncilFavors() {
        return councilFavors;
    }

    public Assets getCouncilBonus() {
        return councilBonus;
    }

    /**
     * this method parse an Assets' type from Json file
     * @param assetsToParse is a JsonNode element "link" to the assets in JSon.txt
     * @return an Assets' Object type after parsing
     */
    public static Assets parseAssets (JsonNode assetsToParse){
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
     * this method parse an Strengths' type from Json file
     * @param strengthsToParse is a JsonNode element "link" to the strength in JSon.txt
     * @return a Strengths' Object type after parsing
     */
    public static Strengths parseStrengths (JsonNode strengthsToParse) {
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

    /**
     * TODO:see params and return
     */
    public static Parser parser (String pathToDirectory){
        Parser parser = new Parser();

        try {
            parser.setCards(cardParser(pathToDirectory + "configuratorCardFile.txt"));
            parser.setBoardAssetsBonus(boardAssetsParser(pathToDirectory + "configuratorBonusAssetsFile.txt"));
            parser.setCouncilBonus(councilParserBonus(pathToDirectory + "configuratorBonusAssetsFile.txt"));
            parser.setCouncilFavors(councilParserFavors(pathToDirectory + "configuratorBonusAssetsFile.txt"));
        }
        catch (Exception e){
            e.printStackTrace();
            //TODO: catch all the exception
        }

        /*ArrayList<Excommunication> excommunications = new ArrayList<>();
        try {
            excommunications = excommunicationParser(pathToDirectory + "configuratorExcomunicationFile.txt");
        }
        catch (){
            //TODO: catch all the exception
        }*/
        return parser;
    }


}
