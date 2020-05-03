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
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.common.utils.TextComponentUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "Language", name = "Mute", aliases = "M")
public class MuteCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void mutePlayer(ICommandSender sender, EntityPlayer player) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "language.mute") || !SERegistry
        .isModuleLoaded("Rank")) {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        if (playerData != null) {
          playerData.global = RestRequestGenerator.User
              .getPlayer(player.getGameProfile().getId().toString());
          playerData.global.muted = !playerData.global.muted;
          if (playerData.global.muted) {
            sender
                .sendMessage(
                    TextComponentUtils
                        .addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                                PlayerUtils.getUserLanguage(sender).LANGUAGE_MUTE_SENDER
                                    .replaceAll("%PLAYER%",
                                        COMMAND_INFO_COLOR + player.getDisplayNameString())),
                            player));
            player.sendMessage(
                new TextComponentString(ERROR_COLOR +
                    PlayerUtils.getUserLanguage(player).LANGUAGE_MUTE));
          } else {
            sender
                .sendMessage(
                    TextComponentUtils
                        .addPlayerComponent(new TextComponentString(COMMAND_COLOR +
                                PlayerUtils
                                    .getUserLanguage(sender).LANGUAGE_MUTE_UNMUTE_SENDER
                                    .replaceAll("%PLAYER%",
                                        COMMAND_INFO_COLOR + player.getDisplayNameString())),
                            player));
            player.sendMessage(new TextComponentString(COMMAND_COLOR +
                PlayerUtils.getUserLanguage(player).LANGUAGE_MUTE_UNMUTE));
          }
          if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
            RestRequestGenerator.User
                .overridePlayer(player.getGameProfile().getId().toString(),
                    playerData.global);
          }
        }
      } catch (NoSuchElementException ignored) {
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
