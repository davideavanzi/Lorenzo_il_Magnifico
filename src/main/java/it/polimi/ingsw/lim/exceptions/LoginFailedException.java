package it.polimi.ingsw.lim.exceptions;

/**
 * Created by fabri on 28/06/17.
 */
public class LoginFailedException extends Exception{
    public LoginFailedException(String error){ super(error); }
}