package it.polimi.ingsw.lim.controller;
import it.polimi.ingsw.lim.model.FamilyMember;
import it.polimi.ingsw.lim.model.Game;

import static it.polimi.ingsw.lim.Log.getLog;
import static it.polimi.ingsw.lim.Settings.*;

import it.polimi.ingsw.lim.model.Player;
import it.polimi.ingsw.lim.parser.Parser;
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

    /*Player has not yet been given resources to perform following actions
    TODO: FIX (maybe create a player with fake resources?)

    @Test
    public void isHarvestMoveAllowedTest() {
        Game game = new Game();
        game.rollDices();
        game.addPlayer(new Player("CIAONE", GREEN_COLOR));
        game.addPlayer(new Player("HOLAONE", RED_COLOR));
        FamilyMember bFm = new FamilyMember(BLACK_COLOR, GREEN_COLOR);
        FamilyMember wFm = new FamilyMember(WHITE_COLOR, GREEN_COLOR);
        FamilyMember nFm = new FamilyMember(NEUTRAL_COLOR, GREEN_COLOR);
        FamilyMember bFm2 = new FamilyMember(BLACK_COLOR, RED_COLOR);
        FamilyMember wFm3 = new FamilyMember(WHITE_COLOR, BLUE_COLOR);
        game.addToHarvest(bFm);
        assertEquals(1, game.getHarvest().size());
        assertFalse(game.isHarvestMoveAllowed(wFm3));
        game.addPlayer(new Player("CYKA BLYAT", BLUE_COLOR));
        assertFalse(game.isHarvestMoveAllowed(wFm));
        assertFalse(game.isHarvestMoveAllowed(nFm));
        game.addToHarvest(nFm);
        assertTrue(game.isHarvestMoveAllowed(bFm2));
        assertTrue(game.isHarvestMoveAllowed(wFm3));
        assertFalse(game.isHarvestMoveAllowed(nFm));
    }

    @Test
    public void isProductionMoveAllowedTest() {
        Game game = new Game();
        game.addPlayer(new Player("CIAONE", GREEN_COLOR));
        game.addPlayer(new Player("HOLAONE", RED_COLOR));
        FamilyMember bFm = new FamilyMember(BLACK_COLOR, GREEN_COLOR);
        FamilyMember wFm = new FamilyMember(WHITE_COLOR, GREEN_COLOR);
        FamilyMember nFm = new FamilyMember(NEUTRAL_COLOR, GREEN_COLOR);
        FamilyMember bFm2 = new FamilyMember(BLACK_COLOR, RED_COLOR);
        FamilyMember wFm3 = new FamilyMember(WHITE_COLOR, BLUE_COLOR);
        game.addToProduction(bFm);
        assertEquals(1, game.getProduction().size());
        assertFalse(game.isProductionMoveAllowed(wFm3));
        game.addPlayer(new Player("CYKA BLYAT", BLUE_COLOR));
        assertFalse(game.isProductionMoveAllowed(wFm));
        //assertFalse(game.isProductionMoveAllowed(nFm)); //Player has not enough servants to perform the action
        game.addToProduction(nFm);
        assertTrue(game.isProductionMoveAllowed(bFm2));
        assertTrue(game.isProductionMoveAllowed(wFm3));
        assertFalse(game.isProductionMoveAllowed(nFm));
    }
    */
}
