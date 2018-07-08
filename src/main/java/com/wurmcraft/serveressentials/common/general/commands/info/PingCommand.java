package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class PingCommand extends SECommand {

  @Override
  public String getName() {
    return "ping";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).PING_RESPONSE));
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
