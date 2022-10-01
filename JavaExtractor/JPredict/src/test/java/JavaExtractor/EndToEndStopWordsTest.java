package JavaExtractor;

public class EndToEndStopWordsTest {

  private String testFile = "src/test/resources/examples/stopWordsComment.java";
  private String[] stopWordsIntTestCase = {"and", "or", "at", "be"};

  // @Test
  // void testThatAppReturnsNoStopWordsWhenFlagIsSet()
  //     throws IOException, CmdLineException, Exception {
  //
  //   String[] args = {
  //     "--file",
  //     testFile,
  //     "--max_path_length",
  //     "200",
  //     "--max_path_width",
  //     "10",
  //     "--include_comments",
  //     "--exclude_stopwords"
  //   };
  //
  //   String output =
  //       tapSystemOut(
  //           () -> {
  //             App.main(args);
  //           });
  //
  //   for (String sw : stopWordsIntTestCase) {
  //     assertFalse(output.contains(sw));
  //   }
  // }
  //
  // @Test
  // void testThatAppReturnsStopWordsWhenFlagIsNotSet()
  //     throws IOException, CmdLineException, Exception {
  //   String[] args = {
  //     "--file", testFile, "--max_path_length", "200", "--max_path_width", "10",
  // "--include_comments"
  //   };
  //
  //   String output =
  //       tapSystemOut(
  //           () -> {
  //             App.main(args);
  //           });
  //
  //   for (String sw : stopWordsIntTestCase) {
  //     assertTrue(output.contains(sw));
  //   }
  // }
}
