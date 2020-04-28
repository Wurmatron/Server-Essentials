package com.wurmcraft.serveressentials.forge.modules.economy;

import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.api.player.Wallet;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.command.PerkCommand.Perk;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class EcoUtils {

  public static boolean hasTheMoney(Wallet wallet, double amount) {
    for (Coin c : wallet.currency) {
      if (c.name.equals(((EconomyConfig) (SERegistry
          .getStoredData(DataKey.MODULE_CONFIG,
              "Economy"))).defaultServerCurrency.name)) {
        return c.amount >= amount;
      }
    }
    return false;
  }

  public static double getCurrency(Wallet wallet) {
    for (Coin c : wallet.currency) {
      if (c.name.equals(((EconomyConfig) (SERegistry
          .getStoredData(DataKey.MODULE_CONFIG,
              "Economy"))).defaultServerCurrency.name)) {
        return c.amount;
      }
    }
    return 0;
  }

  public static Wallet setCurrency(Wallet wallet, double amount) {
    for (int i = 0; i < wallet.currency.length; i++) {
      if (wallet.currency[i].name.equals(((EconomyConfig) (SERegistry
          .getStoredData(DataKey.MODULE_CONFIG,
              "Economy"))).defaultServerCurrency.name)) {
        wallet.currency[i].amount = amount;
        return wallet;
      }
    }
    return wallet;
  }

  public static double calculateCostInGlobalPerPerk(Perk perk, int lvl) {
    return lvl;
  }

  public static int getPerkLevel(ICommandSender sender, Perk perk) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      StoredPlayer playerData = PlayerUtils.getPlayer(
          (EntityPlayer) sender.getCommandSenderEntity());
      if (playerData != null && playerData.global != null && playerData.global.perks != null && playerData.global.perks.length > 0) {
        for (String p : playerData.global.perks) {
          if (!p.isEmpty() && p.startsWith(perk.name().toLowerCase())) {
            return Integer.parseInt(p.substring(p.lastIndexOf(".") + 1));
          }
        }
      }

    }
    return 0;
  }

  public static GlobalPlayer setPlayerPerk(GlobalPlayer player, Perk perk, int level) {
    if (player.perks != null && player.perks.length > 0) {
      for (int i = 0; i < player.perks.length; i++) {
        if (player.perks[i].startsWith(perk.name().toLowerCase())) {
          player.perks[i] = perk.name().toLowerCase() + ".amount." + level;
        }
      }
    } else {
      player.perks = new String[]{perk.name().toLowerCase() + ".amount." + level};
    }
    if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      RestRequestGenerator.User.overridePlayer(player.uuid, player);
    }
    return player;
  }
}
