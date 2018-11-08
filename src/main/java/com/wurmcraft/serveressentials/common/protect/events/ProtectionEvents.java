package com.wurmcraft.serveressentials.common.protect.events;

import com.wurmcraft.serveressentials.api.protection.Town;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.protect.ProtectionModule;
import com.wurmcraft.serveressentials.common.utils.CommandUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.BlockEvent.PortalSpawnEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProtectionEvents {

  @SubscribeEvent
  public void blockBreak(BreakEvent e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID().equalsIgnoreCase(e.getPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getPlayer(),
            LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId()).CLAIM_BREAK);
      }
    }
  }

  @SubscribeEvent
  public void blockPlace(PlaceEvent e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID().equalsIgnoreCase(e.getPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getPlayer(),
            LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId()).CLAIM_PLACE);
      }
    }
  }

  @SubscribeEvent
  public void rightAir(RightClickEmpty e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void rightItem(RightClickItem e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void rightBlock(RightClickBlock e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void leftAir(LeftClickEmpty e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void leftItem(RightClickItem e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void leftBlock(LeftClickBlock e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(e.getEntityPlayer().getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", e.getEntityPlayer())) {
        e.setCanceled(true);
        ChatHelper.sendMessage(e.getEntityPlayer(), LanguageModule
            .getLangfromUUID(e.getEntityPlayer().getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void explosion(ExplosionEvent e) {
    for (BlockPos pos : e.getExplosion().getAffectedBlockPositions()) {
      if (ProtectionModule.isAreaClaimed(pos)) {
        e.setCanceled(true);
        return;
      }
    }
  }

  @SubscribeEvent
  public void cropTrample(FarmlandTrampleEvent e) {
    if (e.getEntity() instanceof EntityPlayer && ProtectionModule.isAreaClaimed(e.getPos())) {
      EntityPlayer player = (EntityPlayer) e.getEntity();
      Town town = ProtectionModule.getTownForPos(e.getPos());
      if (!town.getOwnerID()
          .equalsIgnoreCase(player.getGameProfile().getId().toString())
          || CommandUtils.hasPerm("protection.bypass", player)) {
        e.setCanceled(true);
        ChatHelper.sendMessage(player, LanguageModule
            .getLangfromUUID(player.getGameProfile().getId()).CLAIM_INTERACT);
      }
    }
  }

  @SubscribeEvent
  public void portalSpawn(PortalSpawnEvent e) {
    if (ProtectionModule.isAreaClaimed(e.getPos())) {
      e.setCanceled(true);
    }
  }


}
