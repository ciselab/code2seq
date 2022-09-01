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
import org.json.JSONObject;

public class CodeSearchNetDataset implements Dataset {

  /**
   * Extracts the jsonl files from the given directory. Altered to suite the CodeSearchNet dataset.
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
                List<String> code;
                // create file from path (see extractSingleFile on how)
                try {
                  code = Files.readAllLines(f);
                } catch (IOException e) {
                  e.printStackTrace();
                  code = new ArrayList<>();
                }
                // For each line in file create a new task
                code.forEach(
                    l -> {
                      ExtractFeaturesTask task =
                          new ExtractFeaturesTask(s_CommandLineValues, parseJson(l));
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
    ExtractFeaturesTask ex = new ExtractFeaturesTask(s_CommandLineValues, parseJson(fileContent));
    ex.process();
  }

  /**
   * Parses the given json to extract the JavaDoc comment and code pair.
   *
   * @param json json string read from a file.
   */
  private String parseJson(String json) {
    JSONObject jo = new JSONObject(json);

    StringBuilder fullEntry = new StringBuilder();
    StringBuilder javaDoc = new StringBuilder().append("/**\n");
    String doc = (String) jo.get("docstring");

    String summary =
        doc.lines()
            .filter(
                l ->
                    !l.contains("=")
                        && !l.contains("-")
                        && !l.startsWith("<")
                        && !l.startsWith("(")
                        && !l.startsWith("@")
                        && l.split("[\\s+]").length >= 2)
            .findFirst()
            .orElse("");

    javaDoc.append("* " + summary + "\n");
    javaDoc.append("*/\n");

    fullEntry.append(javaDoc);
    fullEntry.append(jo.get("original_string") + "\n");

    return fullEntry.toString();
  }
}
