package com.wurmcraft.serveressentials.common.claim.events;

import com.wurmcraft.serveressentials.api.json.claim.Claim;
import com.wurmcraft.serveressentials.common.claim.ChunkManager;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClaimEvent {

  public static String getOwner(Claim claim) {
    if (claim != null && claim.getTeam() != null) {
      return claim.getTeam().getName();
    } else if (claim != null && claim.getOwner() != null) {
      return UsernameCache.getLastKnownUsername(claim.getOwner());
    }
    return "Unknown";
  }

  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null && !ChunkManager.canDestroy(claim, e.getPlayer().getGameProfile().getId())) {
      e.setCanceled(true);
      e.getPlayer().world.notifyBlockUpdate(e.getPos(), e.getState(), e.getState(), 2);
      e.getPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                      .CLAIM_BREAK
                      .replaceAll("%PLAYER%", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onBlockPlace(BlockEvent.PlaceEvent e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null && !ChunkManager.canDestroy(claim, e.getPlayer().getGameProfile().getId())) {
      e.setCanceled(true);
      e.getPlayer().world.notifyBlockUpdate(e.getPos(), e.getState(), e.getState(), 2);
      e.getPlayer().inventory.markDirty();
      e.getPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                      .CLAIM_PLACE
                      .replaceAll("%PLAYER%", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null
        && !ChunkManager.canDestroy(claim, e.getEntityPlayer().getGameProfile().getId())
        && !ChunkManager.isItemSafe(e.getItemStack())) {
      e.setCanceled(true);
      e.getEntityPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
                      .CLAIM_INTERACT
                      .replaceAll("#", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null
        && !ChunkManager.canDestroy(claim, e.getEntityPlayer().getGameProfile().getId())
        && !ChunkManager.isItemSafe(e.getItemStack())) {
      e.setCanceled(true);
      e.getEntityPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
                      .CLAIM_INTERACT
                      .replaceAll("%PLAYER%", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null
        && !ChunkManager.canDestroy(claim, e.getEntityPlayer().getGameProfile().getId())
        && !ChunkManager.isItemSafe(e.getItemStack())) {
      e.setCanceled(true);
      e.getEntityPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
                      .CLAIM_INTERACT
                      .replaceAll("%PLAYER%", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null
        && !ChunkManager.canDestroy(claim, e.getEntityPlayer().getGameProfile().getId())
        && !ChunkManager.isItemSafe(e.getItemStack())) {
      e.setCanceled(true);
      e.getEntityPlayer()
          .sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
                      .CLAIM_INTERACT
                      .replaceAll("%PLAYER%", getOwner(claim))));
    }
  }

  @SubscribeEvent
  public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e) {
    Claim claim = ChunkManager.getClaim(e.getPos());
    if (claim != null
        && !ChunkManager.canDestroy(claim, e.getEntityPlayer().getGameProfile().getId())) {
      if (!ChunkManager.isItemSafe(e.getItemStack())) {
        e.setCanceled(true);
        e.getEntityPlayer()
            .sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
                        .CLAIM_INTERACT
                        .replaceAll("%PLAYER%", getOwner(claim))));
      }
    }
  }

  @SubscribeEvent
  public void onExplosion(ExplosionEvent.Start e) {
    if (e.getExplosion().getAffectedBlockPositions().size() > 0) {
      for (BlockPos affectedArea : e.getExplosion().getAffectedBlockPositions()) {
        Claim claim = ChunkManager.getClaim(affectedArea);
        if (claim != null) {
          if (e.getExplosion().getExplosivePlacedBy() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getExplosion().getExplosivePlacedBy();
            if (!ChunkManager.canDestroy(claim, player.getGameProfile().getId())) {
              player.sendMessage(
                  new TextComponentString(
                      LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                          .CLAIM_EXPLOSION
                          .replaceAll("#", getOwner(claim))));
              e.setCanceled(true);
            }
          } else {
            e.setCanceled(true);
          }
        }
      }
    } else {
      Vec3d location = e.getExplosion().getPosition();
      Claim claim = ChunkManager.getClaim(new BlockPos(location.x, location.y, location.z));
      if (claim != null) {
        if (e.getExplosion().getExplosivePlacedBy() instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) e.getExplosion().getExplosivePlacedBy();
          if (!ChunkManager.canDestroy(claim, player.getGameProfile().getId())) {
            player.sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                        .CLAIM_EXPLOSION
                        .replaceAll("#", getOwner(claim))));
            e.setCanceled(true);
          }
        } else {
          e.setCanceled(true);
        }
      }
    }
  }
}
