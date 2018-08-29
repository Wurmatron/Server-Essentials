package com.wurmcraft.serveressentials.common.security.events;

import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.security.SecurityModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LockDownEvents {

  @SubscribeEvent
  public void blockBreak(PlayerEvent.BreakSpeed e) {
    if (SecurityModule.lockdown) {
      e.setNewSpeed(0);
    }
  }

  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getPlayer());
    }
  }

  @SubscribeEvent
  public void onBlockBreak(BlockEvent.PlaceEvent e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getPlayer());
    }
  }

  @SubscribeEvent
  public void rightClick(PlayerInteractEvent.RightClickBlock e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getEntityPlayer());
    }
  }

  @SubscribeEvent
  public void leftClick(PlayerInteractEvent.LeftClickBlock e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getEntityPlayer());
    }
  }

  @SubscribeEvent
  public void rightClickAir(PlayerInteractEvent.RightClickEmpty e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getEntityPlayer());
    }
  }

  @SubscribeEvent
  public void leftClickAir(PlayerInteractEvent.LeftClickEmpty e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
      displayLockdown(e.getEntityPlayer());
    }
  }

  @SubscribeEvent
  public void onExplosion(ExplosionEvent.Start e) {
    if (SecurityModule.lockdown) {
      e.setCanceled(true);
    }
  }

  private static void displayLockdown(EntityPlayer player) {
    ChatHelper.sendMessage(
        player, LanguageModule.getLangfromUUID(player.getGameProfile().getId()).LOCKDOWN);
  }
}
