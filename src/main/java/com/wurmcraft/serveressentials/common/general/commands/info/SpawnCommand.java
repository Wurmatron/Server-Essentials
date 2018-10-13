package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class SpawnCommand extends SECommand {

  @Override
  public String getName() {
    return "spawn";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (DataHelper.globalSettings.getSpawn() != null
        && DataHelper.globalSettings.getSpawn().location != null) {
      TeleportUtils.teleportTo(
          (EntityPlayerMP) player, DataHelper.globalSettings.getSpawn().location, true);
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .TP_HOME
              .replaceAll("%HOME%", getCurrentLanguage(sender).SPAWN));
    } else {
      TeleportUtils.teleportTo(
          (EntityPlayerMP) player,
          new LocationWrapper(server.getWorld(0).getSpawnPoint(), player.dimension),
          true);
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .TP_HOME
              .replaceAll("%HOME%", getCurrentLanguage(sender).SPAWN));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/spawn";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_SPAWN.replaceAll("&", FORMATTING_CODE);
  }
}
