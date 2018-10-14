package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import java.util.ArrayList;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class ServerMotdCommand extends SECommand {

  @Override
  public String getName() {
    return "serverMotd";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/serverMotd <msg> ...";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("gMotd");
    return alts;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length > 0) {
      if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
        DataHelper.globalSettings.setGlobalMOTD(new String[0]);
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).MOTD_CHANGED);
      } else {
        DataHelper.globalSettings.setGlobalMOTD(new String[] {Strings.join(args, " ")});
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).MOTD_CHANGED);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SERVER_MOTD;
  }
}
