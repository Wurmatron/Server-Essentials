package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Command(moduleName = "Teleportation")
public class HomeCommand extends SECommand {

  public static Home[] getPlayerHomes(UUID uuid) {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      LocalUser user = (LocalUser) UserManager.getPlayerData(uuid)[1];
      return user.getHomes();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      PlayerData data = (PlayerData) UserManager.getPlayerData(uuid)[0];
      return data.getHomes();
    }
    return new Home[0];
  }

  @Override
  public String getName() {
    return "home";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
    if (args.length == 1 && !args[0].equalsIgnoreCase("list")) {
      Home[] homes = getPlayerHomes(player.getGameProfile().getId());
      for (Home home : homes) {
        if (home.getName().equalsIgnoreCase(args[0])) {
          TeleportUtils.teleportTo((EntityPlayerMP) player, home.getPos(), true);
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).TP_HOME.replaceAll("%HOME%", home.getName())));
          return;
        }
      }
    } else if (args.length == 0) {
      Home[] homes = getPlayerHomes(player.getGameProfile().getId());
      for (Home home : homes) {
        if (home.getName().equalsIgnoreCase(ConfigHandler.defaultHome)) {
          TeleportUtils.teleportTo((EntityPlayerMP) player, home.getPos(), true);
          sender.sendMessage(
              new TextComponentString(
                  getCurrentLanguage(sender).TP_HOME.replaceAll("%HOME%", home.getName())));
          return;
        }
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return false;
  }

  @SubCommand
  public void list(ICommandSender sender, String[] args) {
    Home[] homes =
        HomeCommand.getPlayerHomes(
            ((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
    StringBuilder builder = new StringBuilder();
    for (int index = 0; index < homes.length; index++) {
      builder.append(TextFormatting.GOLD + homes[index].getName());
      if (index != homes.length - 1) {
        builder.append(TextFormatting.GRAY + ", " + TextFormatting.GOLD);
      }
    }
    sender.sendMessage(new TextComponentString(builder.toString()));
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }
}
