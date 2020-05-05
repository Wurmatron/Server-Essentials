package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TeleportUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "TPHere")
public class TPHereCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void tpHere(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.jump") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
        TeleportUtils.teleportTo(player,
            new LocationWrapper(sendingPlayer.posX, sendingPlayer.posY,
                sendingPlayer.posZ,
                sendingPlayer.dimension));
        sendingPlayer.sendMessage(
            TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(sendingPlayer).GENERAL_TPHERE_OTHER
                    .replaceAll("%PLAYER%",
                        COMMAND_INFO_COLOR + player.getDisplayNameString())), player));
        player.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sendingPlayer).GENERAL_TPHERE
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + sendingPlayer.getDisplayNameString())));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
