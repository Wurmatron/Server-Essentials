package com.wurmcraft.serveressentials.forge.modules.general.command.teleport;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.api.player.TPARequest;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.core.api.module.config.GeneralConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Tpa")
public class TPACommand {

  public static int tpaTimeout = ((GeneralConfig) SERegistry
      .getStoredData(DataKey.MODULE_CONFIG, "General")).tpaTimeout;

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void teleportTo(ICommandSender sender, EntityPlayer otherPlayer) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.tpa") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (otherPlayer != null && sender
          .getCommandSenderEntity() instanceof EntityPlayer) {
        SERegistry.addTempData(DataKey.TPA, new TPARequest(
            (EntityPlayer) sender.getCommandSenderEntity(), otherPlayer), tpaTimeout * 1000);
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_TPA_SENT
                    .replaceAll("%PLAYER%",
                        COMMAND_INFO_COLOR + otherPlayer.getDisplayNameString() + COMMAND_COLOR)),
            otherPlayer));
        otherPlayer.sendMessage(TextComponentUtils.addClickCommand(TextComponentUtils.addPlayerComponent(
            new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(otherPlayer).GENERAL_TPA_REQUEST
                    .replaceAll("%PLAYER%",
                        COMMAND_INFO_COLOR + player.getDisplayNameString()+ COMMAND_COLOR)), player), "/tpaccept"));
      } else {
        sender.sendMessage(new TextComponentString(
            ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_PLAYER_NOT_FOUND));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
