/**
 * Reads a tab delimited text file of ASIN attributes
 * and writes them into an XML document for upload to the MFA VLP 
 * (https://mfa-ops-tool-na-prod.amazon.com/feedUpload.html?feedType=117),'
 * which is the format for Data Augmenter contributions to ASINs.
 * Tests are made to catch errors caused by empty cells, rows that are
 * longer than the attribute list (row 2), and skipping empty ASIN inputs.
 * Before Parse can be instantiated, the {@code FeedMetadata} object must
 * be created and passed to {@code Parse}.
 *
 * The field mpDataLoop holds the ASIN attribute flagged as marketplace data,
 * while the addlDataLoop field holds the normal ASIN attribute.  These two
 * sets are wrapped separately in tags as part of each message.  The 
 * productdata[] array is assigned the new row's ASIN and attribute data
 * as the BufferedReader reads the file line-by-line.
 * 
 * See below for a sample of a Feed_117 XML envelope.  The main writer clause
 * mimics the form of the required feed.
 * 
 * Method {@code writeHeader} writes the header block of the XML document,
 * and leaves the <AmazonEnvelope> tag open. This allows the data file 
 * to be parsed line-by-line and not loaded into memory.  It is common to
 * update 100K+ rows of ASIN data.
 *
 * @param   feedObj is the container of the marketplace ID, merchant ID,
 *          and ASIN attribute list
 * @param   filename is the data file, usually in same directory as java package
 * @param   mpDataLoop
 **/

package xml117;
import java.io.*;

public class Parse {
    private int countRows=0;
    private String line, mpDataLoop, addlDataLoop;
    private String[] productdata;

    public Parse(FeedMetadata feedObj, String filename) {  // constructor
        if(!feedObj.isDataValid()) return;

        try ( BufferedReader reader = new BufferedReader(
                                      new FileReader(filename)); 
              BufferedWriter writer = new BufferedWriter(
                                      new FileWriter(filename + "_117.xml")) 
        ) {
            writeHeader(feedObj, writer);

            for(int i = 1; i < 3; i++) line = reader.readLine();    //skip first two lines (metadata lines)

            line = reader.readLine();

            SKIPROW:
            while(line != null) {
                countRows++;
                productdata = line.split("\t");
              
                if(productdata[0].length() != 10) {
                    line = reader.readLine();
                    countRows--;
                    continue SKIPROW;  
                }

                mpDataLoop = "";
                addlDataLoop = "";
                for(int i=1; i<productdata.length; i++) { // the two attribute types, skip empty cells
        
                    if(productdata[i] != null && !productdata[i].isEmpty()){

                        if(feedObj.getMarketplaceAttributeFlag(i)) {
                            mpDataLoop += Tag.tW(feedObj.getAttribute(i)
                                                , Tag.tW("value", productdata[i])
                                                );
                        } else {
                          addlDataLoop += Tag.tW(feedObj.getAttribute(i)
                                                , Tag.tW("value", productdata[i])
                                                );
                        }
                    }
                    
                }

                writer.write( Tag.tW("Message"
                                    , Tag.tW("MessageID", String.valueOf(countRows))
                                    , Tag.tW("OperationType", "PartialUpdate")
                                    , Tag.tW("Item"
                                        , Tag.tW("sku"
                                            , Tag.tW("value", productdata[0])
                                        )
                                        , Tag.tW("MarketplaceData", "market_name", Integer.parseInt(feedObj.getHeaderdata(1))
                                            , mpDataLoop
                                            )
                                        , Tag.tW("AdditionalData"
                                            , Tag.tW("itemId"
                                                , Tag.tW("value", productdata[0])
                                                )
                                            , addlDataLoop
                                            )
                                        )
                                    ) );
    
                line = reader.readLine();
            }

            writer.write("</AmazonEnvelope>"); // save memory, not full recursion
    
            HelperUtilities.printBlanks(2);
            System.out.println("                - - Your .xml File is ready - -"); 
            System.out.println("                - - ASIN data rows parsed: " + countRows); 
            System.out.println("                - - Thanks    for   playing - - ");
            HelperUtilities.printBlanks(2); 

        } catch(IOException e) { HelperUtilities.printExceptionMessage(e, "Parse"); } 
    }

    private void writeHeader(FeedMetadata feedObj, BufferedWriter bw) throws IOException {
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        bw.write("<AmazonEnvelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        bw.write(" xsi:noNamespaceSchemaLocation=\"amzn-envelope.xsd\">");
        bw.write( Tag.tW("Header"
                        , Tag.tW("DocumentVersion","1.01")
                        , Tag.tW("MerchantIdentifier",feedObj.getHeaderdata(3))
                        ) );
        bw.write( Tag.tW("MessageType","Item") );
    }
}