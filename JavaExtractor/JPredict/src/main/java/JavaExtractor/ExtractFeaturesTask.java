package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ExtractFeaturesTask implements Callable<Void> {
  private final CommandLineValues m_CommandLineValues;
  public String code;
  private FeatureExtractor featureExtractor;

  public ExtractFeaturesTask(CommandLineValues commandLineValues, String code) {
    m_CommandLineValues = commandLineValues;
    this.code = code;
    featureExtractor = new FeatureExtractor(m_CommandLineValues);
  }

  @Override
  public Void call() {
    process();
    return null;
  }

  public void process() {
    ArrayList<ProgramFeatures> features;
    try {
      this.code = connectOrphanComments(code);
      features = extractSingleFile();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    if (features == null) {
      return;
    }

    String toPrint = featureExtractor.featuresToString(features);
    if (toPrint.length() > 0) {
      System.out.println(toPrint);
    }
  }

  private ArrayList<ProgramFeatures> extractSingleFile() throws IOException {
    if (m_CommandLineValues.MaxFileLength > 0
        && code.lines().count() > m_CommandLineValues.MaxFileLength) {
      return new ArrayList<>();
    }

    return featureExtractor.extractFeatures(code);
  }

  /**
   * Iterate over the orphan comments in the code snippet and replace them with merged single line
   * comments. The pattern can be decyphered as follows: <.*> - Match enything before the comment
   * signs zero or more times (whitespace) <\\/\\/.*> - Match the // sign of a singel line comment
   * <(\n)*> - Match a newline character zero or more times <(...){2,}> - there must be two or more
   * of such occurences.
   *
   * @param original the original string
   * @return a string with words replaced with their lowercase equivalents
   */
  public String connectOrphanComments(String original) {
    int lastIndex = 0;
    StringBuilder output = new StringBuilder();
    Pattern pattern = Pattern.compile("(.*\\/\\/.*(\n)*){2,}", Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(original);

    while (matcher.find()) {
      output
          .append(original, lastIndex, matcher.start())
          .append(concatinateComments(matcher.group(0)));

      lastIndex = matcher.end();
    }

    if (lastIndex < original.length()) {
      output.append(original, lastIndex, original.length());
    }

    return output.toString();
  }

  public String concatinateComments(String comments) {
    StringJoiner sj = new StringJoiner(" ", "// ", "\n");
    comments
        .lines()
        .filter(Predicate.not(String::isEmpty))
        .forEach(
            l -> {
              sj.add(
                  l.replaceAll(
                      ".*//(\\s)*", "")); // remove comment signs with any whitespace before text
            });
    return sj.toString();
  }
}
