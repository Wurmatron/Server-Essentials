package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class SunCommand extends Command {

  @Override
  public String getName() {
    return "sun";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/sun";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SUN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    server.getWorld(0).getWorldInfo().setRaining(false);
    server.getWorld(0).getWorldInfo().setThundering(false);
    server.getWorld(0).getWorldInfo().setRainTime(0);
    ChatHelper.sendMessage(sender, senderLang.local.GENERAL_SUN);
  }
}
