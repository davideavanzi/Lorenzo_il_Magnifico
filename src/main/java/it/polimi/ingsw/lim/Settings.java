package it.polimi.ingsw.lim;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ava on 23/05/17.
 * This class holds some settings of the game, not required to be set by users.
 */
public class Settings {
    //misc settings
    public static final int MAX_USERS_NUMBER = 5;
    public static final int FAITH_TRACK_LENGTH = 30;
    public static final int TOWER_HEIGHT = 4;

    //faith points corresponding to the first excommunication.
    //Following excommunications will be at +1 fp from their previosu
    public static final int FIRST_EXCOMM_FP = 3;

    //We can add more cards and decide how long will the game be
    public static final int TURNS_PER_AGE = 2;
    public static final int AGES_NUMBER = 3;

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