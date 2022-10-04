import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A Java encoding of PPM Star algorithm. 
 * 
 * It is not a perfect recreation ~ Parts are altered as only using Binary strings and 
 * it is only tested on certain imputs specifially for the PPM paper.
 */
public class PPM
{
    

    /**
     * Encodes a string via the PPM* algorithm. It prints the steps involved at each stage.
     * ie. the current contexts, the active contexts, the counts, tha finding of the shortest deterministic context,
     * the outputs and probabilities, what contexts are added, what contexts are updated.
     * It finally prints out the Context Table for all contexts.
     * 
     * @param String x : The String x to encode.
     * @return : A BinaryContainer2 onject
     */
    public static BinaryContainer2 encode(String x){
        
        if(!Pattern.matches("^[01]+$",x)){
            throw new IllegalArgumentException("The string to encode must be non-empty and only contain 0s and 1s");
        }
        

        Date startDate = Calendar.getInstance().getTime();
        long d_StartTime = new Date().getTime();
        int size = 0;

        FractionList fracs = new FractionList();
        ArrayList<String> outputs = new ArrayList<String>();
        String outputsProb = new String();
        int orderMinusOneProb = 2;

        ArrayList<BinaryContext> model = new ArrayList<BinaryContext>(8406239);
        System.out.println(model.size());
        model.add(new BinaryContext(""));
        size++;

        ArrayList<BinaryContext> active = new ArrayList<BinaryContext>(43);
        ArrayList<Integer> activeIndex = new ArrayList<Integer>(43);

        boolean find = true;
        HashMap<String,Integer> set = new HashMap<String,Integer>();

        /*
         * Looping over the input encoded it character by character.
         */
        for(int i = 0; i < x.length(); i++)
        {
            System.out.println(i);
            if(find && model.size() > 510){
                if(model.get(510).id.equals("11111111")){
                    System.out.println("full " + i);
                    find = false;
                }
            }

            //next character of the input to be encoded character encoded
            char pred = x.charAt(i); 

            //The contexts used to predict pred of the input
            active = activeContexts(model, x.substring(0,i+1), active); 

            //Indexes of the active contexts in the model.
            activeIndex = activeContextsIndex(model, active, activeIndex);

            System.out.println("String: " + x.substring(0,i) + " _ " + pred);
            //System.out.println(i + " Contexts: " + modelIDs(model));
            //System.out.println("Index: "  + activeIndex);
            System.out.println("Active: " + modelIDs(active));

            /*
             * The first character is encoded by the order (-1) table.
             */
            if(i == 0)
            {
                System.out.println("[" + pred + ",-1] : 1/" + orderMinusOneProb);
                outputs.add("[" + pred + ",-1]");
                //outputsProb = outputsProb + "1/" + orderMinusOneProb +"*";
                fracs.add("1",""+ orderMinusOneProb);
            }

            /*
             * Otherwise.
             */
            else{

                //Find Shortest det context
                int shortestDet = shortestDet(active);

                System.out.println("Exists shortest det: "  + shortestDet);
                boolean foundShortest = false;

                /*
                 * If there does not exist a deterministic context, you use the longest context available.
                 * 
                 */
                if(shortestDet == -1){ //If there is no deterministic context

                    boolean seen = false; //A flag used to determine if the order (-1) table should make the prediction.

                    /*
                     * This loop may be redundant as since we use binary strings, if a context is not deterministic, the longest is always used.
                     * So no need to loop over them to find a context which predicts pred.
                     * But it wil only run once though.
                     */
                    for(int j = activeIndex.size() - 1; j >= 0; j--)
                    {
                        int num = activeIndex.get(j);

                        //The current context being used to predict.
                        BinaryContext current = model.get(num); 

                        //Whether the pred has been seen in the model.get(num) context before 
                        boolean predict = current.isPredicted(pred); 

                        /*
                         * If pred has appeared in the current context, output the probability and move on to next "stage"
                         */
                        if(predict){ 
                            System.out.println("Context : " + current.id + " : preds  0: " + current.zeroCount + " 1: " + current.oneCount);
                            System.out.println("[" + pred + "," + current.id + "] : " + current.ctOfPred(pred) + "/" + current.getTotal());
                            outputs.add("[" + pred + "," + current.id +"]");
                            //outputsProb = outputsProb + "" + current.ctOfPred(pred) + "/" + current.getTotal() + "*";
                            fracs.add(""+current.ctOfPred(pred),""+ current.getTotal());
                            seen = true;
                            break;
                        }

                        /*
                         * If it has not been seen, output an escape character and look at the next shortest context.
                         */
                        else{
                            System.out.println("Context : " + current.id + " : preds  0: " + current.zeroCount + " 1: " + current.oneCount);
                            System.out.println("[$," + current.id + "] : " + current.getEscCnt() + "/" + current.getTotal());
                            outputs.add("[$," + current.id +"],");
                            // outputsProb = outputsProb + "" + current.getEscCnt() + "/" + current.getTotal() + "*";

                            fracs.add(""+current.getEscCnt(),""+ current.getTotal());
                        }

                        //This is when pred has never been seen before in any context : Encode via the order (-1) table.
                        if(!seen){ 
                            System.out.println("[" + pred + ",-1] : 1/" + orderMinusOneProb);
                            outputs.add("[" + pred + ",-1]");
                            // outputsProb = outputsProb + "1/" + orderMinusOneProb +"*";
                            fracs.add("1",""+ orderMinusOneProb);
                        }
                    }
                } 

                /*
                 * Otherwise, there does exists a deterministic context.
                 */
                else{
                    boolean seen = false; //A flag used to determine if the order (-1) table should make the prediction.

                    /*
                     * Looping through contexts to get to the shortest deterministic one.
                     */
                    for(int j = activeIndex.size() -1; j>= 0; j--){

                        //Index of the current context being examined
                        int num = activeIndex.get(j);

                        //Context being used to make the prediction
                        BinaryContext current = model.get(num);     

                        //Contexts are ordered longest to shortest so the first instance the flag is true, the context is found.
                        if(current.id.length() <= shortestDet){

                            //Whether the pred has been seen in the model.get(num) context before 
                            boolean predict = current.isPredicted(pred);

                            //If it has, output the probability and move on to next "stage"
                            if(predict){ 
                                System.out.println("Context : " + current.id + " : preds  0: " + current.zeroCount + " 1: " + current.oneCount);
                                System.out.println("[" + pred + "," + current.id + "] : " + current.ctOfPred(pred) + "/" +current.getTotal());
                                outputs.add("[" + pred + "," + current.id +"]");
                                //outputsProb = outputsProb + "" + current.ctOfPred(pred) + "/" + current.getTotal() + "*";
                                fracs.add(""+current.ctOfPred(pred),""+ current.getTotal());
                                seen = true;
                                break;
                            }

                            //If it has not, output an escape character and look at the next shortest context.
                            else{
                                System.out.println("Context : " + current.id + " : preds  0: " + current.zeroCount + " 1: " + current.oneCount);   
                                System.out.println("[$," + current.id + "] : " + current.getEscCnt() + "/" + current.getTotal());
                                outputs.add("[$," + current.id +"]");
                                //outputsProb = outputsProb + "" + current.getEscCnt() + "/" + current.getTotal() + "*";
                                fracs.add(""+current.getEscCnt(),""+ current.getTotal());
                            }
                        }
                    }

                    //This is when pred has never been seen before in any context : Encode via the order (-1) table.
                    if(!seen){
                        System.out.println("[" + pred + ",-1] : 1/" + orderMinusOneProb);
                        outputs.add("[" + pred + ",-1]");
                        //outputsProb = outputsProb + "1/" + orderMinusOneProb +"*";
                        fracs.add("1",""+ orderMinusOneProb);
                    }
                }
            }

            /*
             * Need to update the model to add pred as a prediction for each of the active contexts.
             */
            for(int n: activeIndex){
                model.get(n).addPred(pred);
                System.out.println(pred + " added to " + model.get(n).id);
            }

            //Updating the model to include new contexts if necessary. 
            HashModelSet hms =  updateModel2(model,x.substring(0,i+1), set);
            model = hms.model;
            set = hms.set;

            //Marker to show we have moved onto the next character in the input to encode
            System.out.println("*****************************");
        }

        /*
         * Looping over the model to print the details of each context.
         * 
         * The ID and the Counts of 0 and 1 in the context
         * E.G.
         * 
         * 000
         * [ 0 , 1 ]
         * [ 2 , 3 ]
         * 
         * means a 0 has occurred 2 times and 1 has occurred 3 times in the context '000'
         */
        for(BinaryContext c: model){
            if(true){
                System.out.println(c.id);
                if(c.zeroCount == 0){
                    System.out.println("[ 1 ]\n[ " + c.oneCount + " ]");
                }
                else if(c.oneCount == 0){
                    System.out.println("[ 0 ]\n[ " + c.zeroCount + " ]");//
                }
                else{
                    System.out.println("[ 0 , 1 ]\n[ " + c.zeroCount + " , " + c.oneCount + " ]");
                }
            }
        }

        //Some details at the end.

        System.out.println(" Contexts: " + modelIDs(model)); //Prints out all strings in the model.
        System.out.println();

        //Prints how the string was predicted at each stage.
        System.out.println("Output of Stages: ");
        System.out.println(outputs); 
        System.out.println();

        //Prints how the probability predictions from each stage.
        System.out.println("The probabilities of the outputs:");       
        System.out.println(fracs);
        System.out.println();

        //Printing some end results
        Container result = new  Container(x, fracs.combine().approxCompressionLength(), model.size(), model.get(model.size()-1).id.length(),pathMetric(model),numDeterministicContexts(model), treeWidth(model));
        System.out.println(result);

        Date endDate = Calendar.getInstance().getTime();
        long d_endTime = new Date().getTime();
        System.out.format("StartDate : %s, EndDate : %s \n", startDate, endDate);
        System.out.format("Milli = %s, ( D_Start : %s, D_End : %s ) \n", (d_endTime - d_StartTime),d_StartTime, d_endTime);
        System.out.println();
        System.out.println(size + " : ");
        return new BinaryContainer2(x,model,fracs);

    }
    /**
     * Given a string "x_1...x_n a" (where a is the bit being encoded) and a list of Contexts, returns the active contexts. 
     * Active contexts are suffixes of the string x_1..x_n that have occurred before, i.e are contained in the inputted Context list. 
     * 
     * EG: in "abracadabra" when encoding the final 'a' the active contexts are "abr, br, r, (empty string)".
     * 
     * @param model: the list of Contexts.
     * @param x: The string where the active contexts will be based on.
     * @return: A subset of model which is a list of the active contexts.
     */

