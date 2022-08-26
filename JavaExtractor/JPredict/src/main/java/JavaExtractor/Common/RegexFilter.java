package JavaExtractor.Common;

public class RegexFilter {

  /**
   * Check if given comment contains any code.
   *
   * @param comment - string comment of a node
   * @return true if code is recognised.
   */
  public static boolean containsCode(String comment) {
    return ("//" + comment).matches("^\\s*.*;\\s*$");
  }
}
