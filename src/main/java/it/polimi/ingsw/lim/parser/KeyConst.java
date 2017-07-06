package it.polimi.ingsw.lim.parser;

/**
 * Created by FabCars. This class contains all the key used in the Json file.
 * It is created due to increase scalability of the code (e.g. if the key of the Json file changes)
 */
class KeyConst {
    //parser
    protected static final String MARKET = "MARKET";
    protected static final String FAITH_TRACK = "FAITH";
    protected static final String COUNCIL_FAVOUR = "FAVOUR";

    protected static final String COIN = "coin";
    protected static final String WOOD = "wood";
    protected static final String STONE = "stone";
    protected static final String SERVANTS = "servants";
    protected static final String FAITH_POINTS = "faithPoints";
    protected static final String BATTLE_POINTS = "battlePoints";
    protected static final String VICTORY_POINTS = "victoryPoints";

    protected static final String HARVEST_BONUS = "harvestBonus";
    protected static final String PRODUCTION_BONUS = "productionBonus";
    protected static final String GREEN_BONUS = "greenBonus";
    protected static final String BLUE_BONUS = "blueBonus";
    protected static final String YELLOW_BONUS = "yellowBonus";
    protected static final String PURPLE_BONUS = "purpleBonus";
    protected static final String BLACK_BONUS = "blackBonus";
    protected static final String WHITE_DICE_BONUS = "witheDiceBonus";
    protected static final String BLACK_DICE_BONUS = "blackDiceBonus";
    protected static final String ORANGE_DICE_BONUS = "orangeDiceBonus";

    protected static final String BONUSES = "bonuses";
    protected static final String TOWER_BONUS = "towerBonus";
    protected static final String GREEN_TOWER_BONUS = "greenTowerBonus";
    protected static final String BLUE_TOWER_BONUS = "blueTowerBonus";
    protected static final String YELLOW_TOWER_BONUS = "yellowTowerBonus";
    protected static final String PURPLE_TOWER_BONUS = "purpleTowerBonus";
    protected static final String BLACK_TOWER_BONUS = "blackTowerBonus";
    protected static final String MARKET_BONUS = "marketBonus";
    protected static final String FAITH_BONUS = "faithBonus";
    protected static final String COUNCIL_FAVOUR_BONUS = "councilFavoursBonus";

    protected static final String ACTION_EFFECT = "actionEffect";
    protected static final String ACTION_EFFECT_STRENGTH = "actionEffectStrength";
    protected static final String ACTION_EFFECT_ASSETS = "actionEffectAssets";
    protected static final String ASSETS_EFFECT = "assetsEffect";
    protected static final String ASSETS_EFFECT_BONUS = "assetsEffectBonus";
    protected static final String COUNCIL_FAVOURS_EFFECT_AMOUNT = "councilFavorsEffectAmount";
    protected static final String CARD_MULTIPLIED_EFFECT = "cardMultipliedEffect";
    protected static final String CARD_MULTIPLIED_EFFECT_BONUS = "cardMultipliedEffectBonus";
    protected static final String CARD_MULTIPLIED_EFFECT_MULTIPLIER_COLOR = "cardMultipliedEffectMultiplierColor";
    protected static final String ASSETS_MULTIPLIED_EFFECT = "assetsMultipliedEffect";
    protected static final String ASSETS_MULTIPLIED_EFFECT_BONUS = "assetsMultipliedEffectBonus";
    protected static final String ASSETS_MULTIPLIED_EFFECT_MULTIPLIER = "assetsMultipliedEffectMultiplier";

    protected static final String GREEN_HARVEST_RESULT = "greenHarvestResult";
    protected static final String GREEN_ACTION_STRENGTHS = "greenActionStrengths";

    protected static final String BLUE_PERMANENT_BONUS = "bluePermanentBonus";
    protected static final String BLUE_GREEN_DISCOUNT = "blueGreenDiscount";
    protected static final String BLUE_BLUE_DISCOUNT = "blueBlueDiscount";
    protected static final String BLUE_YELLOW_DISCOUNT = "blueYellowDiscount";
    protected static final String BLUE_PURPLE_DISCOUNT = "bluePurpleDiscount";
    protected static final String BLUE_BLACK_DISCOUNT = "blueBlackDiscount";
    protected static final String BLUE_TOWER_BONUS_ALLOWED = "blueTowerBonusAllowed";