    public static ArrayList<BinaryContext> activeContexts(ArrayList<BinaryContext> model, String x){

        ArrayList<BinaryContext> active = new ArrayList<BinaryContext>();   //Active context id's

        //Empty Context always contained
        active.add(model.get(0));

        if(x.length()==1){ 
            return active;
        }

        //Removing the character being encoded from x.
        x = x.substring(0,x.length() -1);
        int i = x.length() -1;

        boolean found = true; //boolean on if a context is found

        while(i>=0 && found) //Keep searching for longer contexts while they exist. Stop when a suffix is not in the model.
        {
            found = false;

            //Suffix of x : A potential context
            String subSt = x.substring(i);
            for(BinaryContext context : model) //Looping through contexts in the model
            {
                if((context.id).equals(subSt)) //Checking if we have found an active context
                {
                    active = insertContext(active, context); //adding the Context to the list of active contexts.
                    found = true;
                    break; //Context found. Now to find a longer one.

                }
            }
            i--;
        }
        return active;
    }

    /**
     * Given a string "x_1...x_n a" (where a is the bit being encoded) and a list of Contexts, returns the active contexts. 
     * Active contexts are suffixes of the string  x_1..x_n that have occurred before, i.e are contained in the inputted Context list. 
     * 
     * EG: in "abracadabra" when encoding the final 'a' the active contexts are "abr, br, r, (empty context)".
     * 
     * @param model: the list of Contexts.
     * @param x: The string where the active contexts will be based on.
     * @param active: The already existsing active context list. It is emptied and started again.
     * @return: A subset of model which is a list of the active contexts.
     */
    public static ArrayList<BinaryContext> activeContexts(ArrayList<BinaryContext> model, String x, ArrayList<BinaryContext> active){

        //Emptying the current list of active contexts.
        active.clear(); 

        //Empty Context always contained
        active.add(model.get(0));

        if(x.length()==1){ //If we are encoding the first character, we are done.
            return active;
        }

        //Removing the character being encoded from x.
        x = x.substring(0,x.length() -1);

        int i = x.length() -1;
        boolean found = true; //boolean on if a context is found

        while(i>=0 && found) //Keep searching for longer contexts while they exist. Stop when a suffix is not in the model.
        {
            found = false;

            //Suffix of x : A potential context
            String subSt = x.substring(i);

            for(BinaryContext context : model) //Looping through contexts in the model
            {
                if((context.id).equals(subSt)) //Checking if we have found an active context
                {
                    active = insertContext(active, context); //adding the Context to the list of active contexts.
                    found = true;
                    break; //Context found. Now to find a longer one.

                }
            }
            i--;
            // System.out.println("Active: " + modelIDs(active));
        }
        return active;
    }

