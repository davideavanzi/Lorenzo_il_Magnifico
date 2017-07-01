package it.polimi.ingsw.lim.network;

/**
 * Created by ava on 20/06/17.
 * In this class are stored constants for socket communications.
 */
public class ServerConstants {

    public final static String SPLITTER = "|||";
    public final static String SPLITTER_REGEX = "\\|\\|\\|";

    public final static String LOGIN = "LOGIN";
    public final static String LOGIN_RESPONSE = "LOGIN_RESPONSE";
    public final static boolean LOGIN_SUCCESSFUL = true;
    public final static boolean LOGIN_FAILED = false;

    public final static String CHAT = "CHAT";
    public final static String TURN = "TURN";
    public final static String SERVANT = "SERVANT";
}
