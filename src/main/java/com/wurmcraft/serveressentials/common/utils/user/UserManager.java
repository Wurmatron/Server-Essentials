package com.wurmcraft.serveressentials.common.utils.user;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class UserManager {

  private static NonBlockingHashMap<String, Object[]> loadedUserData = new NonBlockingHashMap<>();

  public static Object[] getUserData(String uuid) {
    return loadedUserData.getOrDefault(uuid, new Object[0]);
  }

  public static Object[] getUserData(UUID uuid) {
    return getUserData(uuid.toString());
  }

  public static Object[] getUserData(EntityPlayer player) {
    return getUserData(player.getGameProfile().getId().toString());
  }

  public static boolean isUserLoaded(String uuid) {
    return getUserData(uuid) != null && getUserData(uuid).length > 0;
  }

  public static void setUserData(String uuid, Object[] data) {
    loadedUserData.put(uuid, data);
  }

  public static Rank getUserRank(String uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      return getRestRank(uuid);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return getFileRank(uuid);
    }
    return null;
  }

  private static Rank getFileRank(String uuid) {
    return null;
    //    return getUserData(uuid)[0];
  }

  private static Rank getRestRank(String uuid) {
    return ServerEssentialsAPI.rankManager.getRank(((GlobalRestUser) getUserData(uuid)[0]).rank);
  }
}
