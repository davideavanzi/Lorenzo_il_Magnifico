package it.polimi.ingsw.lim.exceptions;

/**
 * Created by nico on 7/4/17.
 */
public class BadRequestException extends Exception {
    public BadRequestException (String error, Throwable cause) { super(error, cause); }
}
