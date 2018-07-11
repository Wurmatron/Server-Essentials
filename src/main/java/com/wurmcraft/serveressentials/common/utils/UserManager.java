package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  // User Cache
  public static NonBlockingHashMap<UUID, Rank> userRanks = new NonBlockingHashMap<>();
  public static NonBlockingHashMap<UUID, Object[]> playerData = new NonBlockingHashMap<>();
  // Rank Cache
  public static NonBlockingHashMap<String, Rank> rankCache = new NonBlockingHashMap<>();
  public static NonBlockingHashMap<String, AutoRank> autoRankCache = new NonBlockingHashMap<>();
  // TeamCommand Cache
  public static NonBlockingHashMap<String, Object[]> teamCache = new NonBlockingHashMap<>();

  public static NonBlockingHashMap<UUID, Long> joinTime = new NonBlockingHashMap<>();

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
      ServerEssentialsServer.logger.error(
          "KLFGLAGHJLGHAJKLS" + playerData.getOrDefault(uuid, new Object[0]).length);
      return playerData.getOrDefault(uuid, new Object[0]);
    }
    System.out.println("KLFGLAGHJLGHAJKLS EMPTY ARRAY!");
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
}
