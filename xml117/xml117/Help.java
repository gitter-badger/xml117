/**
 *  Display instructions and test input arguments, file location and format
 *
 *  Help responds to missing input file, and provides isntructions for 
 *  required format of input feeds.
 *
 *  NOT COMPLETED
 *  Add help screen functionality, prompt for instructions
 **/

package xml117;
import java.io.*;

public class Help {

    Help() {        // constructor NOT COMPLETE
        String helpFile = "helpfile.txt";
    }

    public static boolean helpUsage(String[] input) {
        if(input.length == 0) { 
            System.out.println("Warning: No data file specified.\nUsage:   java xml117.RunXML <inputfile>.txt");
            System.out.println("         Ending.");

            HelperUtilities.printBlanks(2);
            return false; 

        } else if(input.length > 1) { 
            System.out.println("Warning: Too many arguments.\nUsage:   java xml117.RunXML <inputfile>.txt");
            System.out.println("         Ending.");

            HelperUtilities.printBlanks(2);
            return false; 
        
        } else {
            return true;
        }
    }
}