package it.polimi.ingsw.lim;

/**
 * Here are stored useful methods.
 * Created by ava on 15/06/17.
 */
public class Utils {
    /**
     * Method to get the max and the min numbers from a list, using varargs.
     * from https://stackoverflow.com/questions/15717695/more-convenient-way-to-find-the-max-of-2-numbers
     * @param vals
     * @return
     */
    public static Integer max(Integer... vals) {
        Integer ret = null;
        for (Integer val : vals) {
            if (ret == null || (val != null && val > ret)) {
                ret = val;
            }
        }
        return ret;
    }

    public static Integer min(Integer... vals) {
        Integer ret = null;
        for (Integer val : vals) {
            if (ret == null || (val != null && val > ret)) {
                ret = val;
            }
        }
        return ret;
    }

}
