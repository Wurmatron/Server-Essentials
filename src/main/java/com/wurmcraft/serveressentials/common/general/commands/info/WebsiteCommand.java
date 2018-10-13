package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

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
    ChatHelper.sendMessage(sender, DataHelper.globalSettings.getWebsite());
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/website";
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_WEBSITE;
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("web");
    alts.add("site");
    alts.add("w");
    return alts;
  }
}
