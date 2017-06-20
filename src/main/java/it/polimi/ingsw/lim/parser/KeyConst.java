package it.polimi.ingsw.lim.parser;

/**
 * Created by FabCars. This class contains all the key used in the Json file.
 * It is created due to increase scalability of the code (e.g. if the key of the Json file changes)
 */
public class KeyConst {
    //parser
    public static final String MARKET = "MARKET";
    public static final String FAITH_TRACK = "FAITH";
    public static final String COUNCIL_FAVOUR = "FAVOUR";

    public static final String COIN = "coin";
    public static final String WOOD = "wood";
    public static final String STONE = "stone";
    public static final String SERVANTS = "servants";
    public static final String FAITH_POINTS = "faithPoints";
    public static final String BATTLE_POINTS = "battlePoints";
    public static final String VICTORY_POINTS = "victoryPoints";

    public static final String HARVEST_BONUS = "harvestBonus";
    public static final String PRODUCTION_BONUS = "productionBonus";
    public static final String GREEN_BONUS = "greenBonus";
    public static final String BLUE_BONUS = "blueBonus";
    public static final String YELLOW_BONUS = "yellowBonus";
    public static final String PURPLE_BONUS = "purpleBonus";
    public static final String BLACK_BONUS = "blackBonus";
    
    public static final String BONUSES = "bonuses";
    public static final String TOWER_BONUS = "towerBonus";
    public static final String GREEN_TOWER_BONUS = "greenTowerBonus";
    public static final String BLUE_TOWER_BONUS = "blueTowerBonus";
    public static final String YELLOW_TOWER_BONUS = "yellowTowerBonus";
    public static final String PURPLE_TOWER_BONUS = "purpleTowerBonus";
    public static final String BLACK_TOWER_BONUS = "blackTowerBonus";
    public static final String MARKET_BONUS = "marketBonus";
    public static final String FAITH_BONUS = "faithBonus";
    public static final String COUNCIL_FAVOUR_BONUS = "councilFavoursBonus";

    public static final String ACTION_EFFECT = "actionEffect";
    public static final String ACTION_EFFECT_STRENGTH = "actionEffectStrength";
    public static final String ACTION_EFFECT_ASSETS = "actionEffectAssets";
    public static final String ASSETS_EFFECT = "assetsEffect";
    public static final String ASSETS_EFFECT_BONUS = "assetsEffectBonus";
    public static final String COUNCIL_FAVOURS_EFFECT_AMOUNT = "councilFavorsEffectAmount";
    public static final String CARD_MULTIPLIED_EFFECT = "cardMultipliedEffect";
    public static final String CARD_MULTIPLIED_EFFECT_BONUS = "cardMultipliedEffectBonus";
    public static final String CARD_MULTIPLIED_EFFECT_MULTIPLIER_COLOR = "cardMultipliedEffectMultiplierColor";
    public static final String ASSETS_MULTIPLIED_EFFECT = "assetsMultipliedEffect";
    public static final String ASSETS_MULTIPLIED_EFFECT_BONUS = "assetsMultipliedEffectBonus";
    public static final String ASSETS_MULTIPLIED_EFFECT_MULTIPLIER = "assetsMultipliedEffectMultiplier";

    public static final String GREEN_HARVEST_RESULT = "greenHarvestResult";
    public static final String GREEN_ACTION_STRENGTHS = "greenActionStrengths";

    public static final String BLUE_PERMANENT_BONUS = "bluePermanentBonus";
    public static final String BLUE_GREEN_DISCOUNT = "blueGreenDiscount";
    public static final String BLUE_BLUE_DISCOUNT = "blueBlueDiscount";
    public static final String BLUE_YELLOW_DISCOUNT = "blueYellowDiscount";
    public static final String BLUE_PURPLE_DISCOUNT = "bluePurpleDiscount";
    public static final String BLUE_BLACK_DISCOUNT = "blueBlackDiscount";
    public static final String BLUE_TOWER_BONUS_ALLOWED = "blueTowerBonusAllowed";

    public static final String YELLOW_PRODUCTION = "yellowProduction";
    public static final String YELLOW_PRODUCTION_COST = "yellowProductionCost";
    public static final String YELLOW_PRODUCTION_RESULT = "yellowProductionResult";
    public static final String YELLOW_ACTION_STRENGTHS = "yellowActionStrengths";
    public static final String YELLOW_BONUS_MULTIPLIER = "yellowBonusMultiplier";

    public static final String PURPLE_END_GAME_BONUS = "purpleEndGameBonus";
    public static final String PURPLE_OPTIONAL_BATTLE_POINTS_REQUIREMENT = "purpleOptionalBattlePointsRequirement";
    public static final String PURPLE_OPTIONAL_BATTLE_POINTS_COST = "purpleOptionalBattlePointsCost";

    public static final String CARD = "card";
    public static final String CARD_NAME = "cardName";
    public static final String CARD_AGE = "cardAge";
    public static final String CARD_TYPE = "cardType";
    public static final String CARD_ASSETS_COST = "cardAssetsCost";
    public static final String CARD_IMMEDIATE_EFFECT = "cardImmediateEffect";
    public static final String GREEN_CARD = "greenCard";
    public static final String BLUE_CARD = "blueCard";
    public static final String YELLOW_CARD = "yellowCard";
    public static final String PURPLE_CARD = "purpleCard";
    public static final String BLACK_CARD = "blackCard";

    public static final String COUNCIL_BONUS = "councilBonus";
    public static final String COUNCIL_FAVOURS = "councilFavours";
    public static final String STARTING_GAME_BONUS = "startingGameBonus";

    public static final String EXCOMMUNICATION_TYPE = "excommunicationType";
    public static final String ASSETS_MALUS_EXCOMMUNICATION = "assetsMalusExcommunication";
    public static final String STRENGTH_MALUS_EXCOMMUNICATION = "strengthMalusExcommunication";
    public static final String MARKET_EXCOMMUNICATION = "marketExcommunication";
    public static final String SERVANTS_EXCOMMUNICATION = "servantsExcommunication";
    public static final String TURN_EXCOMMUNICATION = "turnExcommunication";
    public static final String END_GAME_EXCOMMUNICATION = "endGameExcommunication";
    public static final String PRODUCTION_CARD_COST_MALUS = "productionCardCostMalus";
    public static final String ON_ASSETS_MALUS = "onAssetsMalus";

    public static final String EXCOMMUNICATIONS = "excommunications";

    public static final String CONFIGURATOR_CARD_FILE_NAME = "configuratorCardFile.json";
    public static final String CONFIGURATOR_BONUS_ASSETS_FILE_NAME = "configuratorBonusesAssetsFile.json";
    public static final String CONFIGURATOR_EXCOMMUNICATION_FILE_NAME = "configuratorExcommunicationsFile.json";

    public static final String BOARD_PLAYER_PRODUCTION_BONUS = "boardPlayerProductionBonus";
    public static final String BOARD_PLAYER_HARVEST_BONUS = "boardPlayerHarvestBonus";

    //writer
    public static final String PATH_TO_WRITER_FILE = "src/test/writer.json";
}
