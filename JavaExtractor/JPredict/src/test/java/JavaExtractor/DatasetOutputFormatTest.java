package JavaExtractor;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests whether the correct format has been selected for the output, debending on the selected
 * dataset.
 */
public class DatasetOutputFormatTest {

  @Test
  void testThatDefaultDatasetTakesMethodNameAsLabel() throws Exception {
    String[] args = {
      "--file",
      "src/test/resources/TestDefault.java",
      "--max_path_length",
      "200",
      "--max_path_width",
      "10"
    };
    String methodName = "add";

    String output =
        tapSystemOut(
            () -> {
              App.main(args);
            });

    System.out.println(output);
    assertTrue(output.startsWith(methodName));
  }

  @Test
  void testThatCodeSearchNetDatasetTakesJavaDocAsLabel() throws Exception {
    String[] args = {
      "--file",
      "src/test/resources/TestCSN.java",
      "--max_path_length",
      "200",
      "--max_path_width",
      "10",
      "--dataset",
      "codesearchnet"
    };
    String javaDoc = "test|string";

    String output =
        tapSystemOut(
            () -> {
              App.main(args);
            });

    System.out.println(output);
    assertTrue(output.startsWith(javaDoc));
  }

  @Test
  void testThatFuncomDatasetTakesJavaDocAsLabel() throws Exception {
    String[] args = {
      "--file",
      "src/test/resources/TestFuncom.java",
      "--max_path_length",
      "200",
      "--max_path_width",
      "10",
      "--dataset",
      "funcom"
    };
    String javaDoc = "gets|the|sort|name";

    String output =
        tapSystemOut(
            () -> {
              App.main(args);
            });

    System.out.println(output);
    assertTrue(output.startsWith(javaDoc));
  }
}
