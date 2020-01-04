package com.wurmcraft.serveressentials.common.modules.matterbridge.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.matterbridge.MatterBridgeModule;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "MatterBridge")
public class BridgeCommand extends Command {

  @Override
  public String getName() {
    return "bridge";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/bridge reload";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.BRIDGE_DESCRIPTION;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    ChatHelper.sendMessage(sender, getUsage(senderLang));
  }

  @SubCommand
  public void reload(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    MatterBridgeModule.setupAndLoadConfig();
    ChatHelper.sendMessage(sender, senderLang.local.BRIDGE_RELOAD);
  }
}
