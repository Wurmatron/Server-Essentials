package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.UUID;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  // User Cache
  public static NonBlockingHashMap<UUID, Rank> userRanks = new NonBlockingHashMap<>();
  public static NonBlockingHashMap<UUID, Object[]> playerData = new NonBlockingHashMap<>();
  // Rank Cache
  public static NonBlockingHashMap<String, Rank> rankCache = new NonBlockingHashMap<>();

  public static Rank getPlayerRank(UUID uuid) {
    return userRanks.get(uuid);
  }

  public static Rank getRank(String name) {
    return rankCache.getOrDefault(name, getDefaultRank());
  }

  public static Object[] getPlayerData(UUID uuid) {
    return null;
  }

  public static Rank getDefaultRank() {
    return getRank(ConfigHandler.defaultRank);
  }
}
