package com.wurmcraft.serveressentials.forge.modules.general.event;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.event.NewPlayerJoin;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.general.utils.PlayerInventory;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

public class GeneralEvents {

  private static NonBlockingHashMap<EntityPlayer, PlayerInventory> openInv = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<EntityPlayer, BlockPos> frozenPlayers = new NonBlockingHashMap<>();
  public static NonBlockingHashSet<EntityPlayer> vanishedPlayers = new NonBlockingHashSet<>();

  @SubscribeEvent
  public void onDeath(LivingDeathEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      if(player.capabilities.disableDamage) {
        player.setHealth(player.getMaxHealth());
        e.setCanceled(true);
        return;
      }
      if (SERegistry.isModuleLoaded("Rank") && RankUtils
          .hasPermission(RankUtils.getRank(player), "general.back.death") || !SERegistry
          .isModuleLoaded("Rank")) {
        try {
          StoredPlayer playerData = (StoredPlayer) SERegistry
              .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
          playerData.server.lastLocation = new LocationWrapper(player.posX, player.posY,
              player.posZ, player.dimension);
          PlayerDataEvents.savePlayer(player);
        } catch (NoSuchElementException f) {

        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onLogin(PlayerLoggedInEvent e) {
    if (SERegistry.isModuleLoaded("General")) {
      for (String m : (((GeneralConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "General")).motd)) {
        e.player.sendMessage(new TextComponentString(
            m.replaceAll("%SERVER_ID%", SERegistry.globalConfig.serverID).replaceAll("&",
                "\u00A7")));
      }
    }
  }

  @SubscribeEvent
  public void newPlayerLogin(NewPlayerJoin e) {
    TeleportUtils.teleportTo(e.player, ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).spawn);
  }

  @SubscribeEvent
  public void onLivingRespawn(PlayerRespawnEvent e) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, e.player.getGameProfile().getId().toString());
      teleportToPlayerSpawn(e.player, playerData);
    } catch (NoSuchElementException f) {

    }
    if (!vanishedPlayers.isEmpty() && vanishedPlayers.contains(e.player)) {
      PlayerUtils.updateVanishStatus(e.player, false);
    }
  }

  @SubscribeEvent
  public void onDimChange(PlayerChangedDimensionEvent e) {
    if (!vanishedPlayers.isEmpty() && vanishedPlayers.contains(e.player)) {
      PlayerUtils.updateVanishStatus(e.player, false);
    }
  }

  private void teleportToPlayerSpawn(EntityPlayer player, StoredPlayer playerData) {
    if (player.getBedLocation() != null && player.world.getSpawnPoint() != null && !player.getBedLocation().equals(player.world.getSpawnPoint())) {
      BlockPos bedLocation = player.getBedLocation(0);
      player.setPositionAndUpdate(bedLocation.getX() + .5, bedLocation.getY() + 1,
          bedLocation.getZ() + .5);
    }
    if (playerData.server.homes != null && playerData.server.homes.length > 0) {
      for (Home h : playerData.server.homes) {
        if (h.name.equals(((GeneralConfig) SERegistry
            .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome)) {
          if (player.dimension != h.dim) {
            player.changeDimension(h.dim);
          }
          player.setPositionAndUpdate(h.x + .5, h.y + 1, h.z + .5);
          return;
        }
      }
      Home h = playerData.server.homes[0];
      if (player.dimension != h.dim) {
        player.changeDimension(h.dim);
      }
      player.setPositionAndUpdate(h.x + .5, h.y + 1, h.z + .5);
    } else {
      LocationWrapper loc = ((GeneralConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "General")).spawn;
      if (player.dimension != loc.dim) {
        player.changeDimension(loc.dim);
      }
      player.setPositionAndUpdate(loc.x + .5, loc.y + 1, loc.z + .5);
    }
  }

  public static void register(PlayerInventory inv) {
    openInv.put(inv.owner, inv);
  }

  public static void remove(PlayerInventory inv) {
    openInv.remove(inv.owner, inv);
  }

  @SubscribeEvent
  public void tickStart(TickEvent.PlayerTickEvent e) {
    if (openInv.size() > 0 && openInv.containsKey(e.player)) {
      openInv.get(e.player).update();
    }
    if (frozenPlayers.size() > 0 && frozenPlayers.keySet().contains(e.player)) {
      BlockPos lockedPos = frozenPlayers.get(e.player);
      if (e.player.getPosition() != lockedPos) {
        e.player
            .setPositionAndUpdate(lockedPos.getX(), lockedPos.getY(), lockedPos.getZ());
      }
    }
  }

  public static void addFrozen(EntityPlayer player, BlockPos pos) {
    if (!frozenPlayers.keySet().contains(player)) {
      player.capabilities.disableDamage = true;
      frozenPlayers.put(player, pos);
    }
  }

  public static void removeFrozen(EntityPlayer player) {
    if (frozenPlayers.size() > 0 && frozenPlayers.keySet().contains(player)) {
      frozenPlayers.remove(player);
      player.capabilities.disableDamage = false;
    }
  }

  public static void toggleFrozen(EntityPlayer player, BlockPos pos) {
    if (frozenPlayers.keySet().contains(player)) {
      removeFrozen(player);
    } else {
      addFrozen(player, pos);
    }
  }

  public static boolean isFrozen(EntityPlayer player) {
    return frozenPlayers.keySet().contains(player);
  }
}
