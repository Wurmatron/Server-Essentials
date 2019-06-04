package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "General")
public class RainCommand extends Command {

  @Override
  public String getName() {
    return "rain";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/rain";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_RAIN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    server.getWorld(0).getWorldInfo().setRaining(true);
    server.getWorld(0).getWorldInfo().setThundering(true);
    server.getWorld(0).getWorldInfo().setRainTime(server.getWorld(0).rand.nextInt(168000) + 12000);
    ChatHelper.sendMessage(sender, senderLang.local.GENERAL_RAIN);
  }
}
