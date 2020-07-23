package com.wurmcraft.serveressentials.core.data;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.data.Warp;
import com.wurmcraft.serveressentials.core.api.eco.Currency;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import com.wurmcraft.serveressentials.core.api.module.config.LanguageConfig;
import com.wurmcraft.serveressentials.core.api.module.config.MatterLinkConfig;
import com.wurmcraft.serveressentials.core.api.module.config.RankConfig;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.utils.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class FileDataHandler extends BasicDataHandler {

  @Override
  public <T extends StoredDataType> NonBlockingHashMap<String, T> getDataFromKey(
      DataKey key, T type) {
    NonBlockingHashMap<String, T> loaded = super.getDataFromKey(key, type);
    if (loaded.size() == 0) {
      File dir = new File(SECore.SAVE_DIR + File.separator + key.getName());
      if (dir.exists()
          && dir.isDirectory()
          && dir.listFiles() != null
          && dir.listFiles().length > 0) {
        for (File file : dir.listFiles()) {
          try {
            T data = (T) FileUtils.getJson(file, type.getClass());
            loaded.put(data.getID(), data);
            loadedData.put(key, (NonBlockingHashMap<String, StoredDataType>) loaded);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return loaded;
  }

  @Override
  public StoredDataType getData(DataKey key, String dataID) throws NoSuchElementException {
    try {
      return super.getData(key, dataID);
    } catch (NoSuchElementException e) {
      File toLoad =
          new File(
              SECore.SAVE_DIR + File.separator + key.getName() + File.separator + dataID + ".json");
      try {
        List<String> lines = Files.readAllLines(toLoad.toPath());
        if (DataKey.RANK == key) {
          return GSON.fromJson(String.join("\n", lines), Rank.class);
        } else if (DataKey.PLAYER == key) {
          return GSON.fromJson(String.join("\n", lines), StoredPlayer.class);
        } else if (DataKey.AUTO_RANK == key) {
          return GSON.fromJson(String.join("\n", lines), AutoRank.class);
        } else if (DataKey.CURRENCY == key) {
          return GSON.fromJson(String.join("\n", lines), Currency.class);
        } else if (DataKey.LANGUAGE == key) {
          return GSON.fromJson(String.join("\n", lines), Language.class);
        } else if (DataKey.WARP == key) {
          return GSON.fromJson(String.join("\n", lines), Warp.class);
        } else if (DataKey.MODULE_CONFIG == key) {
          if (dataID.equals("Economy")) {
            return GSON.fromJson(String.join("\n", lines), EconomyConfig.class);
          } else if (dataID.equals("General")) {
            return GSON.fromJson(String.join("\n", lines), GeneralConfig.class);
          } else if (dataID.equals("Language")) {
            return GSON.fromJson(String.join("\n", lines), LanguageConfig.class);
          } else if (dataID.equals("MatterLink")) {
            return GSON.fromJson(String.join("\n", lines), MatterLinkConfig.class);
          } else if (dataID.equals("Rank")) {
            return GSON.fromJson(String.join("\n", lines), RankConfig.class);
          }
        }
      } catch (IOException f) {
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public void registerData(DataKey key, StoredDataType dataToStore) {
    if (dataToStore != null && !dataToStore.getID().isEmpty()) {
      super.registerData(key, dataToStore);
      File toSave =
          new File(
              SECore.SAVE_DIR
                  + File.separator
                  + key.getName()
                  + File.separator
                  + dataToStore.getID()
                  + ".json");
      if (!toSave.getParentFile().exists()) {
        if (!toSave.getParentFile().mkdirs()) {
          SECore.logger.warning(
              "Failed to create directory at " + toSave.getParentFile().toString());
        }
      }
      try {
        Files.write(toSave.toPath(), GSON.toJson(dataToStore).getBytes());
      } catch (IOException e) {
        SECore.logger.warning(
            "Failed to save '" + toSave.getAbsolutePath() + "' (FileDataHandler#registerData)");
      }
    }
  }

  @Override
  public void delData(DataKey key, String dataToRemove) throws NoSuchElementException {
    if (!dataToRemove.isEmpty()) {
      super.delData(key, dataToRemove);
      File toSave =
          new File(
              SECore.SAVE_DIR
                  + File.separator
                  + key.getName()
                  + File.separator
                  + dataToRemove
                  + ".json");
      if (!toSave.delete()) {
        SECore.logger.warning(
            "Failed to delete '" + toSave.getAbsolutePath() + "' (FileDataHandler#delData()");
      }
    }
  }
}
