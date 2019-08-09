package com.wurmcraft.serveressentials.common.storage.file;

import static com.wurmcraft.serveressentials.common.storage.StorageUtils.triggerLogoutTimeout;

import com.wurmcraft.serveressentials.api.user.event.NewPlayerJoin;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class FileWorldEvents {

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    FileUser user =
        DataHelper.load(
            Storage.USER,
            new FileUser(e.player.getGameProfile().getId(), UserManager.getDefaultRank()));
    if (user == null) { // New User
      user = new FileUser(e.player.getGameProfile().getId(), UserManager.getDefaultRank());
      DataHelper.save(Storage.USER, user);
      DataHelper.load(Storage.USER, user);
      MinecraftForge.EVENT_BUS.post(
          new NewPlayerJoin(e.player, false)); // Network player is impossible
    }
  }

  @SubscribeEvent
  public void onPlayerLogout(PlayerLoggedOutEvent e) {
    Object[] userData = UserManager.getUserData(e.player);
    DataHelper.save(Storage.USER, (FileUser) userData[0]);
    triggerLogoutTimeout(e.player);
  }
}
