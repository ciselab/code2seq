package JavaExtractor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import JavaExtractor.Common.CommandLineValues;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

public class OrphanCommentsTest {

  @Test
  void testConnectOrphanComments() throws CmdLineException, IOException {
    String testFilePath = "src/test/resources/examples/orphanComment.java";
    String[] results = {"// first second third", "// hello world", "// re turn"};
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
    code = eft.connectOrphanComments(code);
    for (String r : results) {
      assertTrue(code.contains(r));
    }
  }

  @Test
  void testSingleCommentUnchanged() throws CmdLineException, IOException {
    String testFilePath = "src/test/resources/examples/comments.java";
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
    String new_code = eft.connectOrphanComments(code);
    assertTrue(code.equals(new_code));
  }

  @Test
  void testNewLineBetweenComments() throws CmdLineException, IOException {
    String testFilePath = "src/test/resources/examples/orphanCommentNewLine.java";
    String result = "// first second third";
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
    code = eft.connectOrphanComments(code);
    assertTrue(code.contains(result));
  }
}
