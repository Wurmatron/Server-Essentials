package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class ServerCommand extends Command {

  @Override
  public String getName() {
    return "Server";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/server";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SERVER;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(
        sender,
        senderLang.local.GENERAL_SERVER.replaceAll(Replacment.SERVER, ConfigHandler.serverName));
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
