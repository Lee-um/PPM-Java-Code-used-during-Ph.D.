/**
 * Contains methods to construct some examples of DeBruin strings. 
 * 
 * A binary DeBruijn string s of order n is a cyclic sequence in which every 
 * possible length-n binary string occurs as a substring of s exactly once.
 * 
 * EG 00011101 is a binary DeBruijn string of order 3.
 */

public class DeBruijn{
    /**
     *  Algorithm: start with n 0's. Append a 1 if the n-tuple that
     *  would be formed has not already appeared in the sequence;
     *  append a 0 otherwise.
     *  
     *  @param int n : The order of the deBruijn string to be made.
     *  @return : A deBruijn string of order n
     *  
     *  EG 3 -> "00011101"
     */
    public static String makeDB(int n) {

        String deBruijn = "";
        for (int i = 0; i < n; i++)
            deBruijn = deBruijn + "0";

        for (int i = n; i < (1 << n); i++) {
            String suffix = deBruijn.substring(i - n + 1);
            if (deBruijn.indexOf(suffix + "1") == -1)
                deBruijn = deBruijn + "1";
            else
                deBruijn = deBruijn + "0";
        }
        return deBruijn;

    }

    
    /**
     * Returns a string which is all binary strings of length n concatenated together.
     * 
     * @param int n: the length of the binary strings you want to be concatenated together.
     * @return : the result of concatenating the strings.
     * 
     * EG : 2 -> "00011011"
     * EG : 3 -> "000001010011100101110111"
     */
    public static String basePartMaker(int n){
        String sol = new String();

        for(int j = 0; j < Math.pow(2,n); j++){
            String add = Integer.toString(j, 2);
            //System.out.println(i + ":" + j);

            while(add.length() < n){
                add = 0 + add;
            }
            //System.out.println(add);
            sol = sol + add;
        }

        return sol;
    }

    /**
     * Returns a string which is all binary strings concatenated together, followed by all binary strings of length two,
     * ....., up to all binary strings of length n concatenated together. i.e. It is the result of concatenating
     * basePartmaker(1) with basePartMaker(2) with ..... with basePartmaker(n)
     * 
     * @param int n: the length of the binary strings you want to be concatenated together.
     * @return : the result of concatenating the strings.
     * 
     * EG : 2 -> "0100011011"
     * EG : 3 -> "010100011011000001010011100101110111"
     */
    public static String baseMaker(int n){
        String sol = new String();

        for(int i = 1; i <= n; i++){
            for(int j = 0; j < Math.pow(2,i); j++){
                String add = Integer.toString(j, 2);
                //System.out.println(i + ":" + j);

                while(add.length() < i){
                    add = 0 + add;
                }
                //System.out.println(add);
                sol = sol + add;
            }
        }
        return sol;
    }

    /**
     * Creates an 'Odd Zone' of the compressible PPM sequence. 
     * An 'Odd Zone' for integer 'n' is formed by concatenating a deBruijn
     * string of order n with itself  times.
     * 
     * @param int n : The zone to be contructed. i.e. Zone n.
     * @ return : The 'Odd Zone' string. EG if s is a deBruijn string of
     * order n, then s^n is returned.
     * 
     * EG 3 -> "0001110100001110100011101" = "00011101^3"
     *    i.e.  00011101
     *          00011101
     *          00011101
     */

    public static String makeDBOdd(int n){
        String dB = makeDB(n);
        String orig = dB;

        /*
         *Concatenate deBruijns strings together until the resultant string has length 2^n*n.
         */

        while(dB.length() < (n*Math.pow(2,n))){
            //System.out.println(orig);
            dB += orig;
        }
        //System.out.println(orig);
        //System.out.println(dB);
        return dB;
    }

    /**
     * Creates an 'Even Zone' of the compressible PPM sequence. 
     * An 'Even Zone' for integer 'n' is formed by concatenating a deBruijn
     * string of order n with itself n times. However, if at any stage 
     * the string while being constructed is divisible by n, the deBruijn string used 
     * to construct the larger string is shifted by one bit to the left cyclically. 
     * 
     * @param int n : The zone to be contructed. i.e. Zone n.
     * @return : The 'EVEN Zone' string.
     * 
     * EG 4 -> "0000111101100101000111101100101000111101100101000111101100101000"
     *    i.e.  0000111101100101
     *          0001111011001010
     *          0011110110010100
     *          0111101100101000
     *   
     *    6 -> "(0000001111110111100111010111000110110100110010110000101010001001)^3(0000011111101111001110101110001101101001100101100001010100010010)^3"
     *    i.e. 0000001111110111100111010111000110110100110010110000101010001001
     *         0000001111110111100111010111000110110100110010110000101010001001
     *         0000001111110111100111010111000110110100110010110000101010001001
     *         0000011111101111001110101110001101101001100101100001010100010010
     *         0000011111101111001110101110001101101001100101100001010100010010
     *         0000011111101111001110101110001101101001100101100001010100010010
     */

    public static String makeDBEven(int n){

        String dB = DeBruijn.makeDB(n);
        /*
         * The original deBruijn string which is used to construct the zone.
         */
        String orig = dB; 

        /*
         * Concatenate deBruijns strings together until the resultant string has length 2^n*n.
         */
        while(dB.length() < (n*Math.pow(2,n))){
            /*
             * If the string built so far has a length divisible by n, orig is shifted one bit cyclically to the left by 1.
             */
            if(dB.length() % n == 0){
                //System.out.println(orig);
                orig = orig.substring(1) + orig.substring(0,1);

                dB = dB + orig;      
                //System.out.println("Switch");
            }
            else{
                dB += orig;
                //System.out.println(orig);
            }
            //  System.out.println(dB);
        }
        //System.out.println(orig);
        //System.out.println(dB);
        return dB;

    }

    /**
     * Creates a deBruijn string of order n.
     * 
     * @param int n : the order of the deBruijn string being constructed
     * @return : a deBruijn string of orde n
     */
    public static String makeDBPart(int n){
        String dB = DeBruijn.makeDB(n);
        return dB;
    }

    /**
     * Creates a string which is the result of concatenating Zone 1, Zone 2, .... , Zone n
     * 
     * @int n : the number of zone to concatenate together.
     * @return : a string which is Zone 1, Zone 2, .... , Zone n all concatenated together.
     * 
     * EG 3 -> "0100110110000111010001110100011101"
     * 
     *    i.e   01
     *          0011
     *          0110
     *          00011101
     *          00011101
     *          00011101
     */
    public static String fullDB(int n){
        String sol = new String();

        for(int i = 1; i <= n; i++){

            //Checking whether to use the odd method or even method of construction.
            if(i%2 == 0){
                sol = sol + makeDBEven(i);
            }
            else{
                sol = sol + makeDBOdd(i);
            }
        }
        //System.out.println(sol);
        return sol;
    }


}

