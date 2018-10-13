package com.wurmcraft.serveressentials.common.claim2.events;

import com.wurmcraft.serveressentials.api.json.claim2.Claim;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimOwner;
import com.wurmcraft.serveressentials.api.json.claim2.ClaimPerm;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.claim2.ClaimManager;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClaimEvent {

  private static HashMap<EntityPlayer, ChunkPos> playerPos = new HashMap<>();

  @SubscribeEvent
  public void playerTick(TickEvent.PlayerTickEvent e) {
    if (chunkChanged(e.player)) {
      String name = getClaimName(e.player);
      if (name != null) {
        ChatHelper.sendMessage(e.player, TextFormatting.RED + name);
      }
      playerPos.put(e.player, new ChunkPos(e.player.getPosition()));
    } else if (!playerPos.containsKey(e.player)) {
      playerPos.put(e.player, new ChunkPos(e.player.getPosition()));
    }
  }

  @SubscribeEvent
  public void onBlockBreak(BlockEvent.BreakEvent e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getPlayer().getPosition(), e.getPlayer().dimension));
    if (claim != null) {
      if (!claim.isTrusted(e.getPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
        e.setCanceled(true);
        ChatHelper.sendMessage(
            e.getPlayer(),
            LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
                .CLAIM_BREAK
                .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
      }
    }
  }

  @SubscribeEvent
  public void onBlockPlace(BreakEvent e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getPlayer().getPosition(), e.getPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getPlayer(), new ClaimPerm[] {ClaimPerm.PLACE})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getPlayer(),
          LanguageModule.getLangfromUUID(e.getPlayer().getGameProfile().getId())
              .CLAIM_PLACE
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  @SubscribeEvent
  public void oInteractRightAir(PlayerInteractEvent.RightClickEmpty e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getEntityPlayer().getPosition(), e.getEntityPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getEntityPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
              .CLAIM_INTERACT
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  @SubscribeEvent
  public void oInteractLeftAir(PlayerInteractEvent.LeftClickEmpty e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getEntityPlayer().getPosition(), e.getEntityPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getEntityPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
              .CLAIM_INTERACT
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  @SubscribeEvent
  public void oInteractRightBlock(PlayerInteractEvent.RightClickBlock e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getEntityPlayer().getPosition(), e.getEntityPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getEntityPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
              .CLAIM_INTERACT
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  @SubscribeEvent
  public void oInteractLeftBlock(PlayerInteractEvent.LeftClickBlock e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getEntityPlayer().getPosition(), e.getEntityPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getEntityPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
              .CLAIM_INTERACT
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  // TODO Item Whitelist / BlackList
  @SubscribeEvent
  public void oInteractRightItem(PlayerInteractEvent.RightClickItem e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(e.getEntityPlayer().getPosition(), e.getEntityPlayer().dimension));
    if (claim != null && !claim.isTrusted(e.getEntityPlayer(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getLangfromUUID(e.getEntityPlayer().getGameProfile().getId())
              .CLAIM_INTERACT
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  @SubscribeEvent
  public void onExplosion(ExplosionEvent e) {
    Claim claim =
        ClaimManager.getClaim(
            new LocationWrapper(
                e.getExplosion().getPosition(), e.getExplosion().getExplosivePlacedBy().dimension));
    if (claim != null
        && !claim.isTrusted(
            e.getExplosion().getExplosivePlacedBy(), new ClaimPerm[] {ClaimPerm.BREAK})) {
      e.setCanceled(true);
      ChatHelper.sendMessage(
          e.getExplosion().getExplosivePlacedBy(),
          LanguageModule.getLangfromUUID(e.getExplosion().getExplosivePlacedBy().getPersistentID())
              .CLAIM_EXPLOSION
              .replaceAll("%PLAYER%", getOwnerName(claim.getOwner())));
    }
  }

  private boolean chunkChanged(EntityPlayer player) {
    if (playerPos.containsKey(player)) {
      return !(playerPos.get(player).x == ((int) player.posX >> 4)
          && playerPos.get(player).z == ((int) player.posZ >> 4));
    }
    return false;
  }

  private String getClaimName(EntityPlayer player) {
    Claim oldClaim =
        ClaimManager.getClaim(
            new LocationWrapper(playerPos.get(player).getBlock(8, 0, 8), player.dimension));
    Claim claim =
        ClaimManager.getClaim(new LocationWrapper(player.getPosition(), player.dimension));
    if (oldClaim != null && claim != null) {
      return getOwnerName(claim.getOwner());
    }
    if (oldClaim != null && claim == null) {
      return "Wild";
    }
    return null;
  }

  private String getOwnerName(ClaimOwner owner) {
    String player = UsernameResolver.getNick(owner.getOwner());
    if (player != null && player.length() > 0) {
      return player;
    } else {
      String resolver = UsernameCache.getLastKnownUsername(owner.getOwner());
      if (resolver != null && resolver.length() > 0) {
        return resolver;
      }
    }
    return "";
  }
}
