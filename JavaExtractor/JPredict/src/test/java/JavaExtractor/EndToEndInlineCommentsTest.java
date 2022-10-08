package JavaExtractor;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

public class EndToEndInlineCommentsTest {
  private String testFile = "src/test/resources/examples/comments.java";
  private String inlineCommentParsed = "inline|comment";

  @Test
  void testThatAppReturnsInlineCommentsWhenFlagIsSet()
      throws IOException, CmdLineException, Exception {

    String[] args = {
      "--file", testFile, "--max_path_length", "200", "--max_path_width", "10", "--include_comments"
    };

    String output =
        tapSystemOut(
            () -> {
              App.main(args);
            });

    assertTrue(output.contains(inlineCommentParsed));
  }

  @Test
  void testThatAppReturnsNoInlineCommentsWhenFlagIsNotSet()
      throws IOException, CmdLineException, Exception {

    String[] args = {"--file", testFile, "--max_path_length", "200", "--max_path_width", "10"};

    String output =
        tapSystemOut(
            () -> {
              App.main(args);
            });

    assertFalse(output.contains(inlineCommentParsed));
  }
}
