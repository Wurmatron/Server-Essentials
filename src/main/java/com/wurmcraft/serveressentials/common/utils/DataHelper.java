package com.wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.global.GlobalData;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import joptsimple.internal.Strings;

public class DataHelper {

  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  public static GlobalData globalSettings;
  private static final HashMap<Keys, HashMap<Object, Object>> tempData = new HashMap<>();
  private static final HashMap<Keys, List<DataType>> loadedData = new HashMap<>();

  private DataHelper() {}

  public static List<DataType> getData(Keys key) {
    return loadedData.get(key);
  }

  public static <T extends DataType> List<T> getData(Keys key, T type) {
    return (List<T>) loadedData.get(key);
  }

  public static void forceSave(File file, DataType data) {
    if (!file.exists()) {
      file.mkdirs();
    }
    File dataFile = new File(file + File.separator + data.getID() + ".json");
    try {
      dataFile.createNewFile();
      Files.write(Paths.get(dataFile.getAbsolutePath()), GSON.toJson(data).getBytes());
    } catch (IOException e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  public static void forceSave(Keys key, DataType data) {
    File file = new File(ConfigHandler.saveLocation + File.separator + key.name());
    if (!file.exists()) {
      file.mkdirs();
    }
    File dataFile = new File(file + File.separator + data.getID() + ".json");
    try {
      dataFile.createNewFile();
      Files.write(Paths.get(dataFile.getAbsolutePath()), GSON.toJson(data).getBytes());
    } catch (IOException e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  public static <T extends DataType> T load(File file, Keys key, T type) {
    if (file.exists()) {
      ServerEssentialsServer.LOGGER.debug("Loading " + file.getAbsolutePath());
      try {
        String fileData = Strings.join(Files.readAllLines(Paths.get(file.getAbsolutePath())), "");
        T data = GSON.fromJson(fileData, (Class<T>) type.getClass());
        if (loadedData.containsKey(key) && !exists(key, type)) {
          loadedData.get(key).add(data);
        } else {
          List<DataType> dataList = new ArrayList<>();
          dataList.add(data);
          loadedData.put(key, dataList);
        }
        return data;
      } catch (IOException e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
    }
    return null;
  }

  private static <T extends DataType> boolean exists(Keys key, T type) {
    for (DataType d : loadedData.get(key)) {
      if (d.getID().equalsIgnoreCase(type.getID())) {
        return true;
      }
    }
    return false;
  }

  public static <T extends DataType> T load(Keys key, T type) {
    return load(
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + key.name()
                + File.separator
                + type.getID()
                + ".json"),
        key,
        type);
  }

  public static boolean createIfNonExist(File file, DataType data) {
    if (!new File(file + File.separator + data.getID() + ".json").exists()) {
      forceSave(file, data);
      return true;
    }
    return false;
  }

  public static boolean createIfNonExist(Keys key, DataType data) {
    File file = new File(ConfigHandler.saveLocation + File.separator + key.name());
    File f = new File(file + File.separator + data.getID() + ".json");
    if (!f.exists()) {
      forceSave(file, data);
      load(f, key, data);
      return true;
    }
    load(f, key, data);
    return false;
  }

  public static DataType get(Keys key, String data) {
    List<DataType> keyData = getData(key);
    if (keyData != null && !keyData.isEmpty()) {
      for (DataType d : keyData) {
        if (d.getID().equals(data)) {
          return d;
        }
      }
    }
    return null;
  }

  public static void delete(Keys key, DataType data) {
    File file =
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + key.toString()
                + File.separator
                + data.getID()
                + ".json");
    try {
      Files.delete(file.toPath());
    } catch (IOException e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
    loadedData.get(key).remove(data);
  }

  public static void remove(Keys key, DataType data) {
    loadedData.get(key).remove(data);
  }

  public static <T> T getTemp(Keys key, java.util.UUID dataKey, T dataType) {
    if (tempData.get(key) != null && tempData.get(key).size() > 0) {
      HashMap<Object, T> data = (HashMap<Object, T>) tempData.get(key);
      return data.getOrDefault(dataKey, null);
    }
    return null;
  }

  public static <T> Map<T, Object> getTemp(Keys key, T keyType) {
    return (HashMap<T, Object>) tempData.getOrDefault(key, new HashMap<>());
  }

  public static void addTemp(
      Keys key, java.util.UUID dataKey, java.util.UUID data, boolean remove) {
    HashMap<Object, Object> temp;
    if (tempData.size() > 0) {
      temp = tempData.get(key);
    } else {
      temp = new HashMap<>();
    }
    if (!remove) {
      temp.put(dataKey, data);
    } else {
      temp.remove(dataKey);
    }
    tempData.put(key, temp);
  }
}
