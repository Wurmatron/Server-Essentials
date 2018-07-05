package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
      TeleportUtils
          .teleportTo((EntityPlayerMP) player, DataHelper.globalSettings.getSpawn().location, true);
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).TP_HOME.replaceAll("%HOME%", "Spawn")));
    } else {
      TeleportUtils
          .teleportTo((EntityPlayerMP) player,
              new LocationWrapper(server.getWorld(0).getSpawnPoint(), player.dimension), true);
      sender.sendMessage(
          new TextComponentString(
              getCurrentLanguage(sender).TP_HOME.replaceAll("%HOME%", "Overworld Spawn")));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }
}
