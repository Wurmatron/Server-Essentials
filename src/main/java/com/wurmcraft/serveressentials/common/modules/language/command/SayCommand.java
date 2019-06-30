package com.wurmcraft.serveressentials.common.modules.language.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import joptsimple.internal.Strings;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@ModuleCommand(moduleName = "General")
public class SayCommand extends Command {

  @Override
  public String getName() {
    return "say";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/say <msg>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SAY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 0) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .commandManager
          .executeCommand(sender, "bc " + ConfigHandler.serverPrefix + Strings.join(args, " "));
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}
