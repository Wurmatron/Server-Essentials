package com.wurmcraft.serveressentials.forge.common.utils;

import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.forge.modules.economy.command.PerkCommand.Perk;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandParser {

  public static Object[] parseLineToArguments(ICommandSender sender, String[] commandArgs,
      CommandArguments[] argumentFormat) {
    if (commandArgs != null && argumentFormat != null
        && commandArgs.length == argumentFormat.length) {
      Object[] formattedCommandArgs = new Object[argumentFormat.length + 1];
      formattedCommandArgs[0] = sender;
      for (int i = 0; i < argumentFormat.length; i++) {
        formattedCommandArgs[i + 1] = getInstanceForArgument(commandArgs[i],
            argumentFormat[i]);
      }
      return formattedCommandArgs;
    }
    return new Object[0];
  }

  private static Object getInstanceForArgument(String line, CommandArguments arg) {
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
    } else if (arg == CommandArguments.STRING) {
      return line;
    }
    return null;
  }

}
