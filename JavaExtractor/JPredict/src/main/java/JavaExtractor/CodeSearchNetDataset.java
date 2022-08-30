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

public class CodeSearchNetDataset implements Dataset {

  @Override
  public void extractDir(CommandLineValues s_CommandLineValues) {
    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(s_CommandLineValues.NumThreads);
    LinkedList<CodeSearchNetTaskExtractor> tasks = new LinkedList<>();
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
                      CodeSearchNetTaskExtractor task =
                          new CodeSearchNetTaskExtractor(s_CommandLineValues, l);
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
}
