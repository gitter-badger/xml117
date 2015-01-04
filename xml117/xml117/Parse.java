/**
 * parseXML()       A static method that reads a tab delimited text file of ASIN
 *                  attributes and writes them into an XML document for upload 
 *                  to the MFA VLP in the format for Data Augmenter contributions.
 * 
 * The method skips rows with empty ASIN column, and empty cells.  It also
 * leaves the <AmazonEnvelope> tag open. This allows the data file to be parsed 
 * line-by-line and not loaded into memory. 
 * It is common to update 100K+ rows of ASIN data.
 *
 * writeHeader()    Static method that writes a standard header to the XML file.
 * 
 * MFA panel, internal Amazon tool:
 * (https://mfa-ops-tool-na-prod.amazon.com/feedUpload.html?feedType=117)
 *
 * The following two XML series are handled separately:
 * marketplaceDataLoop          ASIN attributes flagged as marketplace data
 * additionalDataLoop           ASIN attributes flagged as normal
 **/

package xml117;
import java.io.*;

public class Parse {

    private Parse() {}

    public static void parseXML(FeedMetadata feedObj) {
        int countRows = 0;
        String line = "";
        String marketplaceDataLoop, additionalDataLoop;
        String[] productdata;

        if(!feedObj.isDataValid()) {
            System.out.println("Warning: Input file not valid, ending.");
            return;
        }

        try ( BufferedReader reader = new BufferedReader(
                                      new FileReader(feedObj.getInputfileName() )); 
              BufferedWriter writer = new BufferedWriter(
                                      new FileWriter(feedObj.getInputfileName() + "_117.xml")) 
        ) {
            writeHeader(feedObj, writer);

            for(int i = 1; i <= 3; i++) line = reader.readLine();

            while(line != null) {
                productdata = line.split("\t");
              
                if(productdata[0].length() != 10) {
                    line = reader.readLine();
                    continue;
                }

                countRows++;

                marketplaceDataLoop = "";
                additionalDataLoop  = "";
                for(int i=1; i<productdata.length; i++) {
        
                    if(productdata[i] != null && !productdata[i].isEmpty()){  // skip empty cells

                        if(feedObj.isMarketplaceAttribute(i)) {
                            marketplaceDataLoop += Tag.tW(feedObj.getAttribute(i)
                                                         , Tag.tW("value", productdata[i])
                                                         );
                        } else {
                             additionalDataLoop += Tag.tW(feedObj.getAttribute(i)
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
                                            , marketplaceDataLoop
                                            )
                                        , Tag.tW("AdditionalData"
                                            , Tag.tW("itemId"
                                                , Tag.tW("value", productdata[0])
                                                )
                                            , additionalDataLoop
                                            )
                                        )
                                    ) );
    
                line = reader.readLine();
            }

            writer.write("</AmazonEnvelope>"); // save memory, not full recursion
    
            HelperUtilities.printBlanks(2);
            System.out.println("                - - Your .xml File is ready - -"); 
            System.out.println("                - - ASIN data rows complete: " + countRows); 
            System.out.println("                - - Thanks    for   parsing - - ");
            HelperUtilities.printBlanks(2); 

        } catch(IOException e) { HelperUtilities.printExceptionMessage(e, "Parse"); } 
    }

    private static void writeHeader(FeedMetadata feedObj, BufferedWriter bw) throws IOException {
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