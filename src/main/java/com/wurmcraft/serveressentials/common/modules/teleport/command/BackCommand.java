package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.user.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class BackCommand extends Command {

  @Override
  public String getName() {
    return "Back";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/back";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_BACK;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      TeleportUtils.teleportTo(
          (EntityPlayerMP) player, UserManager.getLastLocation(player), true, false);
      ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_BACK);
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }
}
