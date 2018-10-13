package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class DiscordCommand extends SECommand {

  @Override
  public String getName() {
    return "discord";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    ChatHelper.sendMessage(sender, ConfigHandler.discordLink);
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/discord";
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("disco");
    return alts;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_DISCORD.replaceAll("&", FORMATTING_CODE);
  }
}
