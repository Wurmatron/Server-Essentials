package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class PingCommand extends Command {

  @Override
  public String getName() {
    return "Ping";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/ping";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_PING;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(sender, senderLang.local.GENERAL_PING);
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("P");
    aliases.add("Hello");
    return aliases;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
