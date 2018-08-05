package com.wurmcraft.serveressentials.common.security.events;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class SecurityEvents {

  @SubscribeEvent
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    if (ConfigHandler.autoOP && SecurityModule.isTrustedMember(e.player) && !FMLCommonHandler
        .instance()
        .getMinecraftServerInstance().getPlayerList().canSendCommands(e.player.getGameProfile())) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getCommandManager()
          .executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),
              "op " + UsernameCache.getLastKnownUsername(e.player.getGameProfile().getId()));
    }
  }
}
