package com.wurmcraft.serveressentials.common.teleport.events;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class TeleportEvents {

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

  @SubscribeEvent
  public void onDeath(LivingDeathEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      TeleportUtils.setLastLocation(player, player.getPosition());
    }
  }
}
