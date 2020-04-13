package com.wurmcraft.serveressentials.core.data;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.utils.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class FileDataHandler extends BasicDataHandler {

  @Override
  public <T extends StoredDataType> NonBlockingHashMap<String, T> getDataFromKey(
      DataKey key, T type) {
    NonBlockingHashMap<String, T> loaded = super.getDataFromKey(key, type);
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
    return loaded;
  }

  @Override
  public StoredDataType getData(DataKey key, String dataID) throws NoSuchElementException {
    return super.getData(key, dataID);
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
