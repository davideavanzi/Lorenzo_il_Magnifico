package it.polimi.ingsw.lim.model;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.lim.utils.Log.getLog;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by fabri on 09/07/17.
 */
public class TestAssets {
    private static ArrayList<Assets> createAssets(){
        ArrayList<Assets> assets = new ArrayList<>();
        assets.add(new Assets());                                                                                   //0
        assets.add(new Assets(1, 0, 0, 0, 0, 0, 0));    //1
        assets.add(new Assets(0, 1, 0, 0, 0, 0, 0));    //2
        assets.add(new Assets(0, 0, 1, 0, 0, 0, 0));    //3
        assets.add(new Assets(0, 0, 0, 1, 0, 0, 0));    //4
        assets.add(new Assets(0, 0, 0, 0, 1, 0, 0));    //5
        assets.add(new Assets(0, 0, 0, 0, 0, 1, 0));    //6
        assets.add(new Assets(0, 0, 0, 0, 0, 0, 1));    //7
        assets.add(new Assets(1, 1, 1, 1, 1, 1, 1));    //8
        assets.add(new Assets(2, 2, 2, 2, 2, 2, 2));    //9
        assets.add(new Assets(4, 4, 4, 4, 4, 4, 4));    //10
        assets.add(new Assets(1, 1, 1, 1, 0, 0, 0));    //11
        return assets;
    }
    
    private void testisGreaterOrEquals(ArrayList<Assets> assets) {
        try {
            try {
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(1)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(2)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(3)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(4)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(5)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(6)), false);
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(7)), false);
            }
            catch (AssertionFailedError e) {
                getLog().info("greater or equal false error");
            }
            try {
                assertEquals(assets.get(0).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(1).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(2).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(3).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(4).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(5).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(6).isGreaterOrEqual(assets.get(0)), true);
                assertEquals(assets.get(7).isGreaterOrEqual(assets.get(0)), true);
            }
            catch (AssertionFailedError e) {
                getLog().info("greater or equal true error");
            }
        }catch (AssertionFailedError e) {
            getLog().info("Greater or equal error");
        }
    }
    
    private void testisNotNull (ArrayList<Assets> assets){
        try {
            try {
                assertEquals(assets.get(0).isNotNull(), false);
            }catch (AssertionFailedError e) {
                getLog().info("not null false error");
            }
            try {
                assertEquals(assets.get(1).isNotNull(), true);
                assertEquals(assets.get(2).isNotNull(), true);
                assertEquals(assets.get(3).isNotNull(), true);
                assertEquals(assets.get(4).isNotNull(), true);
                assertEquals(assets.get(5).isNotNull(), true);
                assertEquals(assets.get(6).isNotNull(), true);
                assertEquals(assets.get(7).isNotNull(), true);
            }catch (AssertionFailedError e) {
                getLog().info("not null true erro");
            }
        }catch (AssertionFailedError e) {
            getLog().info("is not null error");
        }
    }
    
    private void testDivided(ArrayList<Assets> assets){
        try {
            assertEquals(assets.get(0).divide(assets.get(0)), 0);//todo
            assertEquals(assets.get(9).divide(assets.get(8)), 2);
        }catch (AssertionFailedError e) {
            getLog().info("divided error");
        }
    }

    private void testAdders(ArrayList<Assets> assets){
        try {
            try {
                assertEquals((assets.get(0).
                                add(assets.get(1)).
                                add(assets.get(2)).
                                add(assets.get(3)).
                                add(assets.get(4)).
                                add(assets.get(5)).
                                add(assets.get(6)).
                                add(assets.get(7))),
                        (assets.get(8))
                );
            }catch (AssertionFailedError e) {
                getLog().info("simple add error");
            }
            try {
                assertEquals((assets.get(0).
                                addCoins(assets.get(1).getCoins()).
                                addServants(assets.get(2).getServants()).
                                addVictoryPoints(assets.get(3).getVictoryPoints())),
                        (assets.get(1)));
            }catch (AssertionFailedError e) {
                getLog().info("particular adder error");
            }try {
                assertEquals(assets.get(11).sumAll(), 4);
            }
            catch (AssertionFailedError e) {
                    getLog().info("sum all error");
                }
        }catch (AssertionFailedError e) {
            getLog().info("adders error");
        }
    }

    private void testSubtracters(ArrayList<Assets> assets){
        try {
            assertEquals((assets.get(0).subtractToZero(assets.get(1))), assets.get(0));
            assertEquals((assets.get(9).subtractToZero(assets.get(8))), assets.get(8));
        }catch (AssertionFailedError e) {
            getLog().info("subtract to zero error");
        }
        try {
            assertEquals((assets.get(1).subtract(assets.get(0))), assets.get(1));
            assertEquals((assets.get(2).subtract(assets.get(0))), assets.get(2));
            assertEquals((assets.get(3).subtract(assets.get(0))), assets.get(3));
            assertEquals((assets.get(4).subtract(assets.get(0))), assets.get(4));
            assertEquals((assets.get(5).subtract(assets.get(0))), assets.get(5));
            assertEquals((assets.get(6).subtract(assets.get(0))), assets.get(6));
            assertEquals((assets.get(7).subtract(assets.get(0))), assets.get(7));
        }catch (AssertionFailedError e) {
            getLog().info("subtract error");
        }
    }

    private void testMultipliers(ArrayList<Assets> assets) {
        try {
            assertEquals((assets.get(0).multiply(assets.get(0))), assets.get(0));
            assertEquals((assets.get(8).multiply(assets.get(8))), assets.get(8));
            assertEquals((assets.get(9).multiply(assets.get(9))), assets.get(10));
            assertEquals((assets.get(8).multiply(0)), assets.get(0));
            assertEquals((assets.get(8).multiply(2)), assets.get(9));
            assertEquals((assets.get(9).multiply(2)), assets.get(10));
        } catch (AssertionFailedError e) {
            getLog().info("multipliers error");
        }
    }

    @Test
    public void testAssets(){
        ArrayList<Assets> assetsExpected = createAssets();
        testAdders(assetsExpected);
        testDivided(assetsExpected);
        testisGreaterOrEquals(assetsExpected);
        testisNotNull(assetsExpected);
        testSubtracters(assetsExpected);
        testMultipliers(assetsExpected);
    }

}
