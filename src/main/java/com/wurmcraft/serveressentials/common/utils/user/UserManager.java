package com.wurmcraft.serveressentials.common.utils.user;

import static com.wurmcraft.serveressentials.common.storage.rest.RestWorldEvents.rankChangeCache;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
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

  public static void setUserData(UUID uuid, Object[] data) {
    setUserData(uuid.toString(), data);
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

  public static void deleteUser(UUID uuid) {
    loadedUserData.remove(uuid);
  }

  public static Rank getDefaultRank() {
    return ServerEssentialsAPI.rankManager.getRank(ConfigHandler.defaultRank);
  }

  public static double getServerCurrency(UUID uuid) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) getUserData(uuid)[0];
      return user.getBank().getCurrency(ConfigHandler.serverCurrency);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) getUserData(uuid)[0];
      return user.getMoney();
    }
    return -1;
  }

  public static void rankupUser(EntityPlayer player, Rank rank) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      rankChangeCache.put(player.getGameProfile().getId().toString(), rank.getID());
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(player)[0];
      user.setRank(rank.getID());
      RequestGenerator.User.overridePlayer(user, Type.STANDARD);
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(player)[0];
      user.setRank(rank);
      DataHelper.save(Storage.USER, user);
    }
    // TODO Announce User Rank-Up
  }
}
