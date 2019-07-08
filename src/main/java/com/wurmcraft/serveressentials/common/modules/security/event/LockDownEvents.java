package com.wurmcraft.serveressentials.common.modules.security.event;

import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.modules.security.SecurityModule;
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
    ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.SECURITY_LOCKDOWN);
  }
}
