/**
 * The class {@code HelperUtilities} has a few static methods used throughout the package
 *
 * @method  printBlanks() sends blank lines to standard output
 * @method  printExceptionMethod() sends error messages to standard output
 **/

package xml117;

public class HelperUtilities {

    private HelperUtilities() {}

    public static void printBlanks(int i) {for(int j=0; j<i; j++) System.out.println(); }

    public static void printExceptionMessage(Exception excep, String str) {
        System.out.println("Warning: " + str +" error\n" + excep);
    }
}
