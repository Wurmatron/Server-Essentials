package com.wurmcraft.serveressentials.core.data;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class BasicDataHandler extends SERegistry implements IDataHandler {

  public BasicDataHandler() {
    if (loadedData == null) {
      loadedData = new NonBlockingHashMap<>();
      SECore.logger.warning(
          "SERegistry<loadedData> has been tampered with! You should never see this message!");
    }
  }

  /**
   * Returns all the values that have been stored under this key
   *
   * @param key Key the value was stored under
   * @return map of all the values related to the DataKey
   */
  @Override
  public NonBlockingHashMap<String, StoredDataType> getDataFromKey(DataKey key) {
    return loadedData.getOrDefault(key, new NonBlockingHashMap<>());
  }

  /**
   * Get specific data from the database when you know its DataKey and its ID
   *
   * @param key Key the value was stored under
   * @param dataID ID of the data that was stored
   * @return instance of the data that was stored
   */
  @Override
  public StoredDataType getData(DataKey key, String dataID)
      throws NoSuchElementException {
    if (loadedData.containsKey(key) && loadedData.get(key).containsKey(dataID)) {
      StoredDataType data = loadedData.get(key).get(dataID);
      if (data != null) {
        return data;
      } else {
        SECore.logger.fine(
            "Null value found in SERegistry, removing '" + dataID + "' from '" + key
                .toString() + "'!");
        loadedData.get(key).remove(dataID);
        throw new NoSuchElementException(
            "Value was null, thus it must be empty! (" + key.toString() + ":" + dataID
                + ")");
      }
    }
    throw new NoSuchElementException(
        key.toString() + " had nothing with the ID '" + dataID + "'");
  }

  /**
   * Adds a given instance to a database based on its ID
   *
   * @param key Key the value was stored under
   * @param dataToStore instance of the data you wish to get stored
   */
  @Override
  public void registerData(DataKey key, StoredDataType dataToStore) {
    if (dataToStore != null && !dataToStore.getID().isEmpty()) {
      if (loadedData.containsKey(key)) {
        NonBlockingHashMap<String, StoredDataType> database = loadedData.get(key);
        database.put(dataToStore.getID(), dataToStore);
      } else {
        NonBlockingHashMap<String, StoredDataType> newRegistryStorage = new NonBlockingHashMap<>();
        newRegistryStorage.put(dataToStore.getID(), dataToStore);
        loadedData.put(key, newRegistryStorage);
        SECore.logger
            .fine("Creating new entry '" + key.toString() + "' in the Registry!");
      }
    } else {
      SECore.logger.info(
          "null entry was attempted to add to the Registry under '" + key.toString()
              + "'");
    }
  }

  /**
   * Removes a instance from storage
   *
   * @param key Key the value was stored under
   * @param dataToRemove instance with the same ID was the one you wish to remove
   */
  @Override
  public void delData(DataKey key, String dataToRemove) throws NoSuchElementException {
    if (loadedData.containsKey(key) && loadedData.get(key).containsKey(dataToRemove)) {
      loadedData.get(key).remove(dataToRemove);
      SECore.logger.fine(dataToRemove + " has been removed from " + key.toString());
    } else {
      throw new NoSuchElementException(dataToRemove + " was not in " + key + "!");
    }
  }
}
