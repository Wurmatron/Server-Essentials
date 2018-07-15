package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Command(moduleName = "General")
public class WebsiteCommand extends SECommand {

  @Override
  public String getName() {
    return "website";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    ChatHelper.sendMessage(
        sender, DataHelper.globalSettings.getWebsite().replaceAll("&", "\u00A7"));
  }
}
