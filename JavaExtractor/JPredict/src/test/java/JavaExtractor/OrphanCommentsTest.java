package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

public class OrphanCommentsTest {

  @Test
  void testConcatinateConsecutiveComments() throws CmdLineException, IOException {
    String testFilePath = "src/test/resources/examples/orphanComment.java";
    String[] args = {
      "--file",
      testFilePath,
      "--max_path_length",
      "200",
      "--max_path_width",
      "10",
      "--include_comments"
    };

    CommandLineValues clv = new CommandLineValues(args);

    String code = Files.readString(Path.of(testFilePath));
    ExtractFeaturesTask eft = new ExtractFeaturesTask(clv, code);
    // System.out.println(eft.code); eft.solveOrphanComments();
  }
}
