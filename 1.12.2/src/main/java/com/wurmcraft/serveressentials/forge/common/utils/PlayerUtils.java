package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.general.GeneralConfig;
import java.util.Arrays;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

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
        Home[] newHomes = addHome(playerData.server.homes,home,false);
        if(newHomes != null) {
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
    } else if(!replaced) {
      return null;
    }
    return homes;
  }

}
