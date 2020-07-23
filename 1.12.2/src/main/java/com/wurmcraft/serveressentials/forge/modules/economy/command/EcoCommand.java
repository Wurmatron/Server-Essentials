package com.wurmcraft.serveressentials.forge.modules.economy.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Economy", name = "Eco")
public class EcoCommand {

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.STRING,
      CommandArguments.INTEGER})
  public void change(ICommandSender sender, EntityPlayer player, String type,
      int amount) {
    if (type.equalsIgnoreCase("add")) {
      EcoUtils.addCurrency(player, amount);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
          .getUserLanguage(sender).ECO_ECO_ADD.replaceAll("%PLAYER%",
              COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
          .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
    } else if (type.equalsIgnoreCase("remove") || type.equalsIgnoreCase("rem")) {
      EcoUtils.consumeCurrency(player, amount);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
          .getUserLanguage(sender).ECO_ECO_REM.replaceAll("%PLAYER%",
              COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
          .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.STRING,
      CommandArguments.DOUBLE})
  public void change(ICommandSender sender, EntityPlayer player, String type,
      double amount) {
    if (type.equalsIgnoreCase("add")) {
      EcoUtils.addCurrency(player, amount);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
          .getUserLanguage(sender).ECO_ECO_ADD.replaceAll("%PLAYER%",
              COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
          .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
    } else if (type.equalsIgnoreCase("remove") || type.equalsIgnoreCase("rem")) {
      EcoUtils.consumeCurrency(player, amount);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
          .getUserLanguage(sender).ECO_ECO_REM.replaceAll("%PLAYER%",
              COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
          .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.STRING,
      CommandArguments.INTEGER, CommandArguments.STRING})
  public void change(ICommandSender sender, EntityPlayer player, String type,
      int amount, String currency) {
    try {
      SERegistry.getStoredData(DataKey.CURRENCY, currency);
      if (type.equalsIgnoreCase("add")) {
        EcoUtils.consumeCurrency(player, amount, currency);
        sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
            .getUserLanguage(sender).ECO_ECO_ADD.replaceAll("%PLAYER%",
                COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
            .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
      } else if (type.equalsIgnoreCase("remove") || type.equalsIgnoreCase("rem")) {
        EcoUtils.consumeCurrency(player, amount);
        sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
            .getUserLanguage(sender).ECO_ECO_REM.replaceAll("%PLAYER%",
                COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
            .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
      }
    } catch (NoSuchElementException e) {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_ECO_NOT_FOUND
              .replaceAll("%ECO%", currency)));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER, CommandArguments.STRING,
      CommandArguments.DOUBLE, CommandArguments.STRING})
  public void change(ICommandSender sender, EntityPlayer player, String type,
      double amount, String currency) {
    try {
      SERegistry.getStoredData(DataKey.CURRENCY, currency);
      if (type.equalsIgnoreCase("add")) {
        EcoUtils.consumeCurrency(player, amount, currency);
        sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
            .getUserLanguage(sender).ECO_ECO_ADD.replaceAll("%PLAYER%",
                COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
            .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
      } else if (type.equalsIgnoreCase("remove") || type.equalsIgnoreCase("rem")) {
        EcoUtils.consumeCurrency(player, amount);
        sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
            .getUserLanguage(sender).ECO_ECO_REM.replaceAll("%PLAYER%",
                COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
            .replaceAll("%AMOUNT%", COMMAND_INFO_COLOR + amount + COMMAND_COLOR)));
      }
    } catch (NoSuchElementException e) {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_ECO_NOT_FOUND
              .replaceAll("%ECO%", currency)));
    }
  }

}
