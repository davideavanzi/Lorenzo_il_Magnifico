package it.polimi.ingsw.lim.exceptions;

/**
 * Created by nico.
 */
public class ServerNetworkException extends Exception {
    public ServerNetworkException (String error, Throwable cause) { super(error, cause); }
}
