package it.polimi.ingsw.lim.exceptions;

/**
 * Created by ava on 26/06/17.
 */
public class ControllerException extends Exception{
    public ControllerException (String error, Throwable cause) { super(error, cause); }
    public ControllerException (String error) { super(error); }
}