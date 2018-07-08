package com.wurmcraft.serveressentials.common.team.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.language.Local;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Team")
public class TeamCommand extends SECommand {

  private static String[] formatTeam(Local lang, GlobalTeam team) {
    List<String> lines = new ArrayList<>();
    lines.add(lang.CHAT_SPACER);
    if (team != null) {
      lines.add(
          TextFormatting.LIGHT_PURPLE
              + lang.CHAT_NAME
              + ": "
              + TextFormatting.AQUA
              + team.getName());
      lines.add(
          TextFormatting.LIGHT_PURPLE
              + lang.CHAT_LEADER
              + ": "
              + TextFormatting.AQUA
              + team.getLeader());
      for (String active : ConfigHandler.activeCurrency) {
        lines.add(
            TextFormatting.LIGHT_PURPLE
                + active
                + ": "
                + TextFormatting.GOLD
                + team.getBank().getCurrency(active.replaceAll(" ", "_")));
      }
    }
    lines.add(lang.CHAT_SPACER);
    return lines.toArray(new String[0]);
  }

  @Override
  public String getName() {
    return "seTeam";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/seTeam <name> <info>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    } else {
      if (args.length == 2 && args[1].equalsIgnoreCase("info")) {
        GlobalTeam team = forceTeamFromName(args[0]);
        for (String line : formatTeam(getCurrentLanguage(sender), team)) {
          sender.sendMessage(new TextComponentString(line));
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    }
  }
}
