package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class ExtractFeaturesTask implements Callable<Void> {
  private final CommandLineValues m_CommandLineValues;
  private final String fileContents;
  private FeatureExtractor featureExtractor;

  public ExtractFeaturesTask(CommandLineValues commandLineValues, String fileContents) {
    m_CommandLineValues = commandLineValues;
    this.fileContents = fileContents;
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
        && fileContents.lines().count() > m_CommandLineValues.MaxFileLength) {
      return new ArrayList<>();
    }

    return featureExtractor.extractFeatures(fileContents);
  }
}
