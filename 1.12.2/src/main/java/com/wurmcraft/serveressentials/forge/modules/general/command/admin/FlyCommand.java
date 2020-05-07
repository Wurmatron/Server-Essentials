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

@ModuleCommand(moduleName = "General", name = "Fly", aliases = {"F"})
public class FlyCommand {

  @Command(inputArguments = {})
  public void selfFly(ICommandSender sender) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.fly") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        if (player.capabilities.allowFlying) {
          player.sendMessage(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_FLY_UNDO));
          if (!player.isCreative()) {
            player.capabilities.allowFlying = false;
          }
          player.sendPlayerAbilities();
        } else {
          player.sendMessage(
              new TextComponentString(
                  COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_FLY));
          player.capabilities.allowFlying = true;
          player.sendPlayerAbilities();
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void playerFly(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.fly.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (player.capabilities.allowFlying) {
        player.sendMessage(
            new TextComponentString(COMMAND_COLOR+
                PlayerUtils.getUserLanguage(player).GENERAL_FLY_UNDO));
        player.capabilities.allowFlying = false;
        player.sendPlayerAbilities();
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_FLY_OTHER
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() +COMMAND_COLOR )), player));
      } else {
        player.sendMessage(
            new TextComponentString(COMMAND_COLOR + PlayerUtils.getUserLanguage(player).GENERAL_FLY));
        player.capabilities.allowFlying = true;
        player.sendPlayerAbilities();
        sender.sendMessage(TextComponentUtils.addPlayerComponent(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_FLY_OTHER_UNDO
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() +COMMAND_COLOR )), player));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
