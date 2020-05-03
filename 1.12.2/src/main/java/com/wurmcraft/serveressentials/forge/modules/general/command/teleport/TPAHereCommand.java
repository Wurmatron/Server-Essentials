package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.general.command.teleport.TPACommand.tpaTimeout;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.player.TPAHereRequest;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "TPAHere")
public class TPAHereCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void tpaHere(ICommandSender sender, EntityPlayer player) {
    if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
      TPAHereRequest request = new TPAHereRequest(player, sendingPlayer);
      SERegistry.addTempData(DataKey.TPA, request, tpaTimeout * 1000);
      sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
          PlayerUtils.getUserLanguage(player).GENERAL_TPAHERE_REQUEST.replaceAll("%PLAYER%", COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
      player.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(PlayerUtils.getUserLanguage(player).GENERAL_TPAHERE.replaceAll("%PLAYER%", sendingPlayer.getDisplayNameString())), sendingPlayer));
    }
  }

}
