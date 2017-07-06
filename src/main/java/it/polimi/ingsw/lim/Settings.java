package it.polimi.ingsw.lim;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ava on 23/05/17.
 * This class holds some settings of the game, not required to be set by users.
 */
public class Settings {
    //misc settings
    public static final int MAX_USERS_PER_ROOM = 5;
    public static final int FAITH_TRACK_LENGTH = 30;
    public static final int TOWER_HEIGHT = 4;
    public static final int[] FLOORS_ACTION_COSTS = {1, 3, 5, 7};
    public static final int[] PLAYER_TERRITORIES_REQ = {0, 0, 3, 7, 12, 18};
    public static final int COUNCIL_FAVUORS_TYPES = 5;
    public static final int MARKET_MAX_SIZE = 5;
    public static final int MARKET_ACTION_COST = 1;
    public static final int NEUTRAL_FM_STRENGTH = 0;
    public static final int DEPLOYABLE_FM_PER_ROUND = 1;

    //faith points corresponding to the first excommunication.
    //Following excommunications will be at +1 fp from their previous
    public static final int FIRST_EXCOMM_FP = 3;
    public static final int COINS_TO_ENTER_OCCUPIED_TOWER = 3;

    public static final int ROUNDS_PER_TURN = 4;
    public static final int TURNS_PER_AGE = 2;
    public static final int AGES_NUMBER = 3;

    public static final int HARVEST_DEFAULT_STR = 1;
    public static final int HARVEST_STR_MALUS = 3;
    public static final int HARVEST_DEFAULTSPACE_SIZE = 1;

    public static final int PRODUCTION_DEFAULT_STR = 1;
    public static final int PRODUCTION_STR_MALUS = 3;
    public static final int PRODUCTION_DEFAULTSPACE_SIZE = 1;


    public static final int[] ENDGAME_GREEN_CARDS_VP_BONUS = {0, 0, 1, 4, 10, 20};
    public static final int[] ENDGAME_BLUE_CARDS_VP_BONUS = {1, 3, 6, 10, 15, 21};
    public static final int ENDGAME_VP_ASSETS_DIVIDER = 5;
    public static final int ENDGAME_FIRSTVP_BONUS = 5;
    public static final int ENDGAME_SECONDVP_BONUS = 2;

    //Color strings used in the code.
    //These should prevent errors setting a standard name to all fixed string variables.
    public static final String GREEN_COLOR = "GREEN";
    public static final String BLUE_COLOR = "BLUE";
    public static final String YELLOW_COLOR = "YELLOW";
    public static final String PURPLE_COLOR = "PURPLE";
    public static final String BLACK_COLOR = "BLACK";
    public static final String ORANGE_COLOR = "ORANGE";
    public static final String WHITE_COLOR = "WHITE";
    public static final String NEUTRAL_COLOR = "NEUTRAL";
    public static final String RED_COLOR = "RED";

    //These are utilized by lambda functions
    public static final List<String> FM_COLORS = Arrays.asList(BLACK_COLOR, ORANGE_COLOR, WHITE_COLOR, NEUTRAL_COLOR);
    public static final List<String> DICE_COLORS = FM_COLORS.subList(0,3);
    public static final List<String> DEFAULT_TOWERS_COLORS =
            Arrays.asList(GREEN_COLOR, BLUE_COLOR, YELLOW_COLOR, PURPLE_COLOR);

    public static final List<String> PLAYER_COLORS =
            Arrays.asList(GREEN_COLOR, YELLOW_COLOR, BLUE_COLOR, RED_COLOR, PURPLE_COLOR);

    //PATHS
    protected static final String LOG_PATH = "src/main/gameData/logs/";
    public static final String CONFIGS_PATH = "src/main/gameData/configs/";



}