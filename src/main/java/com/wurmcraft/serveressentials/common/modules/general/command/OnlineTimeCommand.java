package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.time.DurationFormatUtils;

@ModuleCommand(moduleName = "General")
public class OnlineTimeCommand extends Command {

  @Override
  public String getName() {
    return "OnlineTime";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/onlineTime <user>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_ONLINETIME;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      displayOnlineTime((EntityPlayer) sender.getCommandSenderEntity());
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
    } else if (args.length == 1) {
      EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
      if (player != null) {
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
        displayOnlineTime(player);
        ChatHelper.sendMessage(sender, senderLang.local.CHAT_SPACER);
      } else {
        ChatHelper.sendMessage(
            sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private void displayOnlineTime(EntityPlayer player) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      String formattedGlobal =
          DurationFormatUtils.formatDuration(
                  (long)
                      (((GlobalRestUser) UserManager.getUserData(player)[0])
                              .getServerData(ConfigHandler.serverName)
                              .getOnlineTime()
                          * 60000),
                  "d%:H$:m#:s@")
              .replace('%', 'D')
              .replace('$', 'H')
              .replace('#', 'M')
              .replace('@', 'S')
              .replaceAll(":", ", ");
      String formattedLocal =
          DurationFormatUtils.formatDuration(
                  ((LocalRestUser) UserManager.getUserData(player)[1]).getOnlineTime() * 60000,
                  "d%:H$:m#:s@")
              .replace('%', 'D')
              .replace('$', 'H')
              .replace('#', 'M')
              .replace('@', 'S')
              .replaceAll(":", ", ");
      ChatHelper.sendMessage(
          player,
          LanguageModule.getUserLanguage(player)
              .local
              .REST_OT_GLOBAL
              .replaceAll(Replacment.TIME, formattedGlobal));
      ChatHelper.sendMessage(
          player,
          LanguageModule.getUserLanguage(player)
              .local
              .REST_OT_LOCAL
              .replaceAll(Replacment.TIME, formattedLocal));
    } else {
      String formattedLocal =
          DurationFormatUtils.formatDuration(
                  ((FileUser) UserManager.getUserData(player)[0]).getOnlineTime() * 60000,
                  "d%:H$:m#:s@")
              .replace('%', 'D')
              .replace('$', 'H')
              .replace('#', 'M')
              .replace('@', 'S')
              .replaceAll(":", ", ");
      ChatHelper.sendMessage(
          player,
          LanguageModule.getUserLanguage(player)
              .local
              .REST_OT_LOCAL
              .replaceAll(Replacment.TIME, formattedLocal));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("onlineTime");
    aliases.add("Time");
    aliases.add("T");
    aliases.add("OnlineT");
    return aliases;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    return CommandUtils.predictUsernames(args, 0);
  }
}
