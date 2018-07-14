package com.wurmcraft.serveressentials.common.general.commands.info;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;

// TODO Rework Command
@Command(moduleName = "General")
public class ListCommand extends SECommand {

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
    List<UUID> pList = new ArrayList<>();
    for (EntityPlayerMP player : players) {
      pList.add(player.getGameProfile().getId());
    }
    sender.sendMessage(
        new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", "\u00A7")));
    for (UUID name : pList) {
      Rank userRank = getPlayerRank(name);
      sender.sendMessage(
          new TextComponentString(
              TextFormatting.AQUA
                  + UsernameCache.getLastKnownUsername(name)
                  + ": "
                  + userRank.getName()));
    }
    sender.sendMessage(
        new TextComponentString(getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", "\u00A7")));
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  private static Rank getPlayerRank(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      GlobalUser global = (GlobalUser) UserManager.getPlayerData(uuid)[0];
      return UserManager.getRank(global.getRank());
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return data.getRank();
    }
    return null;
  }
}
