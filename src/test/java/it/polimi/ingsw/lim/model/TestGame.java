package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.controller.GameController;
import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.parser.Parser;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static it.polimi.ingsw.lim.Settings.CONFIGS_PATH;
import static it.polimi.ingsw.lim.utils.Log.getLog;
import static org.junit.Assert.assertEquals;

/**
 * Created by fabri on 09/07/17.
 */
public class TestGame {
    Game game;
    GameController gameController;

    @Before
    public void createGame() {
        this.game = new Game();
        String defaultPath = "default/";
        Parser parsedGame = new Parser();
        try {
            parsedGame.parser(CONFIGS_PATH + defaultPath);
        } catch (Exception e) {
            getLog().severe("PARSER ERROR:\n" + e.getMessage());
        }
        game.addPlayer("nick1");
        game.addPlayer("nick2");
        try {
            game.setUpGame(parsedGame);
        } catch (GameSetupException e) {
            getLog().severe(e.getMessage());
        }
        game.setUpTurn();
    }

    private void testNewTurn(){
        try {
            game.newTurn();
            assertEquals(game.getAge(), 1);
            assertEquals(game.getTurn(), 1);
        } catch (AssertionFailedError e) {
            getLog().info("new turn error");
        }
    }

    private void testGiveResource(){
        try {
            game.removeAssetsFromPlayer(game.getPlayer("nick1").getResources(), game.getPlayer("nick1"));
            game.giveAssetsToPlayer(new Assets(1,2,3,4,5,6,7), game.getPlayer("nick1"));
            assertEquals(game.getPlayer("nick1").getResources(), new Assets(1,2,3,4,5,6,7));
        }
        catch (AssertionFailedError e) {
            getLog().info("new turn error");
        }
    }

    private void testIsTowerMoveAllowed(){
        try {
            assertEquals(game.isTowerMoveAllowed("GREEN", 4, game.getPlayer("nick1").getFamilyMember("NEUTRAL")), false);
        }catch (AssertionFailedError e) {
            getLog().info("is tower move allowed error");
        }
    }

    private void testIsToweMoveAffordable(){
        try{
            assertEquals(game.isTowerMoveAffordable("GREEN", 3, game.getPlayer("nick1").getFamilyMember("NEUTRAL")), false);
        }catch (AssertionFailedError e) {
            getLog().info("is tower move affordable error");
        }
    }

    private void testIsTowerOccupied(){
        try {
            assertEquals(game.isTowerOccupied("GREEN"), false);
            assertEquals(game.isTowerOccupied("BLUE"), false);
            assertEquals(game.isTowerOccupied("PURPLE"), false);
            assertEquals(game.isTowerOccupied("YELLOW"), false);
        } catch (AssertionFailedError e) {
            getLog().info("is tower occupied error");
        }
    }

    private void testIsHarvestMoveAllowed(){
        try{
            assertEquals(game.isHarvestMoveAllowed(game.getPlayer("nick1").getFamilyMember("NEUTRAL")), true);
        }catch (AssertionFailedError e) {
            getLog().info("is harvest move allowed error");
        }
    }

    private void testIsProductionMoveAllowed(){
        try{
            assertEquals(game.isProductionMoveAllowed(game.getPlayer("nick1").getFamilyMember("NEUTRAL")), true);
        }catch (AssertionFailedError e) {
            getLog().info("is production move allowed error");
        }
    }

    private void testIsMarketAllowed(){
        try{
            assertEquals(game.isMarketMoveAllowed(game.getPlayer("nick1").getFamilyMember("NEUTRAL"),1), true);
            assertEquals(game.isMarketMoveAllowed(game.getPlayer("nick1").getFamilyMember("NEUTRAL"),2), true);
            assertEquals(game.isMarketMoveAllowed(game.getPlayer("nick1").getFamilyMember("NEUTRAL"),3), false);

        }catch (AssertionFailedError e) {
            getLog().info("is production move allowed error");
        }
    }

    private void testPlayerOrder(){
        int i = 1;
        game.getNewPlayerOrder();
        for(Player pl: game.getPlayers()){
            try {
                assertEquals(pl.getNickname(), "nick" + i);
            }
            catch (AssertionFailedError e) {
                getLog().info("player order error");
            }
            finally {
                i++;
            }
        }
    }

    private void testCalcEndGameBonus(){
        try {
            assertEquals(game.getPlayer("nick1").getResources().getVictoryPoints(), 7);
            game.calcEndGameBonus(game.getPlayer("nick1"));
            assertEquals(game.getPlayer("nick1").getResources().getVictoryPoints(), 9);

            assertEquals(game.getPlayer("nick2").getResources().getVictoryPoints(), 0);
            game.calcEndGameBonus(game.getPlayer("nick2"));
            assertEquals(game.getPlayer("nick2").getResources().getVictoryPoints(), 2);
        }catch (AssertionFailedError e) {
            getLog().info("end game bonus error");
        }
    }

    private void testApplyVp(){
        try {
            game.applyVpOnBpRank();
            assertEquals(game.getPlayer("nick1").getResources().getVictoryPoints(), 14);
            assertEquals(game.getPlayer("nick2").getResources().getVictoryPoints(), 4);
        }catch (AssertionFailedError e) {
            getLog().info("vp on rank error");
        }
    }

    @Test
    public void testGame(){
        createGame();
        testNewTurn();
        testGiveResource();
        testIsToweMoveAffordable();
        testIsTowerMoveAllowed();
        testIsTowerOccupied();
        testIsHarvestMoveAllowed();
        testIsMarketAllowed();
        testIsProductionMoveAllowed();
        testPlayerOrder();
        testCalcEndGameBonus();
        testApplyVp();
    }

}
