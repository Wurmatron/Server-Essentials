package com.wurmcraft.serveressentials.forge.modules.general.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.player.TPARequest;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Tpaccept")
public class TPAAcceptCommand {

  @Command(inputArguments =  {})
  public void accept(ICommandSender sender) {
    if(sender != null && sender.getCommandSenderEntity() != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      try {
        TPARequest request = (TPARequest) SERegistry.getTempData(DataKey.TPA, player.getGameProfile().getId().toString());
        player.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_TPACCEPT_ACCEPT.replaceAll("%PLAYER%",COMMAND_INFO_COLOR + request.sendingPlayer.getDisplayNameString())), request.sendingPlayer));
        request.sendingPlayer.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR + PlayerUtils.getUserLanguage(request.sendingPlayer).GENERAL_TPACCEPT_SEND.replaceAll("%PLAYER%",COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
        TeleportUtils.teleportTo(request.sendingPlayer,new LocationWrapper(request.requestedPlayer.posX,request.requestedPlayer.posY, request.requestedPlayer.posZ,request.requestedPlayer.dimension));
        SERegistry.delTempData(DataKey.TPA, request.getID());
      } catch (NoSuchElementException e) {
        sender.sendMessage(new TextComponentString(PlayerUtils.getUserLanguage(sender).GENERAL_TPAACCEPT_NOT_FOUND));
      }
    }
  }
}
