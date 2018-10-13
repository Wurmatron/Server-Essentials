package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

// TODO Rework Command
@Command(moduleName = "Chat")
public class MuteCommand extends SECommand {

  @Override
  public String getName() {
    return "mute";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = UsernameResolver.getPlayer(args[0]);
    if (player != null) {
      if (getMuted(player.getGameProfile().getId())) {
        setMute(player.getGameProfile().getId(), false);
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .UNMUTED_OTHER
                .replaceAll("%PLAYER%", player.getDisplayNameString()));
        ChatHelper.sendMessage(
            player, LanguageModule.getLangfromUUID(player.getGameProfile().getId()).UNMUTED);
      } else {
        setMute(player.getGameProfile().getId(), true);
        ChatHelper.sendMessage(
            sender,
            getCurrentLanguage(sender)
                .MUTED_OTHER
                .replaceAll("%PLAYER%", player.getDisplayNameString()));
        ChatHelper.sendMessage(
            player, LanguageModule.getLangfromUUID(player.getGameProfile().getId()).MUTED);
      }
    } else {
      if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
        GlobalUser user = forceUserFromUUID(UsernameResolver.getUUIDFromName(args[0]));
        if (user != null) {
          if (user.isMuted()) {
            user.setMuted(false);
            RequestHelper.UserResponses.overridePlayerData(user);
            ChatHelper.sendMessage(
                sender,
                getCurrentLanguage(sender)
                    .UNMUTED_OTHER
                    .replaceAll("%PLAYER%", player.getDisplayNameString()));
          } else {
            user.setMuted(true);
            RequestHelper.UserResponses.overridePlayerData(user);
            ChatHelper.sendMessage(
                sender,
                getCurrentLanguage(sender)
                    .MUTED_OTHER
                    .replaceAll("%PLAYER%", player.getDisplayNameString()));
          }
        } else {
          ChatHelper.sendMessage(
              sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
        }
      } else {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0]));
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  private static void setMute(UUID uuid, boolean mute) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      user.setMuted(mute);
      RequestHelper.UserResponses.overridePlayerData(user);
      UserManager.playerData.put(
          uuid, new Object[] {user, DataHelper.get(Keys.LOCAL_USER, uuid.toString())});
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      data.setMuted(mute);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    }
  }

  public static boolean getMuted(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      return user.isMuted();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return data.isMuted();
    }
    return false;
  }
}