    protected static final String YELLOW_PRODUCTION = "yellowProduction";
    protected static final String YELLOW_PRODUCTION_COST = "yellowProductionCost";
    protected static final String YELLOW_PRODUCTION_RESULT = "yellowProductionResult";
    protected static final String YELLOW_ACTION_STRENGTHS = "yellowActionStrengths";
    protected static final String YELLOW_BONUS_MULTIPLIER = "yellowBonusMultiplier";

    protected static final String PURPLE_END_GAME_BONUS = "purpleEndGameBonus";
    protected static final String PURPLE_OPTIONAL_BATTLE_POINTS_REQUIREMENT = "purpleOptionalBattlePointsRequirement";
    protected static final String PURPLE_OPTIONAL_BATTLE_POINTS_COST = "purpleOptionalBattlePointsCost";

    protected static final String CARD = "card";
    protected static final String CARD_NAME = "cardName";
    protected static final String CARD_AGE = "cardAge";
    protected static final String CARD_TYPE = "cardType";
    protected static final String CARD_ASSETS_COST = "cardAssetsCost";
    protected static final String CARD_IMMEDIATE_EFFECT = "cardImmediateEffect";
    protected static final String GREEN_CARD = "greenCard";
    protected static final String BLUE_CARD = "blueCard";
    protected static final String YELLOW_CARD = "yellowCard";
    protected static final String PURPLE_CARD = "purpleCard";
    protected static final String BLACK_CARD = "blackCard";

    protected static final String COUNCIL_BONUS = "councilBonus";
    protected static final String COUNCIL_FAVOURS = "councilFavours";
    protected static final String STARTING_GAME_BONUS = "startingGameBonus";

    protected static final String EXCOMMUNICATION_TYPE = "excommunicationType";
    protected static final String ASSETS_MALUS_EXCOMMUNICATION = "assetsMalusExcommunication";
    protected static final String STRENGTH_MALUS_EXCOMMUNICATION = "strengthMalusExcommunication";
    protected static final String MARKET_EXCOMMUNICATION = "marketExcommunication";
    protected static final String SERVANTS_EXCOMMUNICATION = "servantsExcommunication";
    protected static final String TURN_EXCOMMUNICATION = "turnExcommunication";
    protected static final String END_GAME_ASSETS_EXCOMMUNICATION = "endGameAssetsExcommunication";
    protected static final String END_GAME_CARDS_EXCOMMUNICATION = "endGameCardsExcommunication";
    protected static final String PRODUCTION_CARD_COST_MALUS = "productionCardCostMalus";
    protected static final String ON_ASSETS_MALUS = "onAssetsMalus";
    protected static final String BLOCKED_CARD_COLOR = "blockedCardColor";

    protected static final String EXCOMMUNICATIONS = "excommunications";

    protected static final String CONFIGURATOR_CARD_FILE_NAME = "configuratorCardFile.json";
    protected static final String CONFIGURATOR_BONUS_ASSETS_FILE_NAME = "configuratorBonusesAssetsFile.json";
    protected static final String CONFIGURATOR_EXCOMMUNICATION_FILE_NAME = "configuratorExcommunicationsFile.json";
    protected static final String CONFIGURATOR_TIMERS_FILE_NAME = "configuratorTimersFile.json";

    protected static final String BOARD_PLAYER_PRODUCTION_BONUS = "boardPlayerProductionBonus";
    protected static final String BOARD_PLAYER_HARVEST_BONUS = "boardPlayerHarvestBonus";

    protected static final String TIMERS = "timers";
    protected static final String START_GAME_TIMER = "startGameTimer";
    protected static final String PLAY_MOVE_TIMER = "playMoveTimer";
    
    //writer
    protected static final String PATH_TO_WRITER_BOARD_FILE = "src/main/gameData/configs/writer/boardWriter";
    protected static final String PATH_TO_WRITER_ROOM_FILE = "src/main/gameData/configs/writer/roomWriter";
    protected static final String EXTENTION = ".json";
}
