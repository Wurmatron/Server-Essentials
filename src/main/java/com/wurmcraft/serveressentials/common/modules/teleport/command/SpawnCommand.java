package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.general.GeneralModule;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class SpawnCommand extends Command {

  @Override
  public String getName() {
    return "Spawn";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/spawn";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_SPAWN;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    TeleportUtils.teleportTo(
        (EntityPlayerMP) sender.getCommandSenderEntity(), GeneralModule.config.spawn, true, false);
    ChatHelper.sendMessage(sender.getCommandSenderEntity(), senderLang.local.TELEPORT_SPAWN);
  }
}
