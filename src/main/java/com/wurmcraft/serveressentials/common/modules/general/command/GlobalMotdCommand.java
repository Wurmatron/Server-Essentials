package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.io.File;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class GlobalMotdCommand extends Command {

  @Override
  public String getName() {
    return "GlobalMOTD";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/globalMOTD <line>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_GLOBALMOTD;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      ChatHelper.sendMessage(sender, GeneralModule.config.globalMOTD);
    } else if (args.length > 1) {
      GeneralModule.config.globalMOTD = Strings.join(args, " ");
      DataHelper.save(new File(ConfigHandler.saveLocation), GeneralModule.config);
      ChatHelper.sendMessage(
          sender,
          senderLang.local.GENERAL_GLOBALMOTD_SET.replaceAll(
              Replacment.MESSAGE, Strings.join(args, " ")));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