    /**
     * See activeContexts method. This method returns the respective indexes of the active methods in the original list of Contexts.
     * @param model: the list of Contexts.
     * @param active: the list of Active Contexts.
     * @return: The indexes of the Active Contexts in the original model list of Contexts.
     */
    public static ArrayList<Integer> activeContextsIndex(ArrayList<BinaryContext> model, ArrayList<BinaryContext> active){

        ArrayList<Integer> activeInd = new ArrayList<Integer>();   //Active context indexes
        for(BinaryContext context : active){
            for(int i = 0; i < model.size(); i++){
                BinaryContext current = model.get(i);
                if(context.id.equals(current.id)){
                    activeInd.add(i);
                    break;                              
                }            
            }
        }
        return activeInd;
    }

    /**
     * See activeContexts method. This method returns the respective indexes of the active methods in the original list of Contexts.
     * @param model: the list of Contexts.
     * @param active: the list of Active Contexts.
     * @param activeInd: the already existing activecontext index list that is cleared to be altered
     * @return: The indexes of the Active Contexts in the original model list of Contexts.
     */
    public static ArrayList<Integer> activeContextsIndex(ArrayList<BinaryContext> model, ArrayList<BinaryContext> active, ArrayList<Integer> activeInd){

        //Emptying the current list of active context indexes.
        activeInd.clear();
        for(BinaryContext context : active){
            for(int i = 0; i < model.size(); i++){
                BinaryContext current = model.get(i);
                if(context.id.equals(current.id)){
                    activeInd.add(i);
                    break;                              
                }            
            }

        }
        return activeInd;
    }

