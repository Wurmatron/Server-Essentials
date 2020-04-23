package com.wurmcraft.serveressentials.core.data;

import static com.wurmcraft.serveressentials.core.SECore.SAVE_DIR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.FileUtils;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator.Rank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
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
          for (String r : loadedData.get(DataKey.RANK).keySet()) {
            SERegistry.delStoredData(DataKey.RANK, r);
          }
        }
        for (com.wurmcraft.serveressentials.core.api.json.rank.Rank r : ranks) {
          if (r != null) {
            data.put(r.getID(), (T) r);
            SERegistry.register(DataKey.RANK, r);
          }
        }
      }
    }
    return data;
  }

  @Override
  public StoredDataType getData(DataKey key, String dataID) throws NoSuchElementException {
    if (key == DataKey.PLAYER) {
      try {
        StoredDataType data = super.getData(key, dataID);
        if (data instanceof StoredPlayer) {
          return data;
        }
      } catch (NoSuchElementException e) {
        try {
          StoredPlayer playerData =
              FileUtils.getJson(
                  new File(
                      SAVE_DIR
                          + File.separator
                          + key.getName()
                          + File.separator
                          + dataID
                          + ".json"),
                  StoredPlayer.class);
          updatePlayerData(playerData);
          SECore.executors.schedule(
              () -> {
                StoredPlayer data = (StoredPlayer) SERegistry.getStoredData(key, dataID);
                data.global = RestRequestGenerator.User.getPlayer(playerData.uuid);
                updatePlayerData(data);
              },
              0,
              TimeUnit.SECONDS);
          return playerData;
        } catch (FileNotFoundException f) {
          throw new NoSuchElementException("Player Not Found in Database!");
        }
      }
    }
    return super.getData(key, dataID);
  }

  @Override
  public void registerData(DataKey key, StoredDataType dataToStore) {
    super.registerData(key, dataToStore);
    if (key == DataKey.RANK) {
      RestRequestGenerator.Rank.addRank(
          (com.wurmcraft.serveressentials.core.api.json.rank.Rank) dataToStore);
    } else if (key == DataKey.PLAYER) {
      RestRequestGenerator.User.addPlayer(((StoredPlayer) (dataToStore)).global);
    }
  }

  @Override
  public void delData(DataKey key, String dataToRemove) throws NoSuchElementException {
    super.delData(key, dataToRemove);
  }

  private void updatePlayerData(StoredPlayer player) {
    if (loadedData.containsKey(DataKey.PLAYER)) {
      loadedData.get(DataKey.PLAYER).put(player.uuid, player);
    } else {
      NonBlockingHashMap<String, StoredDataType> playerData = new NonBlockingHashMap<>();
      playerData.put(player.uuid, player);
      loadedData.put(DataKey.PLAYER, playerData);
    }
  }
}
