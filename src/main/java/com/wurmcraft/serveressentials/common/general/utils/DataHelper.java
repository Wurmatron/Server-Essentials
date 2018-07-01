package com.wurmcraft.serveressentials.common.general.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.global.GlobalData;
import com.wurmcraft.serveressentials.api.json.user.IDataType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Keys;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import joptsimple.internal.Strings;

public class DataHelper {

  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  public static GlobalData globalSettings;
  public static HashMap<Keys, HashMap<Object, Object>> tempData = new HashMap<>();
  private static HashMap<Keys, List<IDataType>> loadedData = new HashMap<>();

  public static List<IDataType> getData(Keys key) {
    return loadedData.get(key);
  }

  public static <T extends IDataType> List<T> getData(Keys key, T type) {
    return (List<T>) loadedData.get(key);
  }

  public static void forceSave(File file, IDataType data) {
    if (!file.exists()) {
      file.mkdirs();
    }
    File dataFile = new File(file + File.separator + data.getID() + ".json");
    try {
      dataFile.createNewFile();
      Files.write(Paths.get(dataFile.getAbsolutePath()), GSON.toJson(data).getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void forceSave(Keys key, IDataType data) {
    File file = new File(ConfigHandler.saveLocation + File.separator + key.name());
    if (!file.exists()) {
      file.mkdir();
    }
    File dataFile = new File(file + File.separator + data.getID() + ".json");
    try {
      dataFile.createNewFile();
      Files.write(Paths.get(dataFile.getAbsolutePath()), GSON.toJson(data).getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static <T extends IDataType> T load(File file, Keys key, T type) {
    if (file.exists()) {
      ServerEssentialsServer.logger.debug("Loading " + file.getAbsolutePath());
      try {
        String fileData = Strings.join(Files.readAllLines(Paths.get(file.getAbsolutePath())), "");
        T data = GSON.fromJson(fileData, (Class<T>) type.getClass());
        if (loadedData.containsKey(key)) {
          loadedData.get(key).add(data);
        } else {
          List<IDataType> dataList = new ArrayList<>();
          dataList.add(data);
          loadedData.put(key, dataList);
        }
        return data;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static <T extends IDataType> T load(Keys key, T type) {
    return load(new File(
        ConfigHandler.saveLocation + File.separator + key.name() + File.separator + type.getID()
            + ".json"), key, type);
  }

  public static boolean createIfNonExist(File file, IDataType data) {
    if (!new File(file + File.separator + data.getID() + ".json").exists()) {
      forceSave(file, data);
      return true;
    }
    return false;
  }

  public static boolean createIfNonExist(Keys key, IDataType data) {
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

  public static IDataType get(Keys key, String data) {
    List<IDataType> keyData = getData(key);
    if (keyData.size() > 0) {
      for (IDataType d : keyData) {
        if (d.getID().equals(data)) {
          return d;
        }
      }
    }
    return null;
  }

  public static void delete(Keys key, IDataType data) {
    File file = new File(
        ConfigHandler.saveLocation + File.separator + key.toString() + File.separator + data.getID()
            + ".json");
    if (file.exists()) {
      file.delete();
    }
    loadedData.get(key).remove(data);
  }

  public static void remove(Keys key, IDataType data) {
    loadedData.get(key).remove(data);
  }

  public static <T> T getTemp(Keys key, Object dataKey, T dataType) {
    if (tempData.get(key) != null && tempData.get(key).size() > 0) {
      HashMap<Object, T> data = (HashMap<Object, T>) tempData.get(key);
      return data.getOrDefault(dataKey, null);
    }
    return null;
  }

  public static <T> HashMap<T, Object> getTemp(Keys key, T keyType) {
    return (HashMap<T, Object>) tempData.getOrDefault(key, new HashMap<>());
  }

  public static <T> void addTemp(Keys key, Object dataKey, Object data, boolean remove) {
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