    /**
     * Given a list of Contexts, it finds, if it exists, the length of the shortest deterministic context. 
     * Deterministic means the Context makes only one prediction. Shortest is the Context whose id is shortest.
     * 
     * @param contexts: the list of contexts
     * @return: the length of the shortest context if it exists, or -1 if it does not.
     */
    public static int shortestDet(ArrayList<BinaryContext> contexts)
    {
        int shortest = Integer.MAX_VALUE; //length of the shortest deterministic context

        int detCount = 0; //The number of Deterministic Contexts found

        for(BinaryContext currentCtxt: contexts)
        {

            if(currentCtxt.isDeterministic()) //Checks if the context is deterministic
            {
                detCount++;
                shortest = currentCtxt.id.length();
                break;
            }
        }

        if(detCount == 0){ //If no deterministic contexts were found
            shortest = -1;
        }

        return shortest;
    }

    /**
     * Given a model of Contexts and string "x_1...x_n a", the model of Contexts is updated. 
     * This means either updating the counts for Contexts in the list that predict 'a', or creating and
     * adding new Contexts to the list of Contexts.
     * 
     * @param model: The list of Contexts being updated
     * @param str: The string that the model's updates are based on.
     * @return: The updated list of Contexts.
     */
    public static HashModelSet updateModel2(ArrayList<BinaryContext> model, String str,  HashMap<String,Integer> set){

        HashSet<String> ctxt = modelIDs2(model); //The ids of all the contexts.
        //  System.out.println();
        // System.out.println("START UPDATE");
        //  System.out.println("str : " + str);

        HashMap<String,Integer>  unis = set;
        String sub = "";

        int j;
        int len = str.length();

        for(int i = 0; i < len-1; i++) //looping through all substrings from left to right
        {
            //System.out.println("i: " + i);
            j = i+1;
            sub = str.substring(i,j);
            /*
             * Looping through substrings of str from left to right. It checks if the substring is unique in str. If a substring is seen more than once, it is no longer unique and so
             * it's corresponding context must be extended. ie. Create a new longer context.
             */

            /*
             * This while loop adds contexts as follows: It finds existing contexts that are no longer unique and has to extend them. 
             * EG if str = '0100002', this loop adds '00_0' and '000_0' as contexts as they appear multiple times in the string.
             */

            HashBool hb = substringUnique2(sub,str,unis);
            unis = hb.set;

            while(!(hb.bool))  //Making the subtrings longer until it is unique.
            {

                // System.out.println("sub i..j: " + str.substring(i,j));
                // System.out.println("count sub i..j" + countSubstring(str.substring(i,j) , str));
                // System.out.println("coontext contained: " + ctxt.contains( str.substring(i,j)));

                if(!ctxt.contains( sub)){ //if the current substring does not correspond to a Context
                    System.out.println("HERE");
                    ctxt.add(sub);   //add the substring

                    BinaryContext add = new BinaryContext(sub); //Create the new Context

                    add.addPred(str.charAt(j));             //Add the character following the substring as a prediction for the new Context
                    model = insertContext(model,add);       //Insert the new Context to the Model

                    System.out.println("New Context + " + add.id);
                    System.out.println("Inserted " + str.charAt(j) + " in " + sub);

                }

                j++;
                sub = str.substring(i,j);

                hb = substringUnique2(sub,str,unis);
                unis= hb.set;

                if(j >=str.length()) //Stop looping from position i when no more substrings starting there exist.
                {
                    //System.out.println("BREAKING");
                    break;
                }
                // System.out.println("sub i..j: " + sub);
                // System.out.println("count sub i..j" + countSubstring(sub , str));
                // System.out.println("coontext contained: " + ctxt.contains( sub));
            }

            //  System.out.println("out of first loop");
            //  System.out.println("j " + j);

            /*
             * This adds contexts based on what is brand new. It is not extending a context because it is no longer unique. 
             * EG if str = '0100002', this loop adds '0_1', '01_0', '1_0', '0000_2' as contexts.
             */
            if(j <= str.length()-1){ //Checking if there is a substring
                // System.out.println("Substring i..j: " + str.substring(i,j));
                // System.out.println("count sub i..j" + countSubstring(str.substring(i,j) , str));
                // System.out.println("coontext contained: " + ctxt.contains( str.substring(i,j)));
                if(!ctxt.contains(sub)){ //if the current substring does not correspond to a Context
                    System.out.println("THERE");

                    ctxt.add(sub);   //add the substring

                    BinaryContext add = new BinaryContext(sub); //Create the new Context

                    add.addPred(str.charAt(j));             //Add the character following the substring as a prediction for the new Context

                    model = insertContext(model,add);       //Insert the new Context to the Model

                    System.out.println("New Context + " + add.id);
                    System.out.println("Inserted " + str.charAt(j) + " in " + str.substring(i,j));

                }
                // System.out.println(str.substring(i,j) +  ": " + countSubstring(str.substring(i,j) , str));
            }
        }
        //System.out.println("out 2nd if");

        //System.out.println("UPDATE DONE");

        return new HashModelSet(unis,model);

    }

