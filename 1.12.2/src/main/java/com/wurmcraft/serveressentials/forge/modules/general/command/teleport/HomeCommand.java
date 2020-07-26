package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Home", aliases = {"H"})
public class HomeCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"home"})
  public void homeSpecific(ICommandSender sender, String name) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      StoredPlayer playerData = PlayerUtils.getPlayer(player);
      if (name.equalsIgnoreCase("list")) {
        for (Home home : playerData.server.homes) {
          sender.sendMessage(new TextComponentString(COMMAND_COLOR + home.name));
        }
      } else {
        for (Home home : playerData.server.homes) {
          if (home.name.equalsIgnoreCase(name)) {
            TeleportUtils.teleportTo(player, home);
            sender.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).GENERAL_HOME_TELEPORT
                    .replaceAll("%HOME%", COMMAND_INFO_COLOR + name + COMMAND_COLOR)));
            return;
          }
        }
        sender.sendMessage(new TextComponentString(
            PlayerUtils.getUserLanguage(player).GENERAL_HOME_NOT_FOUND
                .replaceAll("%HOM%", COMMAND_INFO_COLOR + name + COMMAND_COLOR)));
      }
    }
  }

  @Command(inputArguments = {CommandArguments.HOME}, inputNames = {"Home"})
  public void homeExact(ICommandSender sender, Home home) {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      TeleportUtils.teleportTo(player, home);
      sender.sendMessage(new TextComponentString(COMMAND_COLOR +
          PlayerUtils.getUserLanguage(player).GENERAL_HOME_TELEPORT
              .replaceAll("%HOME%", COMMAND_INFO_COLOR + home.name + COMMAND_COLOR)));
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER}, inputNames = {"Home"})
  public void homeNo(ICommandSender sender, int name) {
    homeSpecific(sender, "" + name);
  }

  @Command(inputArguments = {})
  public void home(ICommandSender sender) {
    homeSpecific(sender, ((GeneralConfig) SERegistry
        .getStoredData(DataKey.MODULE_CONFIG, "General")).defaultHome);
  }
}
