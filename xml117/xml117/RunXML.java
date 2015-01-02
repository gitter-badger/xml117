 /**
 *  Compile/Execute this application from the parent folder of ..\xml117\
 *  Compilation:  javac xml117\*.java
 *  Execution:    java xml117.RunXML .\rsrc\test.txt
 *
 *  Parses a tab-delimited test file into XML for the MFA VLP feedType=117
 *
 *  Resources folder \rsrc\ contains:
 *  test.txt                            test input file
 *  test.txt_117.xml                    output file generated by RunXML.java
 *  test.txt_117_Correct.xml            verified output file for test comparison
 *  Example117UpdateAttributes.xml      an example 117 feed for update
 *  Example117DeleteAttributes.xml      an example 117 feed for deleting selected attributes
 *  Example117DeleteAllAttributes.xml   an example 117 feed for deleting all
 *                                      contributions from the target merchant ID
 *  log.txt                             log of debugging test executions
 **/
 
package xml117;

public class RunXML {
    public static void main(String[] args) {

        HelperUtilities.printBlanks(2);

        TitlePage.displayTitlepage();

        Help helpObject = new Help();  // determine later if a help object is really needed
        boolean helpStatus = helpObject.helpUsage(args);

        if(helpStatus) {
            FeedMetadata feedData = new FeedMetadata(args[0]);

                // here will go control based on feedData header (Update, DeleteAttributes, DeleteAll)
            Parse.parseXML(feedData);
        }
    }
}