package it.polimi.ingsw.lim.exceptions;

/**
 * Created by nico.
 */

public class ClientNetworkException extends Exception {
    public ClientNetworkException (String error, Throwable cause) { super(error, cause); }
}
