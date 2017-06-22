package it.polimi.ingsw.lim.network;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

/**
 * Created by ava on 20/06/17.
 * In this class are stored constants for socket communications.
 */
public class SocketConstants {

    public final static String SPLITTER = "|||";
    public final static String SPLITTER_REGEX = "\\|\\|\\|";

    public final static String ASK_SERVANTS_AMOUNT = "ASK_SERVANTS_AMOUNT";
    public final static String ANSWER_SERVANTS_AMOUNT = "ANSWER_SERVANTS_AMOUNT";
    public final static String LOGIN = "LOGIN";
    public final static String LOGIN_SUCCESSFUL = "LOGIN SUCCESSFUL";
    public final static String CHAT = "CHAT";
    public final static String GET_ASSETS = "GET_ASSETS";
    public final static String ANSWER_GET_ASSETS = "ANSWER_GET_ASSETS";
}
