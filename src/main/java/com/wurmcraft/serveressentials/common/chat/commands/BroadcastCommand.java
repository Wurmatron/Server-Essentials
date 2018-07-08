package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Command(moduleName = "General")
public class BroadcastCommand extends SECommand {

  @Override
  public String getName() {
    return "broadcast";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getPlayerList()
          .sendMessage(new TextComponentString(Strings.join(args, " ").replaceAll("&", "\u00A7")));
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
