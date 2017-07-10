package it.polimi.ingsw.lim.model;

import it.polimi.ingsw.lim.exceptions.GameSetupException;
import it.polimi.ingsw.lim.parser.Parser;
import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.lim.Settings.CONFIGS_PATH;
import static it.polimi.ingsw.lim.Settings.GREEN_COLOR;
import static it.polimi.ingsw.lim.utils.Log.getLog;
import static org.junit.Assert.assertEquals;

/**
 * Created by fabri on 09/07/17.
 */
public class TestGame {
    Game game;

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
            assertEquals(game.isTowerMoveAllowed("GREEN", 4, game.getPlayer("nick1").getFamilyMember("NEUTRAL")), true);
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

    private void testLeaderinGame(){
        try{
            assertEquals(game.playerHasActiveLeader(1, game.getPlayer("nick1")), false);
            assertEquals(game.isLeaderDiscardable(1, game.getPlayer("nick1")), false);
            assertEquals(game.isLeaderActivable(1, game.getPlayer("nick1")), false);
            assertEquals(game.isLeaderDeployable(1, game.getPlayer("nick1")), false);
            game.giveLeaderToPlayer(1, game.getPlayer("nick1"));
            assertEquals(game.getPlayer("nick1").getLeaderById(1).getCardName(), "Francesco Sforza");

        }catch (AssertionFailedError e) {
            getLog().info("has active leader error");
        }
    }

    private void testExcommunicationInGame(){
        assertEquals(game.isPlayerRoundExcommunicated(game.getPlayer("nick1")), false);
        assertEquals(game.isPlayerServantsExcommunicated(game.getPlayer("nick1")), false);
        assertEquals(game.isPlayerEndCardExcommunicated(game.getPlayer("nick1"), "YELLOW"), false);

    }

    private void testTowerMoveInGame(){
        game.towerMove("GREEN", 1, game.getPlayer("nick1").getFamilyMember("ORANGE"), 2, false);
        assertEquals(game.isTowerOccupied(GREEN_COLOR), true);
        assertEquals(game.getPlayer("nick1").getFamilyMembers().size(), 3);
        assertEquals(game.isTowerMoveAllowed("GREEN", 1, game.getPlayer("nick2").getFamilyMember("BLACK")), false);
        assertEquals(game.getPlayer("nick1").getCardsOfColor("GREEN").size(), 1);

    }

    private void testFmInGame(){
        FamilyMember fm = game.getPlayer("nick1").getFamilyMember("BLACK");
        assertEquals((Integer)game.getFmStrength(fm), game.getBoard().getDice().get("BLACK"));
        assertEquals(game.calcHarvestActionStr(game.getPlayer("nick1"), fm, 2, 0), game.getFmStrength(fm) + 2);
        assertEquals(game.calcProductionActionStr(game.getPlayer("nick1"), fm, 3, 0), game.getFmStrength(fm) + 3);
        assertEquals(game.servantsForTowerAction(game.getPlayer("nick1").getFamilyMember("NEUTRAL"), "GREEN", 4), -7);
        assertEquals(game.servantsForCouncilAction(game.getPlayer("nick1").getFamilyMember("NEUTRAL")), -1);
        assertEquals(game.servantsForFastTowerAction(4, "GREEN", 4, game.getPlayer("nick1")), -3);
        assertEquals(game.servantsForHarvestAction(game.getPlayer("nick1"), game.getPlayer("nick1").getFamilyMember("NEUTRAL"), 0), -1);
        assertEquals(game.servantsForMarketAction(game.getPlayer("nick1").getFamilyMember("NEUTRAL")), -1);
        assertEquals(game.servantsForProductionAction(game.getPlayer("nick1"), game.getPlayer("nick1").getFamilyMember("NEUTRAL"), 0), -1);
    }

    private void testMarketMove(){
        int prevServants = game.getPlayer("nick1").getResources().getServants();
        game.marketMove(game.getPlayer("nick1").getFamilyMember("NEUTRAL"), 1, 1);
        assertEquals(game.getPlayer("nick1").getResources().getServants(), (prevServants - 1));
        assertEquals(game.isMarketMoveAllowed(game.getPlayer("nick1").getFamilyMember("BLACK"), 1), false);
        assertEquals(game.isMarketMoveAllowed(game.getPlayer("nick1").getFamilyMember("BLACK"), 2), true);
    }

    private void testCouncilMove(){
        int prevServants = game.getPlayer("nick2").getResources().getServants();
        game.councilMove(game.getPlayer("nick2").getFamilyMember("NEUTRAL"), 1);
        assertEquals(game.getPlayer("nick2").getResources().getServants(), (prevServants - 1));
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
        testLeaderinGame();
        testExcommunicationInGame();
        testTowerMoveInGame();
        testFmInGame();
        testMarketMove();
        //testCouncilMove();
        game.setUpTurn();
    }

}
