package JavaExtractor.Common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class StopWordsFilter {

  public static List<String> stopwords;

  /** Get list of stopwords from dataset */
  public static void setup() {
    try {
      stopwords = IOUtils.readLines(StopWordsFilter.class.getClassLoader().getResourceAsStream("stop_words.txt"), "UTF-8");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * Remove stopwords from a string
   *
   * @param input - string to modify
   * @return input without stopwords
   */
  public static String removeStopWords(String input) {

    setup();

    String[] allWords = input.split(" ");
    StringBuilder builder = new StringBuilder();
    for (String word : allWords) {
      if (!stopwords.contains(word)) {
        builder.append(word);
        builder.append(" ");
      }
    }
    return builder.toString().trim();
  }
}
