package it.polimi.ingsw.lim.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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


    /**
     * This method orders an hashmap in descending order based on it's value
     * this code is obtained from
     * https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     * @param map the map given
     * @param <K> the key
     * @param <V> the value
     * @return an ordered map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
