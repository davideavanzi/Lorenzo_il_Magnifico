package it.polimi.ingsw.lim.network;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 * Created by ava on 20/06/17.
 * In this class are stored constants for socket communications.
 */
public class SocketConstants {

    public final static String SPLITTER = "|||";
    public final static String SPLITTER_REGEX = "\\|\\|\\|";

    public final static String LOGIN = "LOGIN";
    public final static String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    public final static String CHAT = "CHAT";
    public final static String TURN = "TURN";
}
