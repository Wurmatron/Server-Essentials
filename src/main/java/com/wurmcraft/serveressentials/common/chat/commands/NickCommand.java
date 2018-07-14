package com.wurmcraft.serveressentials.common.chat.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

// TODO Rework Command
@Command(moduleName = "Chat")
public class NickCommand extends SECommand {

  @Override
  public String getName() {
    return "nick";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1 && sender.getCommandSenderEntity() != null) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      setNick(player.getGameProfile().getId(), args[0]);
    } else if (args.length == 2) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      if (player != null) {
        setNick(player.getGameProfile().getId(), args[0]);
        sender.sendMessage(
            new TextComponentString(getCurrentLanguage(sender).NICK.replaceAll("%NICK%", args[0])));
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  private static void setNick(UUID uuid, String nick) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser user = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      user.setNick(nick);
      RequestHelper.UserResponses.overridePlayerData(user);
      UserManager.playerData.put(
          uuid, new Object[] {user, DataHelper.get(Keys.LOCAL_USER, uuid.toString())});
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      data.setNickname(nick);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
