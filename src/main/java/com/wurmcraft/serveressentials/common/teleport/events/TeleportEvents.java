package com.wurmcraft.serveressentials.common.teleport.events;

import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class TeleportEvents {

  public static NonBlockingHashMap<EntityPlayer, BlockPos> playerPositionTracker =
      new NonBlockingHashMap<>();

  @SubscribeEvent
  public void onTickEvent(WorldTickEvent.WorldTickEvent e) {
    if (TeleportationModule.activeRequests.size() > 0 && e.world.getWorldTime() % 20 == 0) {
      for (long time : TeleportationModule.activeRequests.keySet()) {
        if ((time + (ConfigHandler.tpaTimeout * 1000)) <= System.currentTimeMillis()) {
          TeleportationModule.activeRequests.remove(time);
        }
      }
    }
    if (playerPositionTracker.size() > 0) {
      for (EntityPlayer player : playerPositionTracker.keySet()) {
        if (!playerPositionTracker.get(player).equals(player.getPosition())) {
          playerPositionTracker.remove(player);
          TeleportationModule.cancelRequest(player);
          ChatHelper.sendMessage(player, LanguageModule.getLangFromPlayer(player).TP_DELAY_FAILED);
        }
      }
    }
    if (TeleportationModule.teleportDelay.size() > 0) {
      for (Long time : TeleportationModule.teleportDelay.keySet()) {
        if (TeleportUtils.handleDelayedTeleport(TeleportationModule.teleportDelay.get(time))) {
          playerPositionTracker.remove(TeleportationModule.teleportDelay.get(time).A);
          TeleportationModule.teleportDelay.remove(time);
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
