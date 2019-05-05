package com.wurmcraft.serveressentials.common.storage;

import com.wurmcraft.serveressentials.api.storage.Storage;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.file.FileStorage;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.storage.rest.RestStorage;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class StorageUtils {

  public static String getActiveStorageType() {
    return isRest() ? "Rest" : "File";
  }

  public static Storage setupStorage() {
    if (getActiveStorageType().equalsIgnoreCase("Rest")) {
      return new RestStorage();
    } else if (getActiveStorageType().equalsIgnoreCase("File")) {
      return new FileStorage();
    }
    return null;
  }

  private static boolean isRest() {
    return ConfigHandler.restAuth.length() > 0
        && ConfigHandler.restURL.length() > 0
        && RequestGenerator.Status.getValidation() != null;
  }

  public static void triggerLogoutTimeout(EntityPlayer player) {
    ServerEssentialsServer.instance.executors.scheduleAtFixedRate(
        () -> {
          if (!FMLCommonHandler.instance()
              .getMinecraftServerInstance()
              .getPlayerList()
              .getPlayers()
              .contains(player)) {
            unloadUser(player.getGameProfile().getId());
          }
        },
        ConfigHandler.userDataSyncPeriod,
        ConfigHandler.userDataSyncPeriod,
        TimeUnit.SECONDS);
  }

  private static void unloadUser(UUID uuid) {
    UserManager.deleteUser(uuid);
    if (getActiveStorageType().equalsIgnoreCase("Rest")) {

      DataHelper.remove(
          com.wurmcraft.serveressentials.common.reference.Storage.LOCAL_USER,
          DataHelper.get(
              com.wurmcraft.serveressentials.common.reference.Storage.LOCAL_USER, uuid.toString()));
      ServerEssentialsServer.LOGGER.debug(
          "Unloaded User '" + uuid + "' (" + UsernameCache.getLastKnownUsername(uuid) + ")");
    } else {
      DataHelper.remove(
          com.wurmcraft.serveressentials.common.reference.Storage.USER,
          DataHelper.get(
              com.wurmcraft.serveressentials.common.reference.Storage.USER, uuid.toString()));
    }
  }
}
