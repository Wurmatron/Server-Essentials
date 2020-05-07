package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.modules.general.command.teleport.TPACommand.tpaTimeout;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.api.player.TPAHereRequest;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "TPAHere")
public class TPAHereCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void tpaHere(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpahere") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer sendingPlayer = (EntityPlayer) sender.getCommandSenderEntity();
        SERegistry.addTempData(DataKey.TPA, new TPAHereRequest(sendingPlayer, player),
            tpaTimeout * 1000);
        sender.sendMessage(
            TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).GENERAL_TPAHERE_REQUEST
                    .replaceAll("%PLAYER%",
                        COMMAND_INFO_COLOR + player.getDisplayNameString()
                            + COMMAND_COLOR)), player));
        player.sendMessage(
            TextComponentUtils.addClickCommand(TextComponentUtils.addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                    PlayerUtils.getUserLanguage(player).GENERAL_TPAHERE
                        .replaceAll("%PLAYER%",
                            COMMAND_INFO_COLOR + sendingPlayer.getDisplayNameString()
                                + COMMAND_COLOR)),
                sendingPlayer), "/tpaccept"));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
