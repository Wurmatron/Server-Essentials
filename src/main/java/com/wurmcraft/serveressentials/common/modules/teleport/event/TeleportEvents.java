package com.wurmcraft.serveressentials.common.modules.teleport.event;

import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportEvents {

  @SubscribeEvent
  public void onPlayerDeath(LivingDeathEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      UserManager.setLastLocation(
          player, new LocationWrapper(player.getPosition(), player.dimension));
    }
  }
}
