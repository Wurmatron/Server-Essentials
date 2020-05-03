package com.wurmcraft.serveressentials.forge.modules.general.command.info;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.ERROR_COLOR;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

@ModuleCommand(moduleName = "General", name = "LastSeen", aliases = {"Seen"})
public class LastSeenCommand {

  @Command(inputArguments = {CommandArguments.PLAYER}, inputNames = {"Player"})
  public void checkOnlinePlayer(ICommandSender sender, EntityPlayer player) {
    checkOfflinePlayer(sender, player.getDisplayNameString());
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"Player"})
  public void checkOfflinePlayer(ICommandSender sender, String playerName) {
    if (SERegistry.isModuleLoaded("Rank") && RankUtils
        .hasPermission(RankUtils.getRank(sender), "general.lastseen") || !SERegistry
        .isModuleLoaded("Rank")) {
      UUID playerUUID = PlayerUtils.getPlayer(playerName);
      if (playerUUID != null) {
        GlobalPlayer playerData = null;
        if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
          playerData = RestRequestGenerator.User.getPlayer(playerUUID.toString());
        } else {
          try {
            playerData = (((StoredPlayer) SERegistry
                .getStoredData(DataKey.PLAYER, playerUUID.toString())).global);
          } catch (NoSuchElementException e) {
            sender.sendMessage(new TextComponentString(
                COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_PLAYER_NOT_FOUND
                    .replaceAll("%PLAYER%", SECommand.COMMAND_INFO_COLOR + playerName)));
          }
        }
        if (playerData != null) {
          Date date = new Date(playerData.lastSeen * 1000);
          DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).GENERAL_LASTSEEN
                  .replaceAll("%PLAYER%", COMMAND_INFO_COLOR + playerName)
                  .replaceAll("%DATE%", COMMAND_INFO_COLOR + df.format(date))));
        } else {
          sender.sendMessage(new TextComponentString(
              COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_PLAYER_NOT_FOUND
                  .replaceAll("%PLAYER%", SECommand.COMMAND_INFO_COLOR + playerName)));
        }
      } else {
        sender.sendMessage(new TextComponentString(
            COMMAND_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_PLAYER_NOT_FOUND
                .replaceAll("%PLAYER%", SECommand.COMMAND_INFO_COLOR + playerName)));
      }
    } else {
      sender.sendMessage(new TextComponentString(
          ERROR_COLOR + PlayerUtils.getUserLanguage(sender).ERROR_NO_PERMS));
    }
  }

}
