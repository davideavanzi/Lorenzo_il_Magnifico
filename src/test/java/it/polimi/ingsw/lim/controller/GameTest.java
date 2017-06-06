package it.polimi.ingsw.lim.controller;
import it.polimi.ingsw.lim.model.Game;
import static it.polimi.ingsw.lim.Settings.*;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by ava on 05/06/17.
 */
public class GameTest {

    @Test
    public void newTurnTest(){
        Game game = new Game();
        int age = 1, turn = 1;
        assertEquals(age, game.getAge());
        assertEquals(turn, game.getTurn());
        game.newTurn();
        turn++;
        if(turn > TURNS_PER_AGE) {
            turn = 1;
            age++;
        }
        assertEquals(age, game.getAge());
        assertEquals(turn, game.getTurn());

        game.newTurn();
        turn++;
        if(turn > TURNS_PER_AGE) {
            turn = 1;
            age++;
        }
        assertEquals(age, game.getAge());
        assertEquals(turn, game.getTurn());
    }

}
