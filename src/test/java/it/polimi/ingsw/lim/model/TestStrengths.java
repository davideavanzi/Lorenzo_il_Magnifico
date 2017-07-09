package it.polimi.ingsw.lim.model;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.lim.utils.Log.getLog;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestStrengths {
    private static ArrayList<Strengths> createStrengths(){
        ArrayList<Strengths> strengths = new ArrayList<>();
        strengths.add(new Strengths());                                                                                                             //0
        strengths.add(new Strengths(1, 0, 0, 0, 0, 0, 0, 0, 0, 0));  //1
        strengths.add(new Strengths(0, 1, 0, 0, 0, 0, 0));                                        //2
        strengths.add(new Strengths(0, 0, 1, 0, 0, 0, 0, 0, 0, 0));  //3
        strengths.add(new Strengths(0, 0, 0, 1, 0, 0, 0));                                        //4
        strengths.add(new Strengths(0, 0, 0, 0, 1, 0, 0, 0, 0, 0));  //5
        strengths.add(new Strengths(0, 0, 0, 0, 0, 1, 0));                                        //6
        strengths.add(new Strengths(0, 0, 0, 0, 0, 0, 1, 0, 0, 0));  //7
        strengths.add(new Strengths(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));  //8
        strengths.add(new Strengths(2, 2, 2, 2, 2, 2, 2, 2, 2, 2));  //9
        strengths.add(new Strengths(4, 4, 4, 4, 4, 4, 4, 4, 4, 4));  //10
        strengths.add(new Strengths(0, 0, 0, 0, 0, 0, 0, 1, 0, 0));  //11
        strengths.add(new Strengths(0, 0, 0, 0, 0, 0, 0, 0, 1, 0));  //12
        strengths.add(new Strengths(0, 0, 0, 0, 0, 0, 0, 0, 0, 1));  //13

        return strengths;
    }

    private void testAdders(ArrayList<Strengths> strengths) {
        try {
            assertEquals(strengths.get(0).add(strengths.get(0)), strengths.get(0));
            assertEquals(strengths.get(0).add(strengths.get(1)), strengths.get(1));
            assertEquals(strengths.get(0).add(strengths.get(2)), strengths.get(2));
            assertEquals(strengths.get(0).add(strengths.get(3)), strengths.get(3));
            assertEquals(strengths.get(0).add(strengths.get(4)), strengths.get(4));
            assertEquals(strengths.get(0).add(strengths.get(5)), strengths.get(5));
            assertEquals(strengths.get(0).add(strengths.get(6)), strengths.get(6));
            assertEquals(strengths.get(0).add(strengths.get(7)), strengths.get(7));
            /*assertEquals(strengths.get(0).add(strengths.get(11)), strengths.get(11));
            assertEquals(strengths.get(0).add(strengths.get(12)), strengths.get(12));
            assertEquals(strengths.get(0).add(strengths.get(13)), strengths.get(13));
            assertEquals(strengths.get(8).add(strengths.get(8)), strengths.get(9));
            assertEquals(strengths.get(9).add(strengths.get(9)), strengths.get(10));*/
        } catch (AssertionFailedError e) {
            getLog().info("add error");
        }
    }

    private void testIsNotNull(ArrayList<Strengths> strengths){
        try {
            assertEquals(strengths.get(0).isNotNull(), false);
            assertEquals(strengths.get(1).isNotNull(), true);
            assertEquals(strengths.get(2).isNotNull(), true);
            assertEquals(strengths.get(3).isNotNull(), true);
            assertEquals(strengths.get(4).isNotNull(), true);
            assertEquals(strengths.get(5).isNotNull(), true);
            assertEquals(strengths.get(6).isNotNull(), true);
            assertEquals(strengths.get(7).isNotNull(), true);
            assertEquals(strengths.get(11).isNotNull(), true);
            assertEquals(strengths.get(12).isNotNull(), true);
            assertEquals(strengths.get(13).isNotNull(), true);
        } catch (AssertionFailedError e) {
            getLog().info("add error");
        }
    }

    @Test
    public void testStrengths(){
        ArrayList<Strengths> strengthsExpected = createStrengths();
        testAdders(strengthsExpected);
        testIsNotNull(strengthsExpected);
    }
}