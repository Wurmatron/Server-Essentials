package com.wurmcraft.serveressentials.forge.modules.language.command;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "language", name = "Nick", aliases = {"NickName"})
public class NickCommand {

  @Command(inputArguments = {CommandArguments.PLAYER,
      CommandArguments.STRING}, inputNames = {"Player", "Nickname"})
  public void nickname(ICommandSender sender, EntityPlayer player, String name) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.nick.other") || !SERegistry
        .isModuleLoaded("Rank")) {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        playerData.server.nick = name;
        SERegistry.register(DataKey.PLAYER, playerData);
        player.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(player).LANGUAGE_NICK
                .replaceAll("%NICK%", COMMAND_INFO_COLOR + name + COMMAND_COLOR)));
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).LANGUAGE_NICK_OTHER
                .replaceAll("%PLAYER%",
                    COMMAND_INFO_COLOR + player.getDisplayNameString() + COMMAND_COLOR)
                .replaceAll("%NICK%", COMMAND_INFO_COLOR + name + COMMAND_COLOR)));
      } catch (NoSuchElementException ignored) {

      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

  @Command(inputArguments = {CommandArguments.STRING})
  public void nick(ICommandSender sender, String name) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.nick") || !SERegistry
        .isModuleLoaded("Rank")) {
      if (sender != null && sender.getCommandSenderEntity() instanceof EntityPlayer) {
        try {
          EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
          StoredPlayer playerData = (StoredPlayer) SERegistry
              .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
          playerData.server.nick = name;
          SERegistry.register(DataKey.PLAYER, playerData);
          player.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(player).LANGUAGE_NICK
                  .replaceAll("%NICK%", COMMAND_INFO_COLOR + name + COMMAND_COLOR)));
        } catch (NoSuchElementException ignored) {
        }
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }
}
