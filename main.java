import java.util.Scanner;


public class main
{
    /**
     * Example. Encoding the string fullDB(4)
     */
    public static void main(String[] args){

        System.out.print("Encoding the string DeBruijn.makeDBOdd(3) \n\n" + DeBruijn.makeDBOdd(3) + "\n...\n");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BinaryContainer2 currentCont = PPM.encode(DeBruijn.makeDBOdd(3));
        
        
        System.out.print("\n \n \n \n \n \n");
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a binary string of your choice to compress - i.e. only strings containing 0 and 1");
        String input = scan.nextLine();
        
        
        System.out.print("Encoding the string \n\n" + input + "\n...\n");
        BinaryContainer2 currentCont2 = PPM.encode(input);
    }
}
