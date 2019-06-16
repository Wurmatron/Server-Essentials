package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class ReloadGlobalCommand extends Command {

  @Override
  public String getName() {
    return "reloadGlobal";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/reloadGlobal";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_RELOAD_GLOBAL;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      GeneralModule.loadAndSetupGlobal();
      ChatHelper.sendMessage(sender, senderLang.local.GENERAL_RELOAD_GLOBAL);

    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
