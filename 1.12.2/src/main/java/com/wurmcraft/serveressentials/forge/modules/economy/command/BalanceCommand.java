package com.wurmcraft.serveressentials.forge.modules.economy.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Economy", name = "Balance", aliases = {"B", "Money", "M", "Bal"})
public class BalanceCommand {

  @Command(inputArguments = {})
  public void displayBalance(ICommandSender sender) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        for (Coin c : playerData.global.wallet.currency) {
          if (((EconomyConfig) SERegistry
              .getStoredData(DataKey.MODULE_CONFIG, "Economy")).defaultServerCurrency.name
              .equals(c.name)) {
            player.sendMessage(new TextComponentString(
                COMMAND_COLOR + c.name + ": " + COMMAND_INFO_COLOR + c.amount));
          } else {
            player.sendMessage(new TextComponentString(
                ERROR_COLOR + c.name + ": " + COMMAND_INFO_COLOR + c.amount));
          }
        }
      } catch (NoSuchElementException ignored) {

      }
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER})
  public void displayBalanceOther(ICommandSender sender, EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      for (Coin c : playerData.global.wallet.currency) {
        if (((EconomyConfig) SERegistry
            .getStoredData(DataKey.MODULE_CONFIG, "Economy")).defaultServerCurrency.name
            .equals(c.name)) {
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + c.name + ": " + COMMAND_INFO_COLOR + c.amount));
        } else {
          player.sendMessage(new TextComponentString(
              ERROR_COLOR + c.name + ": " + COMMAND_INFO_COLOR + c.amount));
        }
      }
    } catch (NoSuchElementException ignored) {

    }
  }

}
