package com.wurmcraft.serveressentials.common.teleport.commands.user;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "Teleportation")
public class BackCommand extends SECommand {

  private static LocationWrapper getLastLocation(EntityPlayer player) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(player)[0];
      return data.getLastLocation();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser data = (LocalUser) UserManager.getPlayerData(player)[1];
      return data.getLastLocation();
    }
    return null;
  }

  @Override
  public String getName() {
    return "back";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    LocationWrapper location = getLastLocation(player);
    if (location != null && location.getY() > 0) {
      TeleportUtils.teleportTo((EntityPlayerMP) player, location, true);
      ChatHelper.sendMessage(sender, getCurrentLanguage(sender).TP_BACK.replaceAll("&", "\u00A7"));
    } else {
      ChatHelper.sendMessage(
          sender, getCurrentLanguage(sender).TP_BACK_FAIL.replaceAll("&", "\u00A7"));
    }
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/back";
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_BACK.replaceAll("&", "\u00A7");
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }
}
