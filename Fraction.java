
import java.math.BigInteger;


/**
 * The Fraction object. Fractions are used for the for the predicton probabilities of PPM.
 *
 * Liam J. 
 * - Used in PPM studies.
 */

public class Fraction {
    BigInteger numerator, denominator;

    /**
     * Fraction Object - a rational number. Constructor if the numerator and denominator are given as Strings.
     * 
     * @param String n: The numberator of the Fraction.
     * @param String d : The denominator of the Fraction.
     */
    public Fraction(String n, String d) {
        this.numerator = new BigInteger(n); //Converts String n into a BigInteger
        this.denominator = new BigInteger(d); //Converts String d into a BigInteger
        simplify();
    }

    /**
     * Fraction Object - a rational number. Constructor if the numerator and denominator are BigIntegers.
     * 
     * @param String n: The numberator of the Fraction.
     * @param String d : The denominator of the Fraction.
     */
    public Fraction(BigInteger n, BigInteger d) {
        this.numerator = n;
        this.denominator = d;
        simplify();
    }

    /**
     * Multiplies a Fraction by another Fraction - Multiplication of rational numbers.
     * 
     * @param Fraction other: The Fraction that is multiplied by. EG 1/3 X 2/3 -> 2/9
     */
    public Fraction multiply(Fraction other) {
        BigInteger numerator = this.numerator.multiply(other.numerator);
        BigInteger denominator = this.denominator.multiply(other.denominator);
        BigInteger biggest_divisor = numerator.gcd(denominator);

        numerator = numerator.divide(biggest_divisor);
        denominator = denominator.divide(biggest_divisor);
        return new Fraction(numerator, denominator);
    }

    /**
     * Divides a Fraction by another Fraction - Division of rational numbers.
     * 
     * @param Fraction other: The Fraction that is divided by. EG 1/6 DIV 2/1 -> 1/12
     */
    public Fraction divide(Fraction other) {
        BigInteger numerator = this.numerator.multiply(other.denominator);
        BigInteger denominator = this.denominator.multiply(other.numerator);
        BigInteger biggest_divisor = numerator.gcd(denominator);

        numerator = numerator.divide(biggest_divisor);
        denominator = denominator.divide(biggest_divisor);
        return new Fraction(numerator, denominator);
    }

    /**
     * Adds a Fraction to another Fraction - Addition of rational numbers.
     * 
     * @param Fraction other: The Fraction that is added. EG 1/6 + 1/6 -> 1/3
     */
    public Fraction add(Fraction other){
        Fraction answer;

        /*
         * Checking to see if the denominator of the two Fractions are the same. Then the result is just adding the numerators.
         */
        if(other.denominator.compareTo(this.denominator) == 0){
            answer = new Fraction(this.numerator.add(other.numerator), this.denominator);
        }
        /*
         * If the denominators are different.
         */
        else{
            BigInteger den = this.denominator.multiply(other.denominator);
            BigInteger num = this.numerator.multiply(other.denominator);
            num = num.add(this.denominator.multiply(other.numerator));
            answer = new Fraction(num,den);
        }
        BigInteger biggest_divisor = answer.numerator.gcd(answer.denominator);

        numerator = answer.numerator.divide(biggest_divisor);
        denominator = answer.denominator.divide(biggest_divisor);
        return new Fraction(numerator,denominator);
    }
    
    /**
     * Subtracts a Fraction from another Fraction - Subtraction of rational numbers.
     * 
     * @param Fraction other: The Fraction that is added. EG 1/3 - 1/6 -> 1/6
     */
    public Fraction minus(Fraction other){
        Fraction answer;
        
        /*
         * Checking to see if the denominator of the two Fractions are the same. Then the result is just adding the numerators.
         */
        if(other.denominator.compareTo(this.denominator) == 0){
            answer = new Fraction(this.numerator.subtract(other.numerator), this.denominator);
        }
        /*
         * If the denominators are different.
         */
        else{
            BigInteger den = this.denominator.multiply(other.denominator);
            BigInteger num = this.numerator.multiply(other.denominator);
            num = num.subtract(this.denominator.multiply(other.numerator));
            answer = new Fraction(num,den);
        }
        BigInteger biggest_divisor = answer.numerator.gcd(answer.denominator);

        numerator = answer.numerator.divide(biggest_divisor);
        denominator = answer.denominator.divide(biggest_divisor);
        return new Fraction(numerator,denominator);

    }

    /**
     * Simplifies a Fraction. 
     * EG 2/12 -> 1/6
     */
    public void simplify(){
        BigInteger gcd = this.numerator.gcd(this.denominator);
        this.numerator = this.numerator.divide(gcd);
        this.denominator = this.denominator.divide(gcd);
    }

    public int approxCompressionLength() {
        return this.denominator.bitLength() - this.numerator.bitLength() + 1;
    }

    /**
     * Returns the String version of a Fraction.
     * 
     * @return : The String version of the Fraction. EG The Fraction with n = 2, d = 7 -> "2/7"
     */
    public String toString() {
        return "" + this.numerator + "/" + this.denominator;
    }
}


