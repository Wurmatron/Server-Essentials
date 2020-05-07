package com.wurmcraft.serveressentials.forge.modules.general.command.admin;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "God", aliases = {"G"})
public class GodCommand {

  @Command(inputArguments = {})
  public void selfGod(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.god") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (player.capabilities.disableDamage) {
          player.sendMessage(new TextComponentString( COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_GOD_UNDO));
          player.capabilities.disableDamage = false;
        } else {
          player.sendMessage(
              new TextComponentString(COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_GOD));
          player.capabilities.disableDamage = true;
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void playerGod(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.god.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (player.capabilities.disableDamage) {
        player.sendMessage(
            new TextComponentString(COMMAND_COLOR+
                PlayerUtils.getUserLanguage(player).GENERAL_GOD_UNDO));
        player.capabilities.disableDamage = false;
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_GOD_OTHER
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)), player));
      } else {
        player.sendMessage(
            new TextComponentString(COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_GOD));
        player.capabilities.disableDamage = true;
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_GOD_OTHER_UNDO
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)), player));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
