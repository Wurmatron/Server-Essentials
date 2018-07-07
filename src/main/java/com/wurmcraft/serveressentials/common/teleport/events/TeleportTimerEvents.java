package com.wurmcraft.serveressentials.common.teleport.events;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TeleportTimerEvents {

  @SubscribeEvent
  public void onTickEvent(WorldTickEvent.WorldTickEvent e) {
    if (TeleportationModule.activeRequests.size() > 0 && e.world.getWorldTime() % 20 == 0) {
      for (long time : TeleportationModule.activeRequests.keySet()) {
        if ((time + (ConfigHandler.tpaTimeout * 1000)) <= System.currentTimeMillis()) {
          TeleportationModule.activeRequests.remove(time);
        }
      }
    }
  }
}