    public static HashBool substringUnique2(String sub, String str, HashMap<String,Integer> set){
        if(!set.containsKey(sub)){
            set.put(sub,1);
            return new HashBool(set, true);
        }
        else{
            if(set.get(sub) != 1){
                return new HashBool(set,false);
            }
            else if(substringUnique(sub,str)){
                return new HashBool(set, true);
            }
            else{
                set.put(sub,0);
                return new HashBool(set, false);
            }
        }
    }

    /**
     * Inserting a Context into a list of Context. The Context is inserted based on the value of its id. 
     * The list is kept in lexicographical order based on the Contexts' ids.
     * 
     * @param model: The list of model where the insertion will take place.
     * @param input: The Context that is inserted into the list.
     * @return: The updated list of Contexts after insertion.
     */
    
    public static ArrayList<BinaryContext> insertContext(ArrayList<BinaryContext> model, BinaryContext input){
        boolean added = false;

        for(int j = (model.size() - 1); j >= 0; j--){ //Looping through the model to find where the Context should be inserted.
            BinaryContext current = model.get(j);
            if(input.compareTo(current) > 0) //Checking if the Context should be inserted at this point based on the Context's id
            {
                model.add(j+1, input);
                added = true;
                break;
            }
        }
        if(added == false){ //If the Context was not added into the middle of the list, add it at the end.
            model.add(input);
        }
        return model;
    }

