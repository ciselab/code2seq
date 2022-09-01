package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class covers the data-format original to Code2Vec and Code2Seq.
 * Namely, Java-Small, Java-Med, Java-Large.
 * As this was the dataset provided by the authors,
 */
public class DefaultDataset implements Dataset {

  /**
   * Extracts the java files from the given directory. Altered to suite the default dataset of this
   * project.
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
          .filter(p -> p.toString().toLowerCase().endsWith(".java"))
          .forEach(
              f -> {
                String fileContent;
                try {
                  fileContent = Files.readString(f);
                } catch (IOException e) {
                  e.printStackTrace();
                  fileContent = Common.EmptyString;
                }
                ExtractFeaturesTask task =
                    new ExtractFeaturesTask(s_CommandLineValues, fileContent);
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

  /**
   * Extracts the code from the given file.
   *
   * @param s_CommandLineValues comman line arguments.
   * @param fileContent contents of the given file.
   */
  @Override
  public void extractFile(CommandLineValues s_CommandLineValues, String fileContent) {
    ExtractFeaturesTask ex = new ExtractFeaturesTask(s_CommandLineValues, fileContent);
    ex.process();
  }
}
