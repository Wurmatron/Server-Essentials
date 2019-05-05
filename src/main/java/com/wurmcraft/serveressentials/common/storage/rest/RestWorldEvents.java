package com.wurmcraft.serveressentials.common.storage.rest;

import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class RestWorldEvents {

  @SubscribeEvent
  public static void onPlayerJoin(PlayerLoggedInEvent e) {
    GlobalRestUser user =
        RequestGenerator.User.getUser(e.player.getGameProfile().getId().toString());
    if (user != null) {

    } else { // New Player
      GlobalRestUser newUser =
          new GlobalRestUser(
              e.player.getGameProfile().getId().toString(), ConfigHandler.defaultRank);
      RequestGenerator.User.addNewPlayer(newUser);
    }
  }
}