    /**
     * Takes in a list of Contexts and gives back a list of their ids.
     * @param model: The list of Contexts.
     * @return ids: List of the id's of the model
     */
    public static ArrayList<String> modelIDs(ArrayList<BinaryContext> model){
        ArrayList<String> ids = new ArrayList<String>();
        for(BinaryContext context : model)
        {
            ids.add(context.id);
        }
        return ids;
    }

    public static HashSet<String> modelIDs2(ArrayList<BinaryContext> model){
        HashSet<String> ids = new HashSet<String>();
        for(BinaryContext context : model)
        {
            ids.add(context.id);
        }
        return ids;
    }
    
    /**
     * Takes in a list of Contexts and counts how many are deterministic
     * @param model: The list of Contexts.
     * @return : the number of deterministic contexts
     */
    public static int numDeterministicContexts(ArrayList<BinaryContext> model){
        int count = 0;
        for(BinaryContext c: model){
            if(c.isDeterministic()){
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if a substring occurs only once in a string. This includes overlaps.
     * Eg "ababa" contains "aba" tiwce, "ba" twice and "a" 3 times.
     * @param sub: the substring to look for.
     * @param string: the string to look through
     * @return: boolean on if unique or not.
     */
    public static boolean substringUnique(String sub, String string){

        int iIdxFound = string.indexOf(sub);
        if(iIdxFound == -1){
            return false;
        }
        iIdxFound = string.indexOf(sub,iIdxFound + 1);

        return iIdxFound == -1;
    }

    /**
     * Given a model, it calculates the length of each path of the model's tree and returns 
     * a list of the lengths.
     * @param model: The list of Contexts
     * @ return fracs: The list of the length of each path of the model's tree.
     * 
     *  ~ IRRELEVANT NOW. The metric was useless.
     */
    public static Fraction pathMetric(ArrayList<BinaryContext> model){
        ArrayList<String> contexts = modelIDs(model);
        //ArrayList<String> use = new ArrayList<String>();
        FractionList fracs = new FractionList();
        boolean path = true;
        int i = 0;

        for(String current : contexts){
            int j = i+1;
            for(j = j; j < contexts.size(); j++){

                if(contexts.get(j).startsWith(current)){
                    path = false;
                    break;
                }
            }
            if(path){
                Fraction x = new Fraction("1", "" + current.length());
                fracs.add(x);
                //use.add("1/" + current.length());
            }
            path = true;
            i++;
        }

        Fraction answer = fracs.sum();
        //System.out.println("metric " + answer);
        return answer;
    }
    
    /**
     * ~ IRRELEVANT NOW - the metric was useless
     */
    public static int treeWidth(ArrayList<BinaryContext> model){
        ArrayList<String> contexts = modelIDs(model);

        boolean path = true;
        int i = 0;
        int count = 0;

        for(String current : contexts){
            int j = i+1;
            for(j = j; j < contexts.size(); j++){

                if(contexts.get(j).startsWith(current)){
                    path = false;
                    break;
                }
            }
            if(path){
                count++;
            }
            path = true;
            i++;
        }
        return count;
    }

}

