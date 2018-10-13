package com.wurmcraft.serveressentials.common.teleport.commands.user.tpa;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Teleportation")
public class TpaAcceptCommand extends SECommand {

  @Override
  public String getName() {
    return "tpaccept";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      for (long time : TeleportationModule.activeRequests.keySet()) {
        EntityPlayer[] players = TeleportationModule.activeRequests.get(time);
        if (players[1].getGameProfile().getId().equals(player.getGameProfile().getId())) {
          ChatHelper.sendMessage(
              player, getCurrentLanguage(sender).TPA_ACCEPT.replaceAll("&", FORMATTING_CODE));
          ChatHelper.sendMessage(
              players[0], getCurrentLanguage(sender).TPA_ACCEPT.replaceAll("&", FORMATTING_CODE));
          TeleportUtils.teleportTo(players[0], players[1]);
          TeleportationModule.activeRequests.remove(time);
          break;
        }
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TPAACCEPT;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
