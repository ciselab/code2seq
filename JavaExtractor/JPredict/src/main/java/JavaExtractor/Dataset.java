package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;

interface Dataset {

  public void extractDir(CommandLineValues s_CommandLineValues);

  public void extractFile(CommandLineValues s_CommandLineValues, String filePath);
}
