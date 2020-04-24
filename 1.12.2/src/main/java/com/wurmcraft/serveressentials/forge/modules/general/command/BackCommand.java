package com.wurmcraft.serveressentials.forge.modules.general.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General",name = "Back")
public class BackCommand {

  @Command(inputArguments = {})
  public void sendBack(ICommandSender sender) {
    if(sender != null & sender.getCommandSenderEntity() != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      StoredPlayer playerData = (StoredPlayer) SERegistry.getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      TeleportUtils.teleportTo(player,playerData.server.lastLocation);
      sender.sendMessage(new TextComponentString(PlayerUtils.getUserLanguage(sender).GENERAL_BACK_SENT));
    }
  }

}
