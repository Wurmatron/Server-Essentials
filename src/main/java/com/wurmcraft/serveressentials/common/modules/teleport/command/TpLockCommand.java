package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class TpLockCommand extends Command {

  @Override
  public String getName() {
    return "tpLock";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/tpLock";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TPLOCK;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      boolean lock = UserManager.toggleTPA((EntityPlayer) sender.getCommandSenderEntity());
      if (lock) {
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TOGGLE_ENABLE);
      } else {
        ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TOGGLE_DISABLE);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Lock");
    return aliases;
  }
}
