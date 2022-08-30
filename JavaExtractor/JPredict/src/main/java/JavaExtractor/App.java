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

    if (s_CommandLineValues.File != null) {
      String code;
      try {
        code = new String(Files.readAllBytes(s_CommandLineValues.File.toPath()));
      } catch (IOException e) {
        e.printStackTrace();
        code = Common.EmptyString;
      }

      ExtractFeaturesTask extractFeaturesTask = new ExtractFeaturesTask(s_CommandLineValues, code);
      extractFeaturesTask.processFile();
    } else if (s_CommandLineValues.Dir != null) {
      switch (s_CommandLineValues.ds) {
        case CODESEARCHNET:
          new CodeSearchNetDataset().extractDir(s_CommandLineValues);
        default:
          new DefaultDataset().extractDir(s_CommandLineValues);
      }
    }
  }
}
