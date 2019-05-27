package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import java.util.List;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Language")
public class BroadcastCommand extends Command {

  @Override
  public String getName() {
    return "broadcast";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/bc <msg>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_BROADCAST;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 0) {
      ChatHelper.sendMessageToAll(Strings.join(args, " "));
    } else {
      ChatHelper.sendMessage(sender, getUsage(LanguageModule.getUserLanguage(sender)));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("bc");
    aliases.add("announce");
    aliases.add("notice");
    return aliases;
  }
}
