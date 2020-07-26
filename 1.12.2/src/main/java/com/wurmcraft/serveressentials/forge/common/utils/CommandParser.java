package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.economy.command.PerkCommand.Perk;
import java.util.Arrays;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandParser {

  public static Object[] parseLineToArguments(ICommandSender sender, String[] commandArgs,
      CommandArguments[] format) {
    if (format == null || format.length == 0) {
      return new Object[]{sender};
    } else if (format.length == 1 && format[0].equals(CommandArguments.STRING_ARR)) {
      return new Object[]{sender, commandArgs};
    } else if (format.length == 1) {
      return new Object[]{sender,
          getInstanceForArgument(sender, commandArgs[0], format[0])};
    } else {
      if (format[format.length - 1].equals(CommandArguments.STRING_ARR)) {
        Object[] formattedObject = new Object[commandArgs.length + 1];
        formattedObject[0] = sender;
        for (int index = 0; index < format.length; index++) {
          if (index == format.length) {
            for (int currentIndex = index; currentIndex < commandArgs.length;
                currentIndex++) {
              formattedObject[currentIndex + 1] = getInstanceForArgument(sender,
                  commandArgs[currentIndex], CommandArguments.STRING);
            }
          } else {
            formattedObject[index + 1] = getInstanceForArgument(sender,
                commandArgs[index],
                format[index]);
          }
        }
        return formattedObject;
      } else {
        Object[] formattedObject = new Object[format.length + 1];
        formattedObject[0] = sender;
        for (int index = 0; index < format.length; index++) {
          formattedObject[index + 1] = getInstanceForArgument(sender, commandArgs[index],
              format[index]);
        }
        return formattedObject;
      }
    }
  }

  public static Object getInstanceForArgument(ICommandSender sender, String line,
      CommandArguments arg) {
    if (arg == CommandArguments.INTEGER) {
      return Integer.parseInt(line);
    } else if (arg == CommandArguments.DOUBLE) {
      return Double.parseDouble(line);
    } else if (arg == CommandArguments.PERK) {
      for (Perk p : Perk.values()) {
        if (p.name().equalsIgnoreCase(line)) {
          return p;
        }
      }
    } else if (arg == CommandArguments.PLAYER) {
      for (EntityPlayerMP player : FMLCommonHandler.instance()
          .getMinecraftServerInstance().getPlayerList()
          .getPlayers()) {
        if (player.getName().equalsIgnoreCase(line)) {
          return player;
        }
      }
      return null;
    } else if (arg == CommandArguments.STRING || arg == CommandArguments.STRING_ARR) {
      return line;
    } else if (arg == CommandArguments.RANK) {
      return SERegistry.getStoredData(DataKey.RANK, line);
    } else if (arg == CommandArguments.HOME && sender
        .getCommandSenderEntity() instanceof EntityPlayer) {
      Home[] homes = PlayerUtils
          .getPlayer((EntityPlayer) sender.getCommandSenderEntity()).server.homes;
      for (Home home : homes) {
        if (home.name.equalsIgnoreCase(line)) {
          return home;
        }
      }
    }
    return null;
  }

}
