package com.wurmcraft.serveressentials.core.data;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator.Rank;
import java.util.NoSuchElementException;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class RestDataHandler extends FileDataHandler {

  @Override
  public <T extends StoredDataType> NonBlockingHashMap<String, T> getDataFromKey(
      DataKey key, T type) {
    NonBlockingHashMap<String, T> data = super.getDataFromKey(key, type);
    if (data.size() == 0) {
      if (key == DataKey.RANK) {
        com.wurmcraft.serveressentials.core.api.json.rank.Rank[] ranks = Rank.getAllRanks();
        if (loadedData.containsKey(key) && ranks.length > 0) {
          loadedData.get(DataKey.RANK).clear();
        }
        for (com.wurmcraft.serveressentials.core.api.json.rank.Rank r : ranks) {
          if (r != null) {
            data.put(r.getID(), (T) r);
            if (data.containsKey(key)) {
              loadedData.get(DataKey.RANK).put(r.getID(), r);
            } else {
              loadedData.put(DataKey.RANK, (NonBlockingHashMap<String, StoredDataType>) data);
            }
          }
        }
      }
    }
    return data;
  }

  @Override
  public StoredDataType getData(DataKey key, String dataID) throws NoSuchElementException {
    return super.getData(key, dataID);
  }

  @Override
  public void registerData(DataKey key, StoredDataType dataToStore) {
    loadedData.get(key).put(dataToStore.getID(), dataToStore);
    if (key == DataKey.RANK) {
      RestRequestGenerator.Rank.addRank(
          (com.wurmcraft.serveressentials.core.api.json.rank.Rank) dataToStore);
    } else {
      super.registerData(key, dataToStore);
    }
  }

  @Override
  public void delData(DataKey key, String dataToRemove) throws NoSuchElementException {
    if (key == DataKey.RANK) {
      RestRequestGenerator.Rank.deleteRank(
          (com.wurmcraft.serveressentials.core.api.json.rank.Rank)
              SERegistry.getStoredData(DataKey.RANK, dataToRemove));
    } else {
      super.delData(key, dataToRemove);
    }
  }
}
