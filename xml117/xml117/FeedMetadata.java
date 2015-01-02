/**
 * Reads input file and loads metadata to object FeedMetaData.
 * FeedMetaData object is used by Parse class to create XML output.
 *
 * testInputFile()          tests the input file for contextual errors.
 * 
 * Getters:
 * isDataValid()            true if the datafile is OK to load
 * getHeaderData()          the contents of row 1, 4 elements
 * getAttribute()           the fields in second row of input file
 * isMarketplaceAttribute   indicates attribute is a special type for parsing
 * getInputfileName()       the name of the data file relative to working directory
 **/

package xml117;
import java.io.*;

public class FeedMetadata {
    private static String[] marketplaceAttributes = {"gl_product_group_type", "display_on_website", "display_attribute"};
    private int numberOfAttributes;
    private boolean marketplaceAttributeFlag[], datafileStatus;
    private String[] headerdata, attribute;
    private String inputfileName;

    FeedMetadata(String filename) {

        inputfileName = filename;

        try ( BufferedReader reader = new BufferedReader(new FileReader(filename))) 
        {
            String line = reader.readLine();
            headerdata = line.split("\t"); 

            line = reader.readLine();
            attribute = line.split("\t");
            
            numberOfAttributes = attribute.length;

            marketplaceAttributeFlag = new boolean[numberOfAttributes];
            for(int i=0; i < numberOfAttributes; i++) { 
                for(String alpha : marketplaceAttributes) {
                    if(attribute[i].equals(alpha)) marketplaceAttributeFlag[i] = true;
                }
            }

            datafileStatus = testInputfile(reader);

        } catch(IOException e) { HelperUtilities.printExceptionMessage(e, "FeedMetadata"); } 
    }

    public boolean isDataValid(){
        return datafileStatus;
    }

    public String getInputfileName(){
        return inputfileName;
    }

    public String getHeaderdata(int i){
        return headerdata[i];
    }

    public String getAttribute(int i){
        return attribute[i];
    }

    public boolean isMarketplaceAttribute(int i){
        return marketplaceAttributeFlag[i];
    }

    private boolean testInputfile(BufferedReader br) throws IOException {  
        String[] rowAttributes;
        int rowCount = 2;
        boolean status = true;        

        if(headerdata.length != 4){
            System.out.println("Warning: Header row should have 4 fields");
            System.out.println("         Contents of header row");
            for(int i=0; i < headerdata.length; i++) {
                System.out.println("         Header field " + i + ": " + headerdata[i]);
            }
            status = false;
        }

        String line = br.readLine();

        while(line != null) {
            rowCount++;
            rowAttributes = line.split("\t");
            if(rowAttributes.length > numberOfAttributes) {
                System.out.println("Warning: Row overflows attribute columns on row: " + (rowCount) ); 
                status = false;
            }

            line = br.readLine();
        }

        return status;
    }
}