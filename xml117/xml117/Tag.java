/**
 * The class {@code Tag} contaings methods for wrapping XML tags around
 * text content to create XML elements.  The methods of {@code tW}, 
 * (which is short for tag wrapper), are designed to create nested 
 * elements, and/or tags with attribute values. The attriubute tags can 
 * be self-closing or open, they will be open if {@code String} beta
 * attributes follow the integer or boolean values. {@code tW} can be
 * called recursively to mimic the document structure required by 
 * the MFA Feed_117.
 *
 * @param   alpha is the element tag
 * @param   beta is the text content to be wrapped
 * @param   gamma is an attribute to be followed by an int or boolean
 *          gamma is usually "marketplace_name" or "delete"
 * @param   j is the value of the attribute gamma
 * @param   bool is the value for a boolean attribute gamma
 * @return  A string of well-formed XML elements
 **/

package xml117;

public final class Tag {

    private Tag() {}
          
    public static String tW(String alpha, String ... beta) {
        String segment, temp = "";

        for(int i=0; i < beta.length; i++){ temp += beta[i]; }

        segment = "<" + alpha + ">" + temp + "</" + alpha + ">";
        return segment;
    }

    public static String tW(String alpha, String gamma, int j, String ... beta) { // non self-closing
          String segment, temp = "";

          for(int i=0; i < beta.length; i++){ temp += beta[i]; }

          segment =  "<" + alpha + " " + gamma + "=\"" + String.valueOf(j) + "\">";
          segment += temp + "</" + alpha + ">";
          return segment;
    }

    public static String tW(String alpha, String gamma, int j) { // delete needs only self-closing elements
          String segment = "<" + alpha + " " + gamma + "=\"" + String.valueOf(j) + "\"/>";
          return segment;
    }

    public static String tW(String alpha, String gamma, boolean bool) { // delete needs only self-closing elements
          String segment = "<" + alpha + " " + gamma + "=\"" + String.valueOf(bool) + "\"/>";
          return segment;
    } 
}