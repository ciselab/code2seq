package JavaExtractor;

import JavaExtractor.Common.StopWordsFilter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StopWordFilterTest {

  @Test
  public void removeStopWordsTest() {
    String filtered = StopWordsFilter.removeStopWords("a all be as if in hello world");
    assertEquals(filtered, "hello world");
  }
}
