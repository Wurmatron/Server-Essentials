package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Command(moduleName = "General")
public class SunCommand extends SECommand {

  @Override
  public String getName() {
    return "sun";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    server.getWorld(0).getWorldInfo().setRaining(false);
    server.getWorld(0).getWorldInfo().setThundering(false);
    server.getWorld(0).getWorldInfo().setRainTime(0);
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).SUN);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
