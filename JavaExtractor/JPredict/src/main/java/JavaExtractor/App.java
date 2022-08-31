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
        break;
      default:
        dataset = new DefaultDataset();
    }

    if (s_CommandLineValues.File != null) {
      String code;
      try {
        code = new String(Files.readAllBytes(s_CommandLineValues.File.toPath()));
        System.out.println(code);
      } catch (IOException e) {
        e.printStackTrace();
        code = Common.EmptyString;
      }
      dataset.extractFile(s_CommandLineValues, code);
    } else if (s_CommandLineValues.Dir != null) {
      dataset.extractDir(s_CommandLineValues);
    }
  }
}
