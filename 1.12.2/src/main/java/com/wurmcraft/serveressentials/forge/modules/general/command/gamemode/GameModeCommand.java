package com.wurmcraft.serveressentials.forge.modules.general.command.gamemode;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;
import static com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils.getGamemode;

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
import net.minecraft.world.GameType;

@ModuleCommand(moduleName = "General", name = "GameMode")
public class GameModeCommand {

  @Command(inputArguments = {CommandArguments.INTEGER}, inputNames = {"Mode"})
  public void changeMode(ICommandSender sender, int mode) {
    if (mode == 0) {
      changeMode(sender, "Survival");
    } else if (mode == 1) {
      changeMode(sender, "Creative");
    } else if (mode == 2) {
      changeMode(sender, "Adventure");
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Mode"})
  public void changeMode(ICommandSender sender, String mode) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.gamemode") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        GameType type = getGamemode(mode);
        if (type != null) {
          if (RankUtils.hasPermission(RankUtils.getRank(player),
              "general.gamemode." + type.name())) {
            player.setGameType(type);
            player.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).GENERAL_GAMEMODE_CHANGED
                    .replaceAll("%MODE%", COMMAND_INFO_COLOR + type.name())));
          } else {
            sender.sendMessage(new TextComponentString(
                ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
          }
        } else {
          player.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
              .getUserLanguage(player).GENERAL_GAMEMODE_NOT_FOUND
              .replaceAll("%MODE%", COMMAND_INFO_COLOR + mode)));
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.INTEGER,
      CommandArguments.PLAYER}, inputNames = {"Mode", "Player"})
  public void changeMode(ICommandSender sender, int mode, EntityPlayer player) {
    if (mode == 0) {
      changeMode(sender, "Survival", player);
    } else if (mode == 1) {
      changeMode(sender, "Creative", player);
    } else if (mode == 2) {
      changeMode(sender, "Adventure", player);
    }
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.PLAYER}, inputNames = {"Mode", "Player"})
  public void changeMode(ICommandSender sender, String mode, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.gamemode") || !SERegistry
        .isModuleLoaded("Rank")) {
      GameType type = getGamemode(mode);
      if (type != null) {
        if (RankUtils.hasPermission(RankUtils.getRank(sender),
            "general.gamemode." + type.name())) {
          player.setGameType(type);
          player.sendMessage(new TextComponentString(COMMAND_COLOR +
              PlayerUtils.getUserLanguage(player).GENERAL_GAMEMODE_CHANGED
                  .replaceAll("%MODE%", COMMAND_INFO_COLOR + type.name())));
          sender.sendMessage(TextComponentUtils.addPlayerComponent(
              new TextComponentString(COMMAND_COLOR + PlayerUtils
                  .getUserLanguage(sender).GENERAL_GAMEMODE_CHANGED_SENDER
                  .replaceAll("%PLAYER%",
                      COMMAND_INFO_COLOR + player.getDisplayNameString())
                  .replaceAll("%MODE%", COMMAND_INFO_COLOR + type.name())), player));
        } else {
          sender.sendMessage(new TextComponentString(
              ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
        }
      } else {
        sender.sendMessage(new TextComponentString(COMMAND_COLOR + PlayerUtils
            .getUserLanguage(sender).GENERAL_GAMEMODE_NOT_FOUND
            .replaceAll("%MODE%", COMMAND_INFO_COLOR + mode)));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
