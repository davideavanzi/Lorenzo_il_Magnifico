package it.polimi.ingsw.lim.exceptions;

/**
 * This Exception is thrown by the game controller when user gives wrong input
 */
public class BadRequestException extends Exception {
    public BadRequestException (String error, Throwable cause) { super(error, cause); }
    public BadRequestException (String error) { super(error); }
}
