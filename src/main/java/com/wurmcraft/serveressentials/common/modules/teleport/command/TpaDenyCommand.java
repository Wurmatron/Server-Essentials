package com.wurmcraft.serveressentials.common.modules.teleport.command;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.teleport.TeleportModule;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@ModuleCommand(moduleName = "Teleportation")
public class TpaDenyCommand extends Command {

  @Override
  public String getName() {
    return "tpaDeny";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/tpaDeny";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_TPADENY;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (TeleportModule.tpaRequests.containsKey(player.getGameProfile().getId())) {
      TeleportModule.tpaRequests.remove(player.getGameProfile().getId());
      ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TPA_DENY);
    } else {
      ChatHelper.sendMessage(sender, senderLang.local.TELEPORT_TPA_NONE);
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("Deny");
    aliases.add("TpDeny");
    aliases.add("Den");
    return aliases;
  }
}
