package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * This class covers the data-format for the Funcom dataset. More information on said dataset:
 * http://leclair.tech/data/funcom/
 */
public class FuncomDataset implements Dataset {

  /**
   * Extracts the jsonl files from the given directory. Altered to suite the Funcom dataset.
   *
   * @param s_CommandLineValues comman line arguments.
   */
  @Override
  public void extractDir(CommandLineValues s_CommandLineValues) {
    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(s_CommandLineValues.NumThreads);
    LinkedList<ExtractFeaturesTask> tasks = new LinkedList<>();
    try {
      Files.walk(Paths.get(s_CommandLineValues.Dir))
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().toLowerCase().endsWith(".jsonl"))
          .forEach(
              f -> {
                // For each file in the directory, read all of its code lines
                List<String> codeLines;
                try {
                  codeLines = Files.readAllLines(f);
                } catch (IOException e) {
                  e.printStackTrace();
                  codeLines = new ArrayList<>();
                }
                // For each code line create a new ExtractFeaturesTask
                codeLines.forEach(
                    line -> {
                      ExtractFeaturesTask task =
                          new ExtractFeaturesTask(s_CommandLineValues, removeBrackets(line));
                      tasks.add(task);
                    });
              });
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    List<Future<Void>> tasksResults = null;
    try {
      tasksResults = executor.invokeAll(tasks);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      executor.shutdown();
    }
    tasksResults.forEach(
        f -> {
          try {
            f.get();
          } catch (InterruptedException | ExecutionException e) {
            // e.printStackTrace();
          }
        });
  }

  /**
   * Extracts the code from the given file.
   *
   * @param s_CommandLineValues comman line arguments.
   * @param fileConent contents of the given file.
   */
  @Override
  public void extractFile(CommandLineValues s_CommandLineValues, String fileContent) {
    ExtractFeaturesTask ex =
        new ExtractFeaturesTask(s_CommandLineValues, removeBrackets(fileContent));
    ex.process();
  }

  /**
   * Method removes curly brackets from the data.
   *
   * @param codeLine codeLine with brackets to remove from.
   */
  private String removeBrackets(String codeLine) {
    String code =
        codeLine.substring(2, codeLine.length() - 3); // Remove json object remnants from string
    return StringEscapeUtils.unescapeJava(code); // Unescape characters so it parses to Java
  }
}
