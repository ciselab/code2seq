package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import java.io.IOException;
import java.nio.file.Files;
import org.kohsuke.args4j.CmdLineException;

public class App {
  private static CommandLineValues s_CommandLineValues;

  public static void main(String[] args) {
    try {
      s_CommandLineValues = new CommandLineValues(args);
    } catch (CmdLineException e) {
      e.printStackTrace();
      return;
    }

    Dataset dataset;
    switch (s_CommandLineValues.ds) {
      case CODESEARCHNET:
        dataset = new CodeSearchNetDataset();
        // case FUNCOM:
      default:
        dataset = new DefaultDataset();
    }

    if (s_CommandLineValues.File != null) {
      String filePath;
      try {
        filePath = new String(Files.readAllBytes(s_CommandLineValues.File.toPath()));
      } catch (IOException e) {
        e.printStackTrace();
        filePath = Common.EmptyString;
      }
      dataset.extractFile(s_CommandLineValues, filePath);
    } else if (s_CommandLineValues.Dir != null) {
      dataset.extractDir(s_CommandLineValues);
    }
  }
}
