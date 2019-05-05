package com.wurmcraft.serveressentials.common.storage.rest;

import static com.wurmcraft.serveressentials.common.storage.StorageUtils.triggerLogoutTimeout;

import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class RestWorldEvents {

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    GlobalRestUser user =
        RequestGenerator.User.getUser(e.player.getGameProfile().getId().toString());
    LocalRestUser local =
        DataHelper.load(Storage.LOCAL_USER, new LocalRestUser(e.player.getGameProfile().getId()));
    if (user != null) {
      if (local == null) { // New to this server but not the network
        local = new LocalRestUser(e.player.getGameProfile().getId());
        DataHelper.save(Storage.LOCAL_USER, local);
        local = DataHelper.load(Storage.LOCAL_USER, local);
      }
      UserManager.setUserData(e.player.getGameProfile().getId(), new Object[] {user, local});
      MinecraftForge.EVENT_BUS.post(new UserSyncEvent(user, user, Type.LOGIN));
      ServerEssentialsServer.LOGGER.debug(
          "Loaded User '"
              + e.player.getGameProfile().getId().toString()
              + "' ("
              + e.player.getDisplayName().getUnformattedText()
              + ")");
    } else { // New Player
      GlobalRestUser newUser =
          new GlobalRestUser(
              e.player.getGameProfile().getId().toString(), ConfigHandler.defaultRank);
      RequestGenerator.User.addNewPlayer(newUser);
      DataHelper.save(Storage.LOCAL_USER, local);
      local = DataHelper.load(Storage.LOCAL_USER, local);
      MinecraftForge.EVENT_BUS.post(new UserSyncEvent(newUser, user, Type.LOGIN));
      UserManager.setUserData(e.player.getGameProfile().getId(), new Object[] {newUser, local});
      ServerEssentialsServer.LOGGER.debug(
          "Loaded User '"
              + e.player.getGameProfile().getId().toString()
              + "' ("
              + e.player.getDisplayName().getUnformattedText()
              + ")");
    }
  }

  @SubscribeEvent
  public void onPlayerLogout(PlayerLoggedOutEvent e) {
    Object[] userData = UserManager.getUserData(e.player);
    DataHelper.save(Storage.LOCAL_USER, (LocalRestUser) userData[1]);
    triggerLogoutTimeout(e.player);
    RequestGenerator.User.overridePlayer((GlobalRestUser) userData[0], Type.LOGOUT);
  }
}
