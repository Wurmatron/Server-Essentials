package com.wurmcraft.serveressentials.forge.modules.core.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.Global;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.Arrays;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Core", name = "ServerEssentials", aliases = {"SE"})
public class ServerEssentialsCommand {

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {
      "<version, storageType, modules, commands>"})
  public void info(ICommandSender sender, String input) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "core.se.info") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (input.equalsIgnoreCase("version") || input.equalsIgnoreCase("v")) {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).CORE_SE_VERSION
                .replaceAll("%VERSION%",
                    COMMAND_INFO_COLOR + Global.VERSION)));
      } else if (input.equalsIgnoreCase("storageType") || input
          .equalsIgnoreCase("storage")) {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).CORE_SE_STORAGE
                .replaceAll("%TYPE%",
                    COMMAND_INFO_COLOR + SERegistry.globalConfig.dataStorgeType)));
      } else if (input.equalsIgnoreCase("modules") || input.equalsIgnoreCase("module")
          || input.equalsIgnoreCase("m ")) {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).CORE_SE_MODULES
                .replaceAll("%MODULES%",
                    COMMAND_INFO_COLOR + Arrays
                        .toString(SERegistry.getLoadedModules()))));
      } else if (input.equalsIgnoreCase("commands") || input.equalsIgnoreCase("command")
          || input.equalsIgnoreCase("c ")) {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).CORE_SE_COMMANDS
                .replaceAll("%COMMANDS%",
                    COMMAND_INFO_COLOR + Arrays
                        .toString(SERegistry.getLoadedCommands()))));
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
      }
    }
  }
}
