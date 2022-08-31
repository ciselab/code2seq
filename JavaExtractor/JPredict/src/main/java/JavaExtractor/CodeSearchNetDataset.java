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
                          new ExtractFeaturesTask(s_CommandLineValues, collectJson(l));
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

  @Override
  public void extractFile(CommandLineValues s_CommandLineValues, String code) {
    ExtractFeaturesTask ex = new ExtractFeaturesTask(s_CommandLineValues, collectJson(code));
    ex.process();
  }

  private String collectJson(String json) {
    JSONObject jo = new JSONObject(json);

    StringBuilder full_entry = new StringBuilder();
    StringBuilder javaDoc = new StringBuilder().append("/**");
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
            .orElseThrow();

    javaDoc.append(summary + "\n");
    javaDoc.append("*/\n");

    full_entry.append(javaDoc);
    full_entry.append(jo.get("original_string") + "\n");

    return full_entry.toString();
  }
}
