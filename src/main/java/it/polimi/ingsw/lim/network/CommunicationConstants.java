package it.polimi.ingsw.lim.network;

/**
 * This constant are used for communication between server and client.
 */
public class CommunicationConstants {

    public static final String START_GAME = "START_GAME";

    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_REQUEST = "LOGIN_REQUEST";
    public static final String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    public static final String CMD_VALIDATOR = "CMD_VALIDATOR";
    public static final String GAME_MSG = "GAME_MSG";
    public static final String CHAT = "CHAT_CMD";
    public static final String TURN = "TURN_CMD";

    public static final String BOARD = "BOARD_CMD";
    public static final String PLAYERS = "PLAYERS";

    public static final String FAMILY_MEMBER = "FAMILY_MEMBER_CMD";
    public static final String FAMILY_MEMBER_OK = "You have placed family member correctly on board";

    public static final String LEADER_CARD = "LEADER_CARD_CMD";
    public static final String LEADER_CARD_OK = "You have completed a leader card action correctly";

    public static final String CHOOSE_LEADER_DRAFT = "CHOOSE_LEADER_DRAFT";
    public static final String CHOOSE_LEADER_DRAFT_OK = "You have take a leader card";

    public static final String EXCOMMUNICATION = "EXCOMMUNICATION_CMD";
    public static final String EXCOMMUNICATION_OK = "You have been correctly excommunicated";

    public static final String CHOOSE_FAVOR = "CHOOSE_FAVOR_CMD";
    public static final String CHOOSE_FAVOR_OK = "The council's favors have correctly chosen";

    public static final String OPTIONAL_BP_PICK = "OPTIONAL_BP_PICK_CMD";
    public static final String OPTIONAL_BP_PICK_OK = "You have pay a purple card with battle point";

    public static final String CHOOSE_PRODUCTION = "CHOOSE_PRODUCTION_CMD";
    public static final String CHOOSE_PRODUCTION_OK = "The production is started correctly";

    public static final String SERVANTS_PRODUCTION = "SERVANTS_PRODUCTION_CMD";
    public static final String SERVANTS_PRODUCTION_OK = "The server has received your servants-production request correctly";

    public static final String SERVANTS_HARVEST = "SERVANTS_HARVEST_CMD";
    public static final String SERVANTS_HARVEST_OK = "The server has received your servants-harvest request correctly";

    public static final String PICK_FROM_TOWER = "PICK_FROM_TOWER_CMD";
    public static final String PICK_FROM_TOWER_OK = "The server has received your pick card from tower action correctly";

    public static final String END_GAME = "END_GAME";

    /**
     * Leader card command.
     */
    public static final String ACTIVATE_LEADER = "ACTIVATE_LEADER";
    public static final String ACTIVATE_LEADER_OK = "You have activate a leader card correctly";

    public static final String DEPLOY_LEADER = "DEPLOY_LEADER";
    public static final String DEPLOY_LEADER_OK = "You have deploy a leader card correctly";

    public static final String DISCARD_LEADER = "DISCARD_LEADER";
    public static final String DISCARD_LEADER_OK = "You have discard a leader card correctly";

    public static final String LORENZO_MEDICI = "LORENZO_MEDICI";
    public static final String LORENZO_MEDICI_OK = "You have copied correctly the ability of a leader";

    public static final String LUDOVICO_MONTEFELTRO = "LUDOVICO_MONTEFELTRO";
    public static final String LUDOVICO_MONTEFELTRO_OK = "Your familiar value is now set to 6";


    /**
     * Constant for socket place family member method.
     */
    public static final String TOWER = "Tower";
    public static final String PRODUCTION = "Production";
    public static final String HARVEST = "Harvest";
    public static final String COUNCIL = "Council";
    public static final String MARKET = "Market";
    public static final String YELLOW = "Yellow";
    public static final String GREEN = "Green";
    public static final String BLUE = "Blue";
    public static final String PURPLE = "Purple";

}
