package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@Command(moduleName = "General")
public class DPFCommand extends SECommand {

  @Override
  public String getName() {
    return "deletePlayerFile";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      EntityPlayer player = UsernameResolver.getPlayer(args[0]);
      setLastLocation(
          player.getGameProfile().getId(),
          new LocationWrapper(player.getPosition(), player.dimension));
      player.onKillCommand();
      ((EntityPlayerMP) player)
          .connection.disconnect(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                      .PLAYER_FILE_DELETE));
      File playerFile =
          new File(
              server.getDataDirectory(),
              File.separator
                  + server.getFolderName()
                  + File.separator
                  + "playerdata"
                  + File.separator
                  + player.getGameProfile().getId().toString()
                  + ".dat");
      ServerEssentialsServer.LOGGER.info(
          "Deleting " + player.getDisplayNameString() + "'s player file");
      try {
        Files.delete(playerFile.toPath());
      } catch (IOException e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      ChatHelper.sendMessage(
          sender,
          getCurrentLanguage(sender)
              .PLAYER_FILE_DELETE_OTHER
              .replaceAll("%PLAYER%", player.getDisplayNameString()));
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  private static void setLastLocation(UUID uuid, LocationWrapper location) {
    if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      data.setLastLocation(location);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    } else if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(uuid)[1];
      user.setLastLocation(location);
      DataHelper.forceSave(Keys.LOCAL_USER, user);
    }
  }
}
