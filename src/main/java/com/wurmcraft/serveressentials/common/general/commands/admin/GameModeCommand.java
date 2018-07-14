package com.wurmcraft.serveressentials.common.general.commands.admin;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;

// TODO Rework Command
@Command(moduleName = "General")
public class GameModeCommand extends SECommand {

  private static final String[] CREATIVE = new String[] {"Creative", "c", "1"};
  private static final String[] SURVIVAL = new String[] {"Survival", "s", "0"};
  private static final String[] ADVENTURE = new String[] {"Adventure", "a", "2"};
  private static final String[] SPECTATOR = new String[] {"Spectator", "sp", "3"};

  @Override
  public String getName() {
    return "gamemode";
  }

  @Override
  public List<String> getAltNames() {
    List<String> altNames = new ArrayList<>();
    altNames.add("gm");
    altNames.add("mode");
    return altNames;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/gamemode <mode> <user>";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      int mode = getMode(args[0]);
      if (mode != -1) {
        if (args.length > 1) {
          EntityPlayer user = UsernameResolver.getPlayer(args[1]);
          if (user != null) {
            user.setGameType(GameType.getByID(mode));
            user.sendMessage(
                new TextComponentString(
                    LanguageModule.getLangfromUUID(user.getGameProfile().getId())
                        .MODE_CHANGED
                        .replaceAll("%MODE%", GameType.getByID(mode).getName())));
            sender.sendMessage(
                new TextComponentString(
                    getCurrentLanguage(sender)
                        .MODE_CHANGED_OTHER
                        .replaceAll("%PLAYER%", user.getDisplayNameString())
                        .replaceAll("%MODE%", GameType.getByID(mode).getName())));
          } else {
            sender.sendMessage(
                new TextComponentString(
                    getCurrentLanguage(sender).PLAYER_NOT_FOUND.replaceAll("%PLAYER%", args[1])));
          }
        } else if (sender instanceof EntityPlayer) {
          EntityPlayer player = (EntityPlayer) sender;
          player.setGameType(GameType.getByID(mode));
          sender.sendMessage(
              new TextComponentString(
                  LanguageModule.getLangfromUUID(player.getGameProfile().getId())
                      .MODE_CHANGED
                      .replaceAll("%MODE%", GameType.getByID(mode).getName())));
        } else {
          sender.sendMessage(new TextComponentString(getCurrentLanguage(sender).PLAYER_ONLY));
        }
      } else {
        sender.sendMessage(
            new TextComponentString(
                getCurrentLanguage(sender).MODE_INVALID.replaceAll("%MODE%", args[0])));
      }
    } else {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    }
  }

  private int getMode(String arg) {
    for (String creative : CREATIVE) {
      if (arg.equalsIgnoreCase(creative)) {
        return 1;
      }
    }
    for (String survival : SURVIVAL) {
      if (arg.equalsIgnoreCase(survival)) {
        return 0;
      }
    }
    for (String adventure : ADVENTURE) {
      if (arg.equalsIgnoreCase(adventure)) {
        return 2;
      }
    }
    for (String spectator : SPECTATOR) {
      if (arg.equalsIgnoreCase(spectator)) {
        return 3;
      }
    }
    return -1;
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    // Lets Players out of creative without perms
    if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      if (player.capabilities.isCreativeMode) {
        return true;
      }
    }
    return super.checkPermission(server, sender);
  }
}
