package it.polimi.ingsw.lim.parser;

import it.polimi.ingsw.lim.exceptions.InvalidCardException;
import it.polimi.ingsw.lim.exceptions.InvalidExcommunicationException;
import it.polimi.ingsw.lim.model.*;
import junit.framework.*;

import java.io.IOException;
import java.util.ArrayList;

import static it.polimi.ingsw.lim.Settings.CONFIGS_PATH;
import static it.polimi.ingsw.lim.Settings.GREEN_COLOR;
import static it.polimi.ingsw.lim.Log.getLog;
/**
 * Created by FabCars. The task of this test class is to test if card parser work as expected
 */
public class TestParser extends TestCase{

    private static GreenCard createGreenCardExpected(){
        Assets assetsCostExpected = new Assets(1, 0, 0, 0, 0, 0, 0);
        ArrayList<ImmediateEffect> iEffectExpected = new ArrayList<>();
        Assets assetsBonusExpected = new Assets(0, 1, 0, 0, 0, 0, 0);
        AssetsEffect assetsEffectExpected = new AssetsEffect(assetsBonusExpected);
        iEffectExpected.add(assetsEffectExpected);
        Assets harvestResultExpected = new Assets(0, 0, 1, 0, 0, 0, 0);
        Strengths actionStrengthExpected = new Strengths(1, 0, 0, 0, 0, 0, 0);
        return new GreenCard(
                "testGreenCard",
                1,
                assetsCostExpected,
                iEffectExpected,
                harvestResultExpected,
                actionStrengthExpected
        );
    }

    public static void testGreenCardParser()
            throws IOException, InvalidExcommunicationException, InvalidCardException, AssertionFailedError{
        Parser testParser = new Parser();
        testParser.parser(CONFIGS_PATH.concat("test/"));
        GreenCard greenCardExpected = createGreenCardExpected();
        assertEquals(greenCardExpected, testParser.getCard(1).get(GREEN_COLOR).get(0));
    }

    public static void main(String args[]){
        try{
            testGreenCardParser();
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
        catch (AssertionFailedError e){
            e.printStackTrace();
            getLog().info("CardNotEqual");
        }
    }
}
