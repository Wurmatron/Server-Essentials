package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.DataHelper;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@Command(moduleName = "Teleportation")
public class DelHomeCommand extends SECommand {

  @Override
  public String getName() {
    return "delHome";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    if (args.length == 1) {
      String homeName = args[0];
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (deleteHome(player.getGameProfile().getId(), homeName)) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).HOME_DELETED.replaceAll("%HOME%", homeName));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  private static boolean deleteHome(UUID uuid, String name) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(uuid)[1];
      user.delHome(name);
      DataHelper.forceSave(Keys.LOCAL_USER, user);
      UserManager.PLAYER_DATA.put(uuid, new Object[] {forceUserFromUUID(uuid), user});
      return true;
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      data.delHome(name);
      DataHelper.forceSave(Keys.PLAYER_DATA, data);
    }
    return false;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    List<String> predictions = new ArrayList<>();
    Home[] homes =
        HomeCommand.getPlayerHomes(
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
    if (args.length > 0 && !args[0].isEmpty()) {
      for (Home home : homes) {
        if (home.getName().length() > args[0].length()
            && home.getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) {
          predictions.add(home.getName());
        }
      }
    }
    if (predictions.isEmpty()) {
      for (Home home : homes) {
        predictions.add(home.getName());
      }
    }
    return predictions;
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("deleteHome");
    alts.add("removeHome");
    alts.add("remHome");
    return alts;
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_DELHOME.replaceAll("&", FORMATTING_CODE);
  }
}
