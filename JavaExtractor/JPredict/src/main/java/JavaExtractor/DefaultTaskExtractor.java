package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Callable;

class DefaultTaskExtractor implements Callable<Void> {
  private final CommandLineValues m_CommandLineValues;
  private final Path filePath;
  private FeatureExtractor featureExtractor;

  public DefaultTaskExtractor(CommandLineValues commandLineValues, Path path) {
    m_CommandLineValues = commandLineValues;
    this.filePath = path;
    featureExtractor = new FeatureExtractor(m_CommandLineValues);
  }

  @Override
  public Void call() {
    processFile();
    return null;
  }

  public void processFile() {
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
    String code;

    if (m_CommandLineValues.MaxFileLength > 0
        && Files.lines(filePath, Charset.defaultCharset()).count()
            > m_CommandLineValues.MaxFileLength) {
      return new ArrayList<>();
    }
    try {
      code = new String(Files.readAllBytes(filePath));
    } catch (IOException e) {
      e.printStackTrace();
      code = Common.EmptyString;
    }

    return featureExtractor.extractFeatures(code);
  }
}
