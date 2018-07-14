package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
    sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).SUN));
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
