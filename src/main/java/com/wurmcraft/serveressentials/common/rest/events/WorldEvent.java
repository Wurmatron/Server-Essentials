package com.wurmcraft.serveressentials.common.rest.events;

import com.wurmcraft.serveressentials.common.rest.RestModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class WorldEvent {

  @SubscribeEvent
  public void onJoinEvent(PlayerEvent.PlayerLoggedInEvent e) {
    RestModule.syncPlayer(e.player.getGameProfile().getId());
  }

  @SubscribeEvent
  public void onLeaveEvent(PlayerEvent.PlayerLoggedOutEvent e) {
    RestModule.deletePlayerData(e.player.getGameProfile().getId());
  }
}
