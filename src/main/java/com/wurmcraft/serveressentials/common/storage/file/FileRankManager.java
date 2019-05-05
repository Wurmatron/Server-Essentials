package com.wurmcraft.serveressentials.common.storage.file;

import com.wurmcraft.serveressentials.api.interfaces.IRankManager;
import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.reference.Storage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class FileRankManager implements IRankManager {

  private static NonBlockingHashMap<String, Rank> rankCache;

  public FileRankManager() {
    rankCache = new NonBlockingHashMap<>();
    loadRanks();
  }

  @Override
  public boolean register(Rank rank) {
    if (rankCache.containsKey(rank.getID())) {
      return false;
    }
    rankCache.put(rank.getID(), rank);
    return true;
  }

  @Override
  public boolean remove(Rank rank) {
    if (rankCache.containsKey(rank.getID())) {
      rankCache.remove(rank.getID());
      removeFromDataHelper(rank.getID());
      return true;
    }
    return false;
  }

  private void removeFromDataHelper(String id) {
    List<Rank> ranks = DataHelper.getData(Storage.RANK, new Rank());
    List<FileType> temp = new ArrayList<>();
    for (Rank rank : ranks) {
      if (!rank.getID().equalsIgnoreCase(id)) {
        temp.add(rank);
      }
    }
    DataHelper.addData(Storage.RANK, temp);
  }

  @Override
  public boolean exists(Rank rank) {
    return exists(rank.getID()) || exists(rank.getName());
  }

  @Override
  public boolean exists(String name) {
    return rankCache.keySet().stream().anyMatch(key -> key.equals(name));
  }

  @Override
  public Rank getRank(String rank) {
    return rankCache.getOrDefault(rank, null);
  }

  private void loadRanks() {
    File rankDir =
        new File(ConfigHandler.saveLocation + File.separator + Storage.RANK + File.separator);
    if (rankDir.exists() && rankDir.isDirectory()) {
      Arrays.stream(rankDir.listFiles())
          .map(file -> DataHelper.load(file, Storage.RANK, new Rank()))
          .forEach(this::register);
    } else {
      createDefaultRanks();
    }
  }

  private void createDefaultRanks() {
    File rankDir =
        new File(ConfigHandler.saveLocation + File.separator + Storage.RANK + File.separator);
    rankDir.mkdirs();
    createRank(
        new Rank(
            "Default",
            "&2[Default]",
            "",
            new String[0],
            new String[0])); // TODO Default Permissions
    createRank(new Rank("Admin", "&c[Admin]", "", new String[] {"Default"}, new String[] {"*"}));
  }

  private void createRank(Rank rank) {
    DataHelper.save(Storage.RANK, rank);
    DataHelper.load(Storage.RANK, rank);
  }
}
