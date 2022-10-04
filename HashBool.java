

import java.util.HashMap;

/**
 * A HashMap object which is used to speed up computations.
 */

public class HashBool{
    HashMap<String,Integer> set;
    boolean bool;

    /**
     * The constructor of HashBool object. It is a pair of a HashMap which maps Strings to Integers, and a boolean.
     */
    public HashBool(HashMap<String,Integer> set, boolean bool){
        this.set = set;
        this.bool = bool;
    }
}
