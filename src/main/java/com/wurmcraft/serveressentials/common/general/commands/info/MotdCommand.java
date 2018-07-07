package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
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
      sender.sendMessage(new TextComponentString(motdLine.replaceAll("&", "\u00A7")));
    }
  }
}
