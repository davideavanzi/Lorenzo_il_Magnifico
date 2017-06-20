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
public class TestParser extends TestCase{

    private static GreenCard createGreenCardExpected(){
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

    private static BlueCard createBlueCardExpected(){
        String nameExpected = "testBlueCard";
        int ageExpected = 2;
        Assets assetsCostExpected = new Assets(2, 3, 0, 0, 0, 0, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Strengths actionEffectStrength = new Strengths(1, 0, 0, 0, 0, 0, 0);
        Assets actionEffectAsset = new Assets();
        ActionEffect actionEffectExpected = new ActionEffect(actionEffectStrength , actionEffectAsset);
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

    private static void testGreenCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError{
        GreenCard greenCardExpected = createGreenCardExpected();
        assertEquals(greenCardExpected, testParser.getCard(1).get(GREEN_COLOR).get(0));
    }

    private static void testBlueCardParser(Parser testParser)
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError {
        BlueCard blueCardExpected = createBlueCardExpected();
        assertEquals(blueCardExpected, testParser.getCard(2).get(BLUE_COLOR).get(0));
    }

    public static void main(String args[]){
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
        }
        catch (IOException e){
            e.printStackTrace();
            getLog().info("IOException");
        }
        catch (InvalidCardException e){
            e.printStackTrace();
            getLog().info("InvalidCardException");
        }
        catch (InvalidExcommunicationException e){
            e.printStackTrace();
            getLog().info("InvalidExcommunicationException");
        }
    }
}
