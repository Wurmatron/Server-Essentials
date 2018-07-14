package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import java.util.ArrayList;
import java.util.List;
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

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/broadcast \u00A7b<the message here>";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("bc");
    alts.add("say");
    return alts;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_BROADCAST;
  }
}
