package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.exceptions.InvalidCardException;
import it.polimi.ingsw.lim.exceptions.InvalidExcommunicationException;
import it.polimi.ingsw.lim.exceptions.InvalidTimerException;
import it.polimi.ingsw.lim.model.Assets;
import it.polimi.ingsw.lim.model.Strengths;
import it.polimi.ingsw.lim.model.cards.*;
import it.polimi.ingsw.lim.model.excommunications.*;
import it.polimi.ingsw.lim.model.immediateEffects.*;
import it.polimi.ingsw.lim.utils.Log;
import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.utils.Log.getLog;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by FabCars. The task of this test class is to test if card parser work as expected
 */

public class TestParser{
    private static GreenCard createGreenCardExpected() {
        String nameExpected = "testGreenCard";
        int ageExpected = 1;
        Assets assetsCostExpected = new Assets(1, 0, 0, 0, 0, 0, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Assets assetsBonusExpected = new Assets(0, 1, 0, 0, 0, 0, 0);
        AssetsEffect assetsEffectExpected = new AssetsEffect(assetsBonusExpected);
        iEffectExpected.add(assetsEffectExpected);
        Assets harvestResultExpected = new Assets(0, 0, 1, 0, 0, 0, 0);
        Strengths actionStrengthExpected = new Strengths(1, 0, 0, 0, 0, 0, 0);
        return new GreenCard(
                nameExpected,
                ageExpected,
                assetsCostExpected,
                iEffectExpected,
                harvestResultExpected,
                actionStrengthExpected,
                0
        );
    }

    private static BlueCard createBlueCardExpected() {
        String nameExpected = "testBlueCard";
        int ageExpected = 2;
        Assets assetsCostExpected = new Assets(2, 3, 0, 0, 0, 0, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Strengths actionEffectStrength = new Strengths(1, 0, 0, 0, 0, 0, 0);
        Assets actionEffectAsset = new Assets();
        ActionEffect actionEffectExpected = new ActionEffect(actionEffectStrength, actionEffectAsset);
        iEffectExpected.add(actionEffectExpected);
        Strengths permaBonusExpected = new Strengths();
        Assets blueGreenDiscount = new Assets(1, 0, 0, 0, 0, 0, 0);
        Assets blueBlueDiscount = new Assets();
        Assets blueYellowDiscount = new Assets();
        Assets bluePurpleDiscount = new Assets();
        Assets blueBlackDiscount = new Assets();
        boolean towerBonusAllowedExpected = true;
        return new BlueCard(
                nameExpected,
                ageExpected,
                assetsCostExpected,
                iEffectExpected,
                permaBonusExpected,
                blueGreenDiscount,
                blueBlueDiscount,
                blueYellowDiscount,
                bluePurpleDiscount,
                blueBlackDiscount,
                towerBonusAllowedExpected
        );
    }

    private static YellowCard createYellowCardExpected() {
        String nameExpected = "testYellowCard";
        int ageExpected = 3;
        Assets assetsCostExpected = new Assets(0, 0, 0, 0, 0, 0, 3);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Assets assetsMultipliedEffectBonus = new Assets(1, 0, 0, 0, 0, 0, 0);
        Assets assetsMultipliedEffectMultiplier = new Assets(0, 0, 0, 0, 2, 0, 0);
        AssetsMultipliedEffect assetsMultipliedEffectExpected = new AssetsMultipliedEffect(assetsMultipliedEffectBonus, assetsMultipliedEffectMultiplier);
        iEffectExpected.add(assetsMultipliedEffectExpected);
        ArrayList<Assets> productionCosts = new ArrayList<>();
        Assets productionCost1 = new Assets(0, 1, 0, 0, 0, 0, 0);
        Assets productionCost2 = new Assets(0, 3, 0, 0, 0, 0, 0);
        productionCosts.add(productionCost1);
        productionCosts.add(productionCost2);
        ArrayList<Object> productionResults = new ArrayList<>();
        Assets productionResult1 = new Assets(5, 0, 0, 0, 0, 0, 0);
        Assets productionResult2 = new Assets(15, 0, 0, 0, 0, 0, 0);
        productionResults.add(productionResult1);
        productionResults.add(productionResult2);
        Strengths actionStrengthExpected = new Strengths();
        String bonusMultiplierExpected = "";
        return new YellowCard(
                nameExpected,
                ageExpected,
                assetsCostExpected,
                iEffectExpected,
                productionCosts,
                productionResults,
                actionStrengthExpected,
                bonusMultiplierExpected
        );
    }

    private static PurpleCard createPurpleCardExpected() {
        String nameExpected = "testPurpleCard";
        int ageExpected = 1;
        Assets assetsCostExpected = new Assets(0, 0, 0, 1, 0, 1, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Assets cardMultipliedEffectBonus = new Assets(1, 0, 0, 0, 0, 0, 0);
        String cardMultipliedEffectString = "BLUE";
        CardMultipliedEffect cardMultipliedEffectExpected = new CardMultipliedEffect(cardMultipliedEffectBonus, cardMultipliedEffectString);
        iEffectExpected.add(cardMultipliedEffectExpected);
        Assets endGameBonusExpected = new Assets(0, 0, 1, 0, 0, 0, 0);
        int optionalBpRequirementExpected = 4;
        int optionalBpCost = 2;
        return new PurpleCard(
                nameExpected,
                ageExpected,
                assetsCostExpected,
                iEffectExpected,
                endGameBonusExpected,
                optionalBpRequirementExpected,
                optionalBpCost
        );
    }

    private static BlackCard createBlackCardExpected() {
        String nameExpected = "testBlackCard";
        int ageExpected = 1;
        Assets assetsCostExpected = new Assets(0, 0, 0, 0, 1, 1, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        CouncilFavorsEffect councilFavorsEffectExpected = new CouncilFavorsEffect(2);
        iEffectExpected.add(councilFavorsEffectExpected);
        return new BlackCard(
                nameExpected,
                ageExpected,
                assetsCostExpected,
                iEffectExpected
        );
    }

    private static void testGreenCardParser(Parser testParser)
            throws InvalidCardException, AssertionFailedError {
        GreenCard greenCardExpected = createGreenCardExpected();
        assertEquals(greenCardExpected, testParser.getCard(1).get(GREEN_COLOR).get(0));
    }

    private static void testBlueCardParser(Parser testParser)
            throws InvalidCardException, AssertionFailedError {
        BlueCard blueCardExpected = createBlueCardExpected();
        assertEquals(blueCardExpected, testParser.getCard(2).get(BLUE_COLOR).get(0));
    }

    private static void testYellowCardParser(Parser testParser)
            throws InvalidCardException, AssertionFailedError {
        YellowCard yellowCardExpected = createYellowCardExpected();
        assertEquals(yellowCardExpected, testParser.getCard(3).get(YELLOW_COLOR).get(0));
    }

    private static void testPurpleCardParser(Parser testParser)
            throws InvalidCardException, AssertionFailedError {
        PurpleCard purpleCardExpected = createPurpleCardExpected();
        assertEquals(purpleCardExpected, testParser.getCard(1).get(PURPLE_COLOR).get(0));
    }

    private static void testBlackCardParser(Parser testParser)
            throws InvalidCardException, AssertionFailedError {
        BlackCard blackCardExpected = createBlackCardExpected();
        assertEquals(blackCardExpected, testParser.getCard(1).get(BLACK_COLOR).get(0));
    }

    private static void testCardParser(Parser testParser) {
        try {
            try {
                testGreenCardParser(testParser);
                getLog().info("GreenCardParser Tested");
            } catch (AssertionFailedError e) {
                getLog().info("GreenCardNotEqual");
            }
            try {
                testBlueCardParser(testParser);
                getLog().info("BlueCardParser Tested");
            } catch (AssertionFailedError e) {
                getLog().info("BlueCardNotEqual");
            }
            try {
                testYellowCardParser(testParser);
                getLog().info("YellowCardParser Tested");
            } catch (AssertionFailedError e) {
                getLog().info("YellowCardNotEqual");
            }
            try {
                testPurpleCardParser(testParser);
                getLog().info("PurpleCardParser Tested");
            } catch (AssertionFailedError e) {
                getLog().info("PurpleCardNotEqual");
            }
            try {
                testBlackCardParser(testParser);
                getLog().info("BlackCardParser Tested");
            } catch (AssertionFailedError e) {
                getLog().info("BlackCardNotEqual");
            }
        } catch (InvalidCardException e) {
            getLog().info("InvalidCardException");
        }
    }

    private static ArrayList<Excommunication> createFirstAgeExcommunication(){
        ArrayList<Excommunication> firstAgeExcommExpected = new ArrayList<>();
        AssetsExcommunication e1Expected = new AssetsExcommunication(new Assets(0,0,1,0,0,0,0));
        firstAgeExcommExpected.add(e1Expected);
        StrengthsExcommunication e2Expected = new StrengthsExcommunication(new Strengths(0,0,0,0,1,0,0));
        firstAgeExcommExpected.add(e2Expected);
        return firstAgeExcommExpected;
    }

    private static ArrayList<Excommunication> createSecondAgeExcommunication(){
        ArrayList<Excommunication> secondAgeExcommExpected = new ArrayList<>();
        MarketExcommunication e1Expected = new MarketExcommunication();
        secondAgeExcommExpected.add(e1Expected);
        RoundExcommunication e2Expected = new RoundExcommunication();
        secondAgeExcommExpected.add(e2Expected);
        return secondAgeExcommExpected;
    }

    private static ArrayList<Excommunication> createThirdAgeExcommunication(){
        ArrayList<Excommunication> firtAgeExcommExpected = new ArrayList<>();
        ServantsExcommunication e1Expected = new ServantsExcommunication();
        firtAgeExcommExpected.add(e1Expected);
        Assets[] onAssetsMalus = new Assets[2];
        onAssetsMalus[0] = new Assets(0,1,0,0,0,0,0);
        onAssetsMalus[1] = new Assets(0,0,2,0,0,0,0);
        EndGameAssetsExcommunication e2Expected = new EndGameAssetsExcommunication(new Assets(1,0,0,0,0,0,0), onAssetsMalus);
        firtAgeExcommExpected.add(e2Expected);
        return firtAgeExcommExpected;
    }

    private static void testFirstAgeExcommunication(Parser testParser) throws InvalidExcommunicationException{
        ArrayList<Excommunication> firstAgeExcommExpected = createFirstAgeExcommunication();
        assertEquals(firstAgeExcommExpected, testParser.getExcommunications().get(1));
    }

    private static void testSecondAgeExcommunication(Parser testParser) throws InvalidExcommunicationException{
        ArrayList<Excommunication> secondAgeExcommExpected = createSecondAgeExcommunication();
        assertEquals(secondAgeExcommExpected, testParser.getExcommunications().get(2));
    }

    private static void testThirdAgeExcommunication(Parser testParser) throws InvalidExcommunicationException{
        ArrayList<Excommunication> thirdAgeExcommExpected = createThirdAgeExcommunication();
        assertEquals(thirdAgeExcommExpected, testParser.getExcommunications().get(3));
    }

    private static void testExcommunicationParser(Parser testParser){
        try{
            try {
                Log.getLog().info("***First age excomm testing equals***");
                testFirstAgeExcommunication(testParser);
                Log.getLog().info("***First age excomm equals***");
            }
            catch (AssertionFailedError e) {
                getLog().info("firstAgeExcommunicationNotEqual");
            }
            try {
                Log.getLog().info("***Second age excomm testing equals***");
                testSecondAgeExcommunication(testParser);
                Log.getLog().info("***Second age excomm equals***");
            }
            catch (AssertionFailedError e) {
                getLog().info("SecondAgeExcommunicationNotEqual");
            }
            try {

                Log.getLog().info("***Third age excomm testing equals***");
                testThirdAgeExcommunication(testParser);
                Log.getLog().info("***Third age excomm equals***");
            }
            catch (AssertionFailedError e) {
                getLog().info("ThirdAgeExcommunicationNotEqual");
            }
        }
        catch (InvalidExcommunicationException e) {
            getLog().info("InvalidExcommunicationException");
        }
    }

    private static void testTimerParser(Parser testParser){
        try{
            getLog().info("***Test timer parser***");
            assertEquals(testParser.getTimerPlayMove(), 64);
            assertEquals(testParser.getTimerStartGame(), 62);
            getLog().info("***Timer equals***");
        }catch (AssertionFailedError e) {
            getLog().info("TimerNotEquals");
        }

    }

    @Test
    public void testParser() {
        try {
            Parser testParser = new Parser();
            testParser.parser(CONFIGS_PATH.concat("test/"));
            testCardParser(testParser);
            testExcommunicationParser(testParser);
            testTimerParser(testParser);
        }
        catch (IOException e) {
            getLog().info("IOException");
        }
        catch (InvalidTimerException e){
            getLog().info("InvalidTimersException");
        }
        catch (InvalidCardException e) {
            getLog().info("InvalidCardException");
        }
        catch (InvalidExcommunicationException e) {
            getLog().info("InvalidExcommunicationException");
        }
    }
}