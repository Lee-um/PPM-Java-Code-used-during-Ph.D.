import java.util.ArrayList;

/**
 * The Fraction List is used to store all the prediction probabilities of PPM.
 *
 * Liam J. 
 * - Used in PPM studies.
 */

public class FractionList {
    
    // Used to store all the prediction probabilites of PPM.
    public ArrayList<Fraction> list;
    
    /**
     * FractionList constructor - An empty ArrayList object.
     */
    public FractionList() {
        list = new ArrayList<>();
    }

    /**
     * Adds the rational number n/d to the Fraction List
     * 
     * @param String n : The numerator of the Fraction being added to the list.
     * @param String d : The denominator the Fraction being added to the list.
     */
    public void add(String n, String d) {
        list.add(new Fraction(n, d));
    }

    /**
     * Adding a Fraction to the list.
     * 
     * @param Fraction a : The Fraction to be added.
     */
    public void add(Fraction a) {
        list.add(a);
    }
    
    /**
     * Retrieves the Fraction at index n of the FractionList list
     * 
     * @param n : the index to retrieve
     * @return: the Fraction at index n.
     */
    public Fraction get(int n) {
        return list.get(n);
    }
    
    /**
     * @return : Returns the number of Fractions in the FractionList list
     */
    public int size(){
        return list.size();
    }
    
    /**
     * Multiplies all the Fractions in the FractionList list together. EG: [2/3,1/3] -> 2/9
     * 
     * @return x: The Fraction x which is all the result of multiplying all Fractions in list together.
     */
    public Fraction combine() {
        Fraction x = new Fraction("1", "1"); //Start off with the base Fraction of 1.
        for (Fraction a : list) {
            x = x.multiply(a);
        }
        return x;
    }
    
    /**
     * Takes a FractionList and appends all Fractions in it to the FractionList list.
     * 
     * @input a: The FractionList a to add to list.
     */
    public void addAll(FractionList a){
        for(int i = 0; i < a.size(); i++){
            list.add(a.get(i));
        }
    }

   /**
    * Sums up all Fractions in list. EG [1/3,2/5] -> 11/15.
    * 
    * @return x: The Fraction x which is the result of adding all Fractions in list together.
    */
   public Fraction sum(){
        Fraction x = new Fraction("0","1"); //Start off with the base Fraction of 0.
        for(Fraction a : list){
            x = x.add(a);
        }
        return x;
    }
    
    /**
     * Combines every Fraction in the FractionList list into a String, with the '*' symbol separating each Fraction in list.
     * Makes the list readable.
     * 
     * EG [2/3, 1/6] -> "2/3*1/6*"
     * 
     * return: The string version of all the Fractions in list.
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        for(Fraction f : list){
            b.append(f.toString() + "*");
        }
        return b.toString();
    }
}
