package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  // User Cache
  public static final NonBlockingHashMap<UUID, Rank> userRanks = new NonBlockingHashMap<>();
  public static final NonBlockingHashMap<UUID, Object[]> playerData = new NonBlockingHashMap<>();
  // Rank Cache
  public static final NonBlockingHashMap<String, Rank> rankCache = new NonBlockingHashMap<>();
  public static final NonBlockingHashMap<String, AutoRank> autoRankCache =
      new NonBlockingHashMap<>();
  // TeamCommand Cache
  public static final NonBlockingHashMap<String, Object[]> teamCache = new NonBlockingHashMap<>();

  public static final NonBlockingHashMap<UUID, Long> joinTime = new NonBlockingHashMap<>();

  public static Rank getPlayerRank(UUID uuid) {
    return userRanks.get(uuid);
  }

  public static Rank getRank(String name) {
    for (String key : rankCache.keySet()) {
      if (key.equalsIgnoreCase(name)) {
        return rankCache.get(key);
      }
    }
    return rankCache.size() == 0 ? null : getDefaultRank();
  }

  public static boolean isValidRank(String name) {
    for (String key : rankCache.keySet()) {
      if (key.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  public static Object[] getPlayerData(UUID uuid) {
    if (uuid != null) {
      return playerData.getOrDefault(uuid, new Object[0]);
    }
    return new Object[0];
  }

  public static Object[] getPlayerData(EntityPlayer player) {
    return getPlayerData(player.getGameProfile().getId());
  }

  public static Rank getDefaultRank() {
    return getRank(ConfigHandler.defaultRank);
  }

  public static Object[] getTeam(String name) {
    return teamCache.getOrDefault(name, new Object[0]);
  }

  public static Rank[] getRanks() {
    return rankCache.values().toArray(new Rank[0]);
  }
}
