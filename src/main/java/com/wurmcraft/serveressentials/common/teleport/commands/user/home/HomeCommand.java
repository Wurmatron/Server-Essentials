package com.wurmcraft.serveressentials.common.teleport.commands.user.home;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.file.PlayerData;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.teleport.utils.TeleportUtils;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;

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
          sender.sendMessage(formatHome(sender, home));
          return;
        }
      }
    } else if (args.length == 0) {
      Home[] homes = getPlayerHomes(player.getGameProfile().getId());
      for (Home home : homes) {
        if (home.getName().equalsIgnoreCase(ConfigHandler.defaultHome)) {
          TeleportUtils.teleportTo((EntityPlayerMP) player, home.getPos(), true);
          sender.sendMessage(formatHome(sender, home));
          return;
        }
        TeleportUtils.teleportTo((EntityPlayerMP) player, homes[0].getPos(), true);
        sender.sendMessage(formatHome(sender, homes[0]));
      }
      if (homes.length == 0) {
        ChatHelper.sendMessage(
            sender, getCurrentLanguage(sender).HOME_NOTSET.replaceAll("&", FORMATTING_CODE));
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
        getPlayerHomes(((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
    sender.sendMessage(
        new TextComponentString(
            getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE)));
    for (Home home : homes) {
      TextComponentString msg =
          new TextComponentString(
              getCurrentLanguage(sender)
                  .HOME_LIST
                  .replaceAll("%HOME%", home.getName())
                  .replaceAll("&", FORMATTING_CODE));
      msg.getStyle()
          .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home.getName()));
      sender.sendMessage(msg);
    }
    ChatHelper.sendMessage(
        sender, getCurrentLanguage(sender).CHAT_SPACER.replaceAll("&", FORMATTING_CODE));
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public List<String> getAltNames() {
    List<String> alts = new ArrayList<>();
    alts.add("h");
    return alts;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "\u00A79/home \u00A7b<name>";
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    List<String> predictions = new ArrayList<>();
    Home[] homes =
        getPlayerHomes(((EntityPlayer) sender.getCommandSenderEntity()).getGameProfile().getId());
    if (args.length > 0 && !args[0].isEmpty()) {
      for (Home home : homes) {
        if (home.getName().length() > args[0].length()
            && home.getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) {
          predictions.add(home.getName());
        }
      }
    }
    if (predictions.size() == 0) {
      for (Home home : homes) {
        predictions.add(home.getName());
      }
    }
    return predictions;
  }

  private static TextComponentString formatHome(ICommandSender sender, Home home) {
    TextComponentString msg =
        new TextComponentString(
            getCurrentLanguage(sender)
                .TP_HOME
                .replaceAll("%HOME%", home.getName())
                .replaceAll("&", FORMATTING_CODE));
    msg.getStyle()
        .setHoverEvent(
            new HoverEvent(
                Action.SHOW_TEXT,
                new TextComponentString(
                    getCurrentLanguage(sender)
                        .HOME_HOVER
                        .replaceAll("%X%", String.valueOf(home.getPos().getX()))
                        .replaceAll("%Y%", String.valueOf(home.getPos().getY()))
                        .replaceAll("%Z%", String.valueOf(home.getPos().getZ()))
                        .replaceAll("%DIMENSION%", String.valueOf(home.getPos().getDim()))
                        .replaceAll("&", FORMATTING_CODE))));
    return msg;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_HOME.replaceAll("&", FORMATTING_CODE);
  }
}
