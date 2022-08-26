package JavaExtractor;

import static org.junit.Assert.assertEquals;

import JavaExtractor.Common.StopWordsFilter;
import org.junit.jupiter.api.Test;

public class StopWordFilterTest {

  @Test
  public void removeStopWordsTest() {
    String filtered = StopWordsFilter.removeStopWords("a all be as if in hello world");
    assertEquals(filtered, "hello world");
  }
}
