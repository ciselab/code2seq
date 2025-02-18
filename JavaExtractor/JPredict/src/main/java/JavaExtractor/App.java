package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import java.io.IOException;
import java.nio.file.Files;
import org.kohsuke.args4j.CmdLineException;

public class App {
  private static CommandLineValues s_CommandLineValues;

  public static void main(String[] args) throws CmdLineException {
    s_CommandLineValues = new CommandLineValues(args);

    Dataset dataset;
    switch (s_CommandLineValues.ds) {
      case CODESEARCHNET:
        dataset = new CodeSearchNetDataset();
        break;
      case FUNCOM:
        dataset = new FuncomDataset();
        break;
      default:
        dataset = new DefaultDataset();
    }

    if (s_CommandLineValues.File != null) {
      String code;
      try {
        // In case a single file is given
        // read all the contents immediately
        // and pass it on to be processed.
        code = new String(Files.readAllBytes(s_CommandLineValues.File.toPath()));
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
