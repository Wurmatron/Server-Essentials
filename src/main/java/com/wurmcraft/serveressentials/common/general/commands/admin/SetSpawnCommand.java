package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.global.SpawnPoint;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@Command(moduleName = "General")
public class SetSpawnCommand extends SECommand {

  @Override
  public String getName() {
    return "setSpawn";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    DataHelper.globalSettings.setSpawn(
        new SpawnPoint(
            new LocationWrapper(player.getPosition(), player.dimension),
            player.rotationYaw,
            player.rotationPitch));
    player.world.setSpawnPoint(player.getPosition());
    ChatHelper.sendMessage(sender, getCurrentLanguage(sender).SPAWN_SET);
  }
}
