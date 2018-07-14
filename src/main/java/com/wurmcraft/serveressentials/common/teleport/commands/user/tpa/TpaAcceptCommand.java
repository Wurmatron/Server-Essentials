package com.wurmcraft.serveressentials.common.teleport.commands.user.tpa;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.teleport.TeleportationModule;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
          player.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).TPA_ACCEPT.replaceAll("&", "\u00A7")));
          players[0].sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).TPA_ACCEPT.replaceAll("&", "\u00A7")));
          TeleportUtils.teleportTo(players[0], players[1]);
          TeleportationModule.activeRequests.remove(time);
          break;
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
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
}
