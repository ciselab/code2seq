package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultDataset implements Dataset {

  @Override
  public void extractDir(CommandLineValues s_CommandLineValues) {
    ThreadPoolExecutor executor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(s_CommandLineValues.NumThreads);
    LinkedList<DefaultTaskExtractor> tasks = new LinkedList<>();
    try {
      Files.walk(Paths.get(s_CommandLineValues.Dir))
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().toLowerCase().endsWith(".java"))
          .forEach(
              f -> {
                DefaultTaskExtractor task = new DefaultTaskExtractor(s_CommandLineValues, f);
                tasks.add(task);
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
            e.printStackTrace();
          }
        });
  }
}
