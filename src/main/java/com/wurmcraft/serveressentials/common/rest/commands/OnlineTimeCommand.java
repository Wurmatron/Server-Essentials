package com.wurmcraft.serveressentials.common.rest.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
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
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
          String formattedGlobal =
              DurationFormatUtils.formatDuration(
                      (long)
                          (global.getServerData(ConfigHandler.serverName).getOnlineTime() * 60000),
                      "d%:H$:m#:s@")
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
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender).GLOBAL + ": " + TextFormatting.AQUA + formattedGlobal);
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender).LOCAL
                  + ": "
                  + TextFormatting.AQUA
                  + formattedLocal.replaceAll("&", FORMATTING_CODE));
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE));
        } else {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
        }
      } else {
        ChatHelper.sendMessage(sender, getUsage(sender));
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
          ChatHelper.sendMessage(
              sender,
              getCurrentLanguage(sender).LOCAL
                  + ": "
                  + TextFormatting.AQUA
                  + formattedLocal.replaceAll("&", FORMATTING_CODE));
        } else {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
        }
      } else {
        ChatHelper.sendMessage(sender, getUsage(sender));
      }
    }
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("online");
    alts.add("time");
    alts.add("ot");
    return alts;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/onlineTime \u00A7b<player>";
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_ONLINETIME.replaceAll("&", FORMATTING_CODE);
  }
}
