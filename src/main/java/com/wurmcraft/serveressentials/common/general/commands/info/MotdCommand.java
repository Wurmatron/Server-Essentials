package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class MotdCommand extends SECommand {

  @Override
  public String getName() {
    return "motd";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    for (String motdLine : DataHelper.globalSettings.getMotd()) {
      ChatHelper.sendMessage(sender, motdLine);
    }
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A70/motd";
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_MOTD.replaceAll("&", "\u00A7");
  }
}
