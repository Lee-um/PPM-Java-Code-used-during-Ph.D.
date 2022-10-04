
/**
 * An object to easily contain all the information about the encoding at the end.
 */

class Container{

    String x;
    int compL, numCtxt, longestCtxt, width, numDeterministic;
    Fraction metric;

    /**
     * The construcor of Container.
     * 
     * String x : The string encoded by PPM
     * int CompL : The length of x encoded
     * int numCtxt : The number of contexts in the model once x is encoded
     * int longestCtxt : The length of the longest context in the model once x is encoded
     * Fraction metric : Some metric on the model.
     * int numDeterministic : The number of deterministic contexts in the model.
     * int width : The width of the model.
     */
    
    public Container(String x, int compL, int numCtxt, int longestCtxt, Fraction metric, int numDeterministic, int width){
        this.x = x;
        this.compL = compL;
        this.numCtxt = numCtxt;
        this.longestCtxt = longestCtxt;
        this.metric = metric;
        this.numDeterministic = numDeterministic;
        this.width = width;

    }

    public String toString()
    {
        String s = "";
        double num = (double) this.numDeterministic;
        double denom = (double) this.numCtxt;
        double ratio = (num/denom)*100.0;

        double bot = (double) this.x.length();
        double top = (double) this.compL;
        double cRatio = (top/bot)*100.0;

        s += this.x + "\nuncompressed " + this.x.length() + "\ncompression ratio " + cRatio + "%\ncompressed length " + this.compL + "\ncontexts " + this.numCtxt + "\nNumber Deterministic " + this.numDeterministic + "\nratio " + ratio +"%\ndepth " + this.longestCtxt + "\nwidth " + this.width + "\nmetric " + this.metric + "\n= " + fracConverter(this.metric);
        return s;
    }
    
    public static double fracConverter(Fraction x){
        return (x.numerator.doubleValue())/(x.denominator.doubleValue());
    }
}