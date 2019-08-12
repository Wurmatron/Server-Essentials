package com.wurmcraft.serveressentials.common.storage.file;

import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;
import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.instance;

import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Strings;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class DataHelper {

  private static final NonBlockingHashMap<String, List<FileType>> loadedData =
      new NonBlockingHashMap<>();

  public static List<FileType> getData(String key) {
    return loadedData.get(key);
  }

  public static <T extends FileType> List<T> getData(String key, T type) {
    return (List<T>) getData(key);
  }

  public static void addData(String key, List<FileType> data) {
    loadedData.put(key.toUpperCase(), data);
  }

  public static void addData(String key, FileType data) {
    if (loadedData.containsKey(key.toUpperCase())) {
      List<FileType> savedData = loadedData.get(key.toUpperCase());
      if (!savedData.contains(data)) {
        savedData.add(data);
      }
      addData(key, savedData);
    } else {
      List<FileType> savedData = new ArrayList<>();
      savedData.add(data);
      addData(key, savedData);
    }
  }

  public static boolean save(File file, FileType data) {
    if (file != null && data != null && data.getID() != null && !data.getID().isEmpty()) {
      if (!file.exists()) {
        file.mkdirs();
      }
      File dataFile = new File(file + File.separator + data.getID() + ".json");
      try {
        boolean created = dataFile.createNewFile();
        Files.write(Paths.get(dataFile.getAbsolutePath()), instance.GSON.toJson(data).getBytes());
        return created || file.exists();
      } catch (IOException e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
    }
    return false;
  }

  public static boolean save(String key, FileType data) {
    return save(new File(saveLocation + File.separator + key), data);
  }

  public static <T extends FileType> T load(File file, String key, T type) {
    if (file.exists()) {
      ServerEssentialsServer.LOGGER.debug("Loading " + file.getAbsolutePath());
      try {
        String fileData = Strings.join(Files.readAllLines(Paths.get(file.getAbsolutePath())), "");
        T data = instance.GSON.fromJson(fileData, (Class<T>) type.getClass());
        if (loadedData.containsKey(key) && !exists(key, type)) {
          loadedData.get(key).add(data);
        } else {
          List<FileType> dataList = new ArrayList<>();
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

  public static <T extends FileType> T load(String key, T type) {
    return load(
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + key
                + File.separator
                + type.getID()
                + ".json"),
        key,
        type);
  }

  public static <T extends FileType> T[] load(String key, T[] type, T a) {
    List<T> data = new ArrayList<>();
    File keyDir = new File(saveLocation + File.separator + key);
    if (!keyDir.exists()) {
      keyDir.mkdirs();
    }
    try {
      for (File t : keyDir.listFiles()) {
        if (t.isFile()) {
          data.add((T) load(t, key, a));
        }
      }
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER.error(e.getMessage());
    }
    return data.toArray((T[]) Array.newInstance(type.getClass(), 0));
  }

  private static <T extends FileType> boolean exists(String key, T type) {
    return loadedData.get(key).stream().anyMatch(d -> d.getID().equalsIgnoreCase(type.getID()));
  }

  public static FileType get(String key, String id) {
    List<FileType> keyData = getData(key);
    if (keyData != null && !keyData.isEmpty()) {
      for (FileType d : keyData) {
        if (d.getID().equalsIgnoreCase(id)) {
          return d;
        }
      }
      return null;
    }
    return null;
  }

  public static <T extends FileType> T get(String key, String id, T type) {
    return (T) get(key, id);
  }

  public static void remove(String key, FileType data) {
    loadedData.get(key.toUpperCase()).remove(data);
  }

  public static void delete(String key, FileType data) {
    File file =
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + key.toUpperCase()
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
}
