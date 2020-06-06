package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.api.track.NetworkTime;
import com.wurmcraft.serveressentials.core.api.track.ServerTime;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.world.GameType;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerUtils {

  public static Language getUserLanguage(String langKey) {
    return (Language) SERegistry.getStoredData(DataKey.LANGUAGE, langKey);
  }

  public static Language getUserLanguage(ICommandSender sender) {
    return (Language) SERegistry
        .getStoredData(DataKey.LANGUAGE, "en_us"); // TODO Load Default
  }

  public static int getMaxHomes(EntityPlayer player) {
    int maxHomes = 1;
    if (SERegistry.isModuleLoaded("General")) {
      maxHomes = ((GeneralConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "General")).startingHomeAmount;
    }
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if (playerData.global.perks != null && playerData.global.perks.length > 0) {
        for (String p : playerData.global.perks) {
          if (p.toLowerCase().startsWith("home.amount.")) {
            try {
              int additionalHomes = Integer.parseInt(p.substring(p.lastIndexOf(".") + 1));
              return maxHomes + additionalHomes;
            } catch (NumberFormatException e) {
              SECore.logger.info("Max Homes for '" + player.getDisplayNameString()
                  + "' perk is invalid!");
            }
          }
        }
      }
    } catch (NoSuchElementException e) {
      return 0;
    }
    return maxHomes;
  }

  public static boolean setHome(EntityPlayer player, Home home) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      int maxHomes = PlayerUtils.getMaxHomes(player);
      if (maxHomes > playerData.server.homes.length) {
        playerData.server.homes = addHome(playerData.server.homes, home, true);
        return true;
      } else { // Possible Override
        Home[] newHomes = addHome(playerData.server.homes, home, false);
        if (newHomes != null) {
          playerData.server.homes = newHomes;
          return true;
        }
      }
      return false;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private static Home[] addHome(Home[] homes, Home home, boolean canExpand) {
    boolean replaced = false;
    for (int i = 0; i < homes.length; i++) {
      if (homes[i].name.equals(home.name)) {
        homes[i] = home;
        replaced = true;
      }
    }
    if (!replaced && canExpand) {
      homes = Arrays.copyOf(homes, homes.length + 1);
      homes[homes.length - 1] = home;
    } else if (!replaced) {
      return null;
    }
    return homes;
  }

  public static StoredPlayer deleteHome(StoredPlayer player, String name) {
    List<Home> homes = new ArrayList<>();
    for (Home h : player.server.homes) {
      if (!h.name.equalsIgnoreCase(name)) {
        homes.add(h);
      }
    }
    player.server.homes = homes.toArray(new Home[0]);
    return player;
  }

  public static StoredPlayer getPlayer(EntityPlayer player) {
    try {
      return (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
    } catch (NoSuchElementException ignored) {
    }
    return null;
  }

  public static List<String> predictUsernames(String[] args, int index) {
    List<String> possibleUsernames =
        Arrays.asList(
            FMLCommonHandler.instance().getMinecraftServerInstance()
                .getOnlinePlayerNames());
    if (args.length > index && args[index] != null) {
      return predictName(args[index], possibleUsernames);
    } else {
      return possibleUsernames;
    }
  }

  private static List<String> predictName(String current, List<String> possibleNames) {
    List<String> predictedNames = new ArrayList<>();
    for (String name : possibleNames) {
      if (name.toLowerCase().startsWith(current.toLowerCase())
          || name.toLowerCase().endsWith(current.toLowerCase())) {
        predictedNames.add(name);
      }
    }
    return predictedNames;
  }

  public static GameType getGamemode(String mode) {
    for (GameType t : GameType.values()) {
      if (t.getName().equalsIgnoreCase(mode)) {
        return t;
      }
    }
    if (mode.toUpperCase().startsWith("SP")) {
      return GameType.SPECTATOR;
    } else if (mode.toUpperCase().startsWith("S")) {
      return GameType.SURVIVAL;
    } else if (mode.toUpperCase().startsWith("C")) {
      return GameType.CREATIVE;
    } else if (mode.toUpperCase().startsWith("A")) {
      return GameType.ADVENTURE;
    }
    return null;
  }

  public static UUID getPlayer(String playerName) {
    for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getPlayerList().getPlayers()) {
      if (p.getDisplayNameString().equalsIgnoreCase(playerName)) {
        return p.getGameProfile().getId();
      }
    }
    for (UUID id : UsernameCache.getMap().keySet()) {
      if (UsernameCache.getMap().get(id).equalsIgnoreCase(playerName)) {
        return id;
      }
    }
    return null;
  }

  public static void updateVanishStatus(EntityPlayer player, boolean isVisable) {
    if (!isVisable) {
      FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(player.dimension)
          .getEntityTracker().untrack(player);
    } else {
      FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(player.dimension)
          .getEntityTracker().track(player);
    }
    for (EntityPlayer p : FMLCommonHandler.instance().getMinecraftServerInstance()
        .getWorld(player.dimension).getEntityTracker().getTrackingPlayers(player)) {
      if (!SERegistry.isModuleLoaded("Rank") || !RankUtils
          .hasPermission(RankUtils.getRank(player), "general.vanish.see")) {
        ((EntityPlayerMP) player).connection.sendPacket(new SPacketSpawnPlayer(player));
      }
    }
  }

  public static long getTotalPlaytime(EntityPlayer player, StoredPlayer playerData) {
    long total = 0;
    for(ServerTime time : playerData.global.playtime.serverTime)
    total =+ time.time;
    return total;
  }
}
