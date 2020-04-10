package com.wurmcraft.serveressentials.core.utils;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.api.json.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileUtils {

  /**
   * Loads a json file and converts it into its represented data structure
   *
   * @param file file to load
   * @param type Class to hold the json data
   * @param <T> Type of the json file
   * @see com.google.gson.Gson
   */
  public static <T extends JsonParser> T getJson(File file, Class<T> type)
      throws FileNotFoundException {
    if (file.exists()) {
      return GSON.fromJson(new BufferedReader(new FileReader(file)), type);
    }
    throw new FileNotFoundException("File '" + file.getAbsolutePath() + "' not found!");
  }
}
