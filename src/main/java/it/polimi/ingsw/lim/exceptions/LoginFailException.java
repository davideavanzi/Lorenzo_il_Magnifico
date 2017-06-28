package it.polimi.ingsw.lim.exceptions;

/**
 * Created by fabri on 28/06/17.
 */
public class LoginFailException extends Exception{
    public LoginFailException(String error){ super(error); }
}