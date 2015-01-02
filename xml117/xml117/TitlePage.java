/**
 * Displays the application title and general introduction.
 * Reads a text resource file and displays it to standard output.
 **/

package xml117;
import java.io.*;

public class TitlePage {

    private TitlePage() {}

    public static void displayTitlepage() {
        String line;
        String titlefile = ".\\rsrc\\SplashXML117.txt";

        try (BufferedReader reader = new BufferedReader(
                                     new FileReader(titlefile)) ) 
        { 
            while((line = reader.readLine()) != null) System.out.println(line);
          
        } catch(IOException e) {
            HelperUtilities.printExceptionMessage(e, "Title file");
        }
    }
}