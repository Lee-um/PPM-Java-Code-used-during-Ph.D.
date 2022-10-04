import java.util.ArrayList;


/**
 * The BinaryContainer2 object. It is primarily used as a storage device.
 *
 * Liam J. 
 * - Used in PPM studies.
 */



class BinaryContainer2{
    String str;
    ArrayList<BinaryContext> model;
    FractionList fracs;

    /**
     * The BinaryContainer2 object. It is used as a storage device the quicken the runtime. 
     * 
     * @param String str : A String str.
     * @param ArrayList<BinaryContext> model : A list of BinaryContexts which is the model of PPM.
     * @param Fraction List fracs : A list o Fractions.
     */
    public BinaryContainer2(String str, ArrayList<BinaryContext> model, FractionList fracs){
        this.str = str;
        this.model = model;
        this.fracs = fracs;

    }

}