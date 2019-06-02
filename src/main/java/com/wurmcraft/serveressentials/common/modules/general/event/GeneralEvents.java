package com.wurmcraft.serveressentials.common.modules.general.event;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class GeneralEvents {

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    if (ConfigHandler.motdOnJoin) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getCommandManager()
          .executeCommand(e.player, "/motd");
    }
  }
}
