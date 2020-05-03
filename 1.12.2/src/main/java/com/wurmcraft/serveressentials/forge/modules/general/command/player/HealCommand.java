package com.wurmcraft.serveressentials.forge.modules.general.command.player;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils.getUserLanguage;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "Heal")
public class HealCommand {

  @Command(inputArguments = {})
  public void healSelf(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.heal") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() != null) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        player.setHealth(player.getMaxHealth());
        sender.sendMessage(
            new TextComponentString(
                COMMAND_COLOR + getUserLanguage(player).GENERAL_HEAL));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void healOther(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.heal.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      player.setHealth(player.getMaxHealth());
      sender.sendMessage(new TextComponentString(
          COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_HEAL_OTHER
              .replaceAll("%PLAYER%",
                  COMMAND_INFO_COLOR + player.getDisplayNameString())));
      player.sendMessage(
          new TextComponentString(COMMAND_COLOR + getUserLanguage(player).GENERAL_HEAL));
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
