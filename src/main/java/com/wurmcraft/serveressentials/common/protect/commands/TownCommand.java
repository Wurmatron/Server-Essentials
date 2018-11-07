package com.wurmcraft.serveressentials.common.protect.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import com.wurmcraft.serveressentials.api.protection.ClaimedArea;
import com.wurmcraft.serveressentials.api.protection.Town;
import com.wurmcraft.serveressentials.common.chat.ChatHelper;
import com.wurmcraft.serveressentials.common.protect.ProtectionModule;
import com.wurmcraft.serveressentials.common.utils.SECommand;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;

@Command(moduleName = "Protection")
public class TownCommand extends SECommand {

  @Override
  public String getName() {
    return "town";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/town info|create <name>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
  }

  @Override
  public boolean canConsoleRun() {
    return super.canConsoleRun();
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_TOWN;
  }

  @SubCommand
  public void create(ICommandSender sender, String[] args) {
    if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      ProtectionModule.createTown(new Town(args[0], player.getGameProfile().getId().toString()));
      ChatHelper.sendMessage(
          sender, getCurrentLanguage(sender).PROTECT_TOWN_CREATE.replaceAll("%TOWN%", args[0]));
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @SubCommand
  public void info(ICommandSender sender, String[] args) {
    if (args.length == 1) {
      if (ProtectionModule.doesTownNameExist(args[0])) {
        Town town = ProtectionModule.getTownFromName(args[0]).town;
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
        try {
          ChatHelper.sendMessage(
              sender,
              Objects.requireNonNull(
                  ((TextFormatting.AQUA
                              + town.getID()
                              + " "
                              + UsernameCache.getLastKnownUsername(
                                  UUID.fromString(town.getOwnerID())))
                          != null)
                      ? UsernameCache.getLastKnownUsername(UUID.fromString(town.getOwnerID()))
                      : town.getOwnerID()));
        } catch (Exception e) {
          e.printStackTrace();
        }
        ChatHelper.sendMessage(sender, getCurrentLanguage(sender).CHAT_SPACER);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }

  @SubCommand
  public void claim(ICommandSender sender, String[] args) {
    if (args.length == 1) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      try {
        int size = Integer.parseInt(args[0]);
        Town town = ProtectionModule.getTownFromOwner(player.getGameProfile().getId().toString());
        ClaimedArea area =
            new ClaimedArea(
                new LocationWrapper(
                    player.getPosition().add(-size, -size, -size), player.dimension),
                new LocationWrapper(player.getPosition().add(size, size, size), player.dimension));
        town.addClaimedArea(area);
        ProtectionModule.updateTown(town);
      } catch (NumberFormatException e) {
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(sender));
    }
  }
}
