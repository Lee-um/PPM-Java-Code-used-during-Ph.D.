import java.util.ArrayList;

/**
 * The object used to represent a context in the PPM model. Restricted to binary strings only.
 * 
 * @author : Liam J
 */

public class BinaryContext implements Comparable<BinaryContext>
{
    String id;  
    int escCnt = 0; //Value of the Escape Character
    int total = 0; //Sum of the Escape Character & All Predictions

    int oneCount = 0; //Number of times 1 occurs in the context.
    int zeroCount = 0; //Number of times 0 occurs in the context.

    /**
     * Constructor for objects of class Context.
     * 
     * id is the context string
     * preds is a list of the predictions of a context.
     * escCnt is the value of the escape character. It equals the number of different characters predicted by the Context. - So 1 or 2
     * oneCount is the number of times 1 occurs in the context.
     * zeroCount is the number of times 0 occurs in the context.
     * total is the sum of the counts of all predictions and the escape value
     * 
     * @param x: The string which identifies the Context.
     */
    public BinaryContext(String x)
    {
        this.id = x;
    }

    /**
     * Compares comtexts via their id . a.compareTo(b) will return a negative value if a.id
     * is before b.id in lexicographic order. Returns a positive number if after. Returns 0 if equal.
     */
    @Override
    public int compareTo(BinaryContext other) {
        int lengthA = this.id.length();
        int lengthB = other.getCtxt().length();
        int ans = 0;

        if(lengthA < lengthB){
            return -1;
        }
        else if(lengthA > lengthB){
            return 1;
        }

        return this.id.compareTo(other.getCtxt());
    }

    /**
     * Returns the id of a Context
     */
    public String getCtxt(){
        return id;
    }

    /**
     * Checks if a character has occurred previously in a given Context.
     * @param a: The character being checked
     * @return: A boolean on if it occurs or not.
     */
    public boolean isPredicted(char a){

        if(a == '0'){
            return (zeroCount > 0);
        }
        else{
            return (oneCount > 0);
        }
        
    }

    /**
     * Adds a character for the prediction of a Context. If the character has already been seen, its count is updated by 1.
     * If it hasn't, a prediction for it is added. The value of Escape Count & Total are updated as required.
     * @param a: The character being added to the predicion
     */
    public void addPred(char a){

        /*
         * If 'a' has been seen before in the context, update its count. Else add it to the prediction.
         */
        if(isPredicted(a))
        {
            total++;
            if(a == '0'){
                zeroCount++;
            }
            else{
                oneCount++;
            }
        }
        
        /*
         * If 'a' has not occurred, it is added as a context. Its count and escCnt are updated as required.
         */
        else
        {
            total += 2;
            escCnt += 1;
            if(a == '0'){
                zeroCount++;
            }
            else{
                oneCount++;
            }
        }
    }

    /**
     * Get the count of a character in a given Context.
     * @param a: The character whose count is gotten.
     * @return: The Count of the character 'a'.
     */
    public int ctOfPred(char a)
    {
        if (!isPredicted(a)){ 
            return 0; //'a' has not been seen before so return 0
        }

        else{
            if(a == '0'){
                return zeroCount;
            }
            else{
                return oneCount;
            }
        }
    }

    /**
     * Returns the total count of all characters & escape character for the Context.
     */
    public int getTotal(){
        return total;
    }

    /**
     * Returns the value of the Escape Character in a context.
     */
    public int getEscCnt(){
        return escCnt;
    }


    /**
     * Checks to see if a Context is deterministic. i.e. Either oneCount or zeroCount are equal to 0. Returns true or false.
     */
    public boolean isDeterministic(){
        return (escCnt == 1);
    }   
}