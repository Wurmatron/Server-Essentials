package com.wurmcraft.serveressentials.common.claim.events;

import com.wurmcraft.serveressentials.api.json.claim.Claim;
import com.wurmcraft.serveressentials.common.claim.ChunkManager;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

@Deprecated
public class ClaimEvent {

  private static HashMap<EntityPlayer, ChunkPos> playerChunkLoc = new HashMap<>();

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

  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent e) {
    if (chunkChanged(e.player)) {
      String name = getClaimName(e.player);
      if (name != null) {
        e.player.sendMessage(new TextComponentString(TextFormatting.RED + name));
      }
      playerChunkLoc.put(e.player, new ChunkPos(e.player.getPosition()));
    } else if (!playerChunkLoc.containsKey(e.player)) {
      playerChunkLoc.put(e.player, new ChunkPos(e.player.getPosition()));
    }
  }

  private boolean chunkChanged(EntityPlayer player) {
    if (playerChunkLoc.containsKey(player)) {
      return !(playerChunkLoc.get(player).x == ((int) player.posX >> 4)
          && playerChunkLoc.get(player).z == ((int) player.posZ >> 4));
    }
    return false;
  }

  private String getClaimName(EntityPlayer player) {
    Claim oldClaim = ChunkManager.getClaim(playerChunkLoc.get(player).getBlock(8, 0, 8));
    Claim claim = ChunkManager.getClaim(player.getPosition());
    if (oldClaim != null && claim != null) {
      if (oldClaim.getTeam() != null
          && claim.getTeam() != null
          && !claim.getTeam().getName().equalsIgnoreCase(oldClaim.getTeam().getName())) {
        return claim.getTeam().getName();
      } else if (oldClaim.getOwner() != null
          && claim.getOwner() != null
          && !oldClaim.getOwner().equals(claim.getOwner())) {
        return UsernameResolver.getUsername(claim.getOwner());
      }
    } else if (claim != null) {
      if (claim.getTeam() != null) {
        return claim.getTeam().getName();
      } else if (claim.getOwner() != null) {
        return UsernameResolver.getUsername(claim.getOwner());
      }
    }
    if (oldClaim != null && claim == null) {
      return "Wild";
    }
    return null;
  }
}
