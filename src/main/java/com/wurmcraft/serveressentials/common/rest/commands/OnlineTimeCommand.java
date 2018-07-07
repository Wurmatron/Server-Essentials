package com.wurmcraft.serveressentials.common.rest.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.time.DurationFormatUtils;

@Command(moduleName = "Rest")
public class OnlineTimeCommand extends SECommand {

  @Override
  public String getName() {
    return "onlineTime";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      if (args.length == 1) {
        EntityPlayer player = UsernameResolver.getPlayer(args[0]);
        if (player != null) {
          GlobalUser global =
              (GlobalUser) UserManager.getPlayerData(player.getGameProfile().getId())[0];
          LocalUser local =
              (LocalUser) UserManager.getPlayerData(player.getGameProfile().getId())[1];
          sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER));
          String formattedGlobal =
              DurationFormatUtils.formatDuration(global.getOnlineTime() * 60000, "d%:H$:m#:s@")
                  .replace('%', 'D')
                  .replace('$', 'H')
                  .replace('#', 'M')
                  .replace('@', 'S')
                  .replaceAll(":", ", ");
          String formattedLocal =
              DurationFormatUtils.formatDuration(local.getOnlineTime() * 60000, "d%:H$:m#:s@")
                  .replace('%', 'D')
                  .replace('$', 'H')
                  .replace('#', 'M')
                  .replace('@', 'S')
                  .replaceAll(":", ", ");
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).GLOBAL
                      + ": "
                      + TextFormatting.AQUA
                      + formattedGlobal));
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).LOCAL + ": " + TextFormatting.AQUA + formattedLocal));
          sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER));
        } else {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    } else {
      if (args.length == 1) {
        EntityPlayer player = UsernameResolver.getPlayer(args[0]);
        if (player != null) {
          PlayerData data =
              (PlayerData) UserManager.getPlayerData(player.getGameProfile().getId())[0];
          String formattedLocal =
              DurationFormatUtils.formatDuration(data.getOnlineTime() * 60000, "d%:H$:m#:s@")
                  .replace('%', 'D')
                  .replace('$', 'H')
                  .replace('#', 'M')
                  .replace('@', 'S')
                  .replaceAll(":", ", ");
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).LOCAL + ": " + TextFormatting.AQUA + formattedLocal));
        } else {
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
        }
      } else {
        sender.sendMessage(new TextComponentString(getUsage(sender)));
      }
    }
  }
}
