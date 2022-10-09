package JavaExtractor;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

public class ParseJsonTest {

  @Test
  void testNewLineBetweenComments() throws CmdLineException, IOException {
    String testFilePath = "src/test/resources/jsonls/jsonWithHtml.jsonl";
    String code = Files.readString(Path.of(testFilePath));

    CodeSearchNetDataset ds = new CodeSearchNetDataset();
    code = ds.parseJson(code);
    assertFalse(code.contains("<p>"));
    assertFalse(code.contains("</p>"));
    assertFalse(code.contains("<a>"));
  }
}
