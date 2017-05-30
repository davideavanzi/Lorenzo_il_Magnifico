package it.polimi.ingsw.lim.exceptions;

/**
 * Created by FabCars.
 */
public class InvalidCardException extends Exception {
    public InvalidCardException (String errorMessage){
        super(errorMessage);
    }
}
