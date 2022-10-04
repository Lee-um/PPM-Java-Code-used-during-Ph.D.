import java.util.ArrayList;
import java.util.HashMap;

/**
 * A HashModelSet object which is used to speed up computations.
 */

public class HashModelSet{
    HashMap<String,Integer> set;
    ArrayList<BinaryContext> model;

    /**
     * The constructor of HashModelBool object. It is a pair of a HashMap which maps Strings to Integers, and an ArrayList of BinaryContexts.
     */
    
    public HashModelSet(HashMap<String,Integer> set, ArrayList<BinaryContext> model){
        this.set = set;
        this.model = model;
    }
}
