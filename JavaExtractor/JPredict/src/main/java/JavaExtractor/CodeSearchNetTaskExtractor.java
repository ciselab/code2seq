package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import org.json.JSONObject;

class CodeSearchNetTaskExtractor implements Callable<Void> {
  private final CommandLineValues m_CommandLineValues;
  private final String fileLine;
  FeatureExtractor featureExtractor;

  public CodeSearchNetTaskExtractor(CommandLineValues commandLineValues, String fileLine) {
    m_CommandLineValues = commandLineValues;
    this.fileLine = fileLine;
    featureExtractor = new FeatureExtractor(m_CommandLineValues);
  }

  @Override
  public Void call() {
    processLines();
    return null;
  }

  public void processLines() {
    ArrayList<ProgramFeatures> features;
    try {
      features = extractSingleLine();
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

  private ArrayList<ProgramFeatures> extractSingleLine() throws IOException {

    // Modified to only extract strings, not files;
    String code = collectJson(fileLine);

    return featureExtractor.extractFeatures(code);
  }

  private String collectJson(String json) {
    JSONObject jo = new JSONObject(json);

    StringBuilder full_entry = new StringBuilder();
    StringBuilder javaDoc = new StringBuilder().append("/**");
    String doc = (String) jo.get("docstring");

    String summary =
        doc.lines()
            .filter(
                l ->
                    !l.contains("=")
                        && !l.contains("-")
                        && !l.startsWith("<")
                        && !l.startsWith("(")
                        && !l.startsWith("@")
                        && l.split("[\\s+]").length >= 2)
            .findFirst()
            .orElseThrow();

    javaDoc.append(summary + "\n");
    javaDoc.append("*/\n");

    full_entry.append(javaDoc);
    full_entry.append(jo.get("original_string") + "\n");

    return full_entry.toString();
  }
}
