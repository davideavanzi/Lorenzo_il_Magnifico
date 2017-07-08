package it.polimi.ingsw.lim.network;

/**
 * This constant are used for communication between server and client.
 */
public class CommunicationConstants {

    /**
     * Used for login.
     */

    public static final String START_GAME = "START_GAME";

    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_REQUEST = "LOGIN_REQUEST";
    public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static final String CMD_VALIDATOR = "CMD_VALIDATOR";
    public static final String GAME_MSG = "GAME_MSG";
    public static final String CHAT = "CHAT";
    public static final String TURN = "TURN";

    public static final String BOARD = "BOARD";
    public static final String PLAYERS = "PLAYERS";

    public static final String FAMILY_MEMBER = "FAMILY_MEMBER";
    public static final String FAMILY_MEMBER_OK = "You have placed family member correctly on board";

    public static final String LEADER_CARD = "LEADER_CARD";
    public static final String LEADER_CARD_OK = "You have completed a leader card action correctly";

    public static final String EXCOMMUNICATION = "EXCOMMUNICATION";
    public static final String EXCOMMUNICATION_OK = "You have been correctly excommunicated";

    public static final String CHOOSE_FAVOR = "CHOOSE_FAVOR";
    public static final String CHOOSE_FAVOR_OK = "The council's favors have correctly chosen";

    public static final String OPTIONAL_BP_PICK = "OPTIONAL_BP_PICK";
    public static final String OPTIONAL_BP_PICK_OK = "You have pay a purple card with battle point";

    public static final String CHOOSE_PRODUCTION = "CHOOSE_PRODUCTION";
    public static final String CHOOSE_PRODUCTION_OK = "The production is started correctly";

    public static final String SERVANTS_PRODUCTION = "SERVANTS_PRODUCTION";
    public static final String SERVANTS_PRODUCTION_OK = "The server has received your servants-production request correctly";

    public static final String SERVANTS_HARVEST = "SERVANTS_HARVEST";
    public static final String SERVANTS_HARVEST_OK = "The server has received your servants-harvest request correctly";

    public static final String PICK_FROM_TOWER = "PICK_FROM_TOWER";
    public static final String PICK_FROM_TOWER_OK = "The server has received your pick card from tower action correctly";

    /**
     * Constant for socket place family member method.
     */
    public static final String TOWER = "Tower";
    public static final String PRODUCTION = "Production";
    public static final String HARVEST = "Harvest";
    public static final String COUNCIL = "Council";
    public static final String MARKET = "Market";
}
