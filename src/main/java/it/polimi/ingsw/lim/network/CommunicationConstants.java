package it.polimi.ingsw.lim.network;

/**
 * Created by ava on 20/06/17.
 * In this class are stored constants for socket communications.
 */
public class CommunicationConstants {

    public static final String SPLITTER = "|||";
    public static final String SPLITTER_REGEX = "\\|\\|\\|";

    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_REQUEST = "LOGIN_REQUEST";
    public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static final String CMD_VALIDATOR = "CMD_VALIDATOR";
    public static final String GAME_MSG = "GAME_MSG";
    public static final String CHAT = "CHAT";
    public static final String TURN = "TURN";

    public static final String FAMILY_MEMBER = "FAMILY_MEMBER";
    public static final String FAMILY_MEMBER_OK = "Family member has been correctly place";

    public static final String EXCOMMUNICATION = "EXCOMMUNICATION";
    public static final String EXCOMMUNICATION_OK = "You have been correctly excommunicated";

    public static final String CHOOSE_FAVOR = "CHOOSE_FAVOR";
    public static final String CHOOSE_FAVOR_OK = "The council's favors have correctly chosen";

    public static final String CHOOSE_PRODUCTION = "CHOOSE_PRODUCTION";
    public static final String CHOOSE_PRODUCTION_OK = "The production is started correctly";

    public static final String OPTIONAL_BP_PICK = "OPTIONAL_BP_PICK";
    public static final String OPTIONAL_BP_PICK_OK = "You have pay a purple card with battle point";

    public static final String TOWER = "Tower";
    public static final String PRODUCTION = "Production";
    public static final String HARVEST = "Harvest";
    public static final String COUNCIL = "Council";
    public static final String MARKET = "Market";

}
