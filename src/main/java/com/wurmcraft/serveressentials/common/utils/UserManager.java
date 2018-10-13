package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  private UserManager() {}

  // User Cache
  public static final NonBlockingHashMap<UUID, Rank> USER_RANKS = new NonBlockingHashMap<>();
  public static final NonBlockingHashMap<UUID, Object[]> PLAYER_DATA = new NonBlockingHashMap<>();
  // Rank Cache
  public static final NonBlockingHashMap<String, Rank> RANK_CACHE = new NonBlockingHashMap<>();
  public static final NonBlockingHashMap<String, AutoRank> AUTO_RANK_CACHE =
      new NonBlockingHashMap<>();
  // TeamCommand Cache
  public static final NonBlockingHashMap<String, Object[]> TEAM_CACHE = new NonBlockingHashMap<>();

  public static final NonBlockingHashMap<UUID, Long> JOIN_TIME = new NonBlockingHashMap<>();

  public static Rank getPlayerRank(UUID uuid) {
    return USER_RANKS.get(uuid);
  }

  public static Rank getRank(String name) {
    for (String key : RANK_CACHE.keySet()) {
      if (key.equalsIgnoreCase(name)) {
        return RANK_CACHE.get(key);
      }
    }
    return RANK_CACHE.size() == 0 ? null : getDefaultRank();
  }

  public static boolean isValidRank(String name) {
    for (String key : RANK_CACHE.keySet()) {
      if (key.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  public static Object[] getPlayerData(UUID uuid) {
    if (uuid != null) {
      return PLAYER_DATA.getOrDefault(uuid, new Object[0]);
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
    return TEAM_CACHE.getOrDefault(name, new Object[0]);
  }

  public static Rank[] getRanks() {
    return RANK_CACHE.values().toArray(new Rank[0]);
  }
}
