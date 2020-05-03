package com.wurmcraft.serveressentials.forge.modules.general.command.info;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Ping", aliases = {"P"})
public class PingCommand {

  @Command(inputArguments = {})
  public void ping(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.ping") || !SERegistry
        .isModuleLoaded("Rank")) {
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_PING));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
