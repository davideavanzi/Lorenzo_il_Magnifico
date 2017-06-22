package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.exceptions.InvalidCardException;
import it.polimi.ingsw.lim.exceptions.InvalidExcommunicationException;
import it.polimi.ingsw.lim.model.*;
import junit.framework.*;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.Settings.*;
import static it.polimi.ingsw.lim.Log.getLog;
/**
 * Created by FabCars. The task of this test class is to test if card parser work as expected
 */
public class TestParser extends TestCase {

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
                actionStrengthExpected
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
        ArrayList<Assets> productionResults = new ArrayList<>();
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
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        GreenCard greenCardExpected = createGreenCardExpected();
        assertEquals(greenCardExpected, testParser.getCard(1).get(GREEN_COLOR).get(0));
    }

    private static void testBlueCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        BlueCard blueCardExpected = createBlueCardExpected();
        assertEquals(blueCardExpected, testParser.getCard(2).get(BLUE_COLOR).get(0));
    }

    private static void testYellowCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        YellowCard yellowCardExpected = createYellowCardExpected();
        assertEquals(yellowCardExpected, testParser.getCard(3).get(YELLOW_COLOR).get(0));
    }

    private static void testPurpleCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        PurpleCard purpleCardExpected = createPurpleCardExpected();
        assertEquals(purpleCardExpected, testParser.getCard(1).get(PURPLE_COLOR).get(0));
    }

    private static void testBlackCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        BlackCard blackCardExpected = createBlackCardExpected();
        assertEquals(blackCardExpected, testParser.getCard(1).get(BLACK_COLOR).get(0));
    }

    public static void testCardParser() {
        try {
            Parser testParser = new Parser();
            testParser.parser(CONFIGS_PATH.concat("test/"));
            try {
                testGreenCardParser(testParser);
                getLog().info("GreenCardParser Tested");
            } catch (AssertionFailedError e) {
                e.printStackTrace();
                getLog().info("GreenCardNotEqual");
            }
            try {
                testBlueCardParser(testParser);
                getLog().info("BlueCardParser Tested");
            } catch (AssertionFailedError e) {
                e.printStackTrace();
                getLog().info("BlueCardNotEqual");
            }
            try {
                testYellowCardParser(testParser);
                getLog().info("YellowCardParser Tested");
            } catch (AssertionFailedError e) {
                e.printStackTrace();
                getLog().info("YellowCardNotEqual");
            }
            try {
                testPurpleCardParser(testParser);
                getLog().info("PurpleCardParser Tested");
            } catch (AssertionFailedError e) {
                e.printStackTrace();
                getLog().info("PurpleCardNotEqual");
            }
            try {
                testBlackCardParser(testParser);
                getLog().info("BlackCardParser Tested");
            } catch (AssertionFailedError e) {
                e.printStackTrace();
                getLog().info("BlackCardNotEqual");
            }
        } catch (IOException e) {
            e.printStackTrace();
            getLog().info("IOException");
        } catch (InvalidCardException e) {
            e.printStackTrace();
            getLog().info("InvalidCardException");
        } catch (InvalidExcommunicationException e) {
            e.printStackTrace();
            getLog().info("InvalidExcommunicationException");
        }
    }


}