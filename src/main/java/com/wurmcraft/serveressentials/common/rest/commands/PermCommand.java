package com.wurmcraft.serveressentials.common.rest.commands;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.SECommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.restOnly.LocalUser;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.language.LanguageModule;
import com.wurmcraft.serveressentials.common.language.Local;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import com.wurmcraft.serveressentials.common.utils.UsernameResolver;
import java.util.Date;
import java.util.UUID;
import joptsimple.internal.Strings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.UsernameCache;

@Command(moduleName = "Rest", trustedRequired = true)
public class PermCommand extends SECommand {

  private static String[] getUserFormatting(UUID uuid) {
    GlobalUser user = forceUserFromUUID(uuid);
    Local lang = LanguageModule.getLangFromKey(user.getLang());
    return new String[] {
      lang.CHAT_SPACER.replaceAll("&", "\u00A7"),
      TextFormatting.LIGHT_PURPLE + "UUID: " + TextFormatting.AQUA + uuid,
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_RANK.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + user.getRank(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_TEAM.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + user.getTeam(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_LANG.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + user.getLang(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_LASTSEEN.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + new Date(user.getLastSeen()).toString(),
      lang.CHAT_SPACER.replaceAll("&", "\u00A7")
    };
  }

  private static String[] getRankFormatting(ICommandSender sender, String r) {
    Local lang = getCurrentLanguage(sender);
    Rank rank = UserManager.getRank(r);
    return new String[] {
      lang.CHAT_SPACER.replaceAll("&", "\u00A7"),
      TextFormatting.LIGHT_PURPLE + lang.CHAT_NAME + ": " + TextFormatting.AQUA + rank.getName(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_PREFIX.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + rank.getPrefix(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_SUFFIX.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + rank.getSuffix(),
      TextFormatting.LIGHT_PURPLE
          + lang.CHAT_INHERITANCE.replaceAll("&", "\u00A7")
          + ": "
          + TextFormatting.AQUA
          + Strings.join(rank.getInheritance(), " "),
      lang.CHAT_SPACER.replaceAll("&", "\u00A7")
    };
  }

  @Override
  public String getName() {
    return "perm";
  }

  @Override
  public String getUsage(ICommandSender sender) {

    return "/perm <user|group> <name> <info|rank|perm|perk|sync> <add|del> <data>...";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    super.execute(server, sender, args);
  }

  @SubCommand
  public void group(ICommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    } else {
      Local lang = getCurrentLanguage(sender);
      if (args.length == 2 && args[1].equalsIgnoreCase("info")) {
        for (String format : getRankFormatting(sender, args[0])) {
          sender.sendMessage(new TextComponentString(format));
        }
      } else if (args.length >= 4 && args[1].equalsIgnoreCase("perm")) {
        Rank rank = UserManager.getRank(args[0]);
        boolean added = args[2].equalsIgnoreCase("add");
        if (rank != null) {
          for (int index = 3; index < args.length; index++) {
            if (added) {
              rank.addPermission(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERM_ADDED
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + rank.getName()
                                      + TextFormatting.LIGHT_PURPLE)));
            } else {
              rank.delPermission(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERM_DEL
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + rank.getName()
                                      + TextFormatting.LIGHT_PURPLE)));
            }
            RequestHelper.RankResponses.overrideRank(rank);
          }
        } else {
          sender.sendMessage(
              new TextComponentString(
                  TextFormatting.RED
                      + lang.PLAYER_NOT_FOUND.replaceAll(
                          "%PLAYER%", TextFormatting.GOLD + args[0] + TextFormatting.RED)));
        }
      } else if (args.length == 2 && args[1].equalsIgnoreCase("sync")) {
        Rank rank = RequestHelper.RankResponses.getRank(args[0]);
        if (rank != null) {
          UserManager.rankCache.put(rank.getName(), rank);
        }
      } else if (args.length == 1 && args[0].equalsIgnoreCase("syncAll")) {
        Rank[] ranks = RequestHelper.RankResponses.getAllRanks();
        UserManager.rankCache.clear();
        for (Rank rank : ranks) {
          UserManager.rankCache.put(rank.getName(), rank);
        }
      }
    }
  }

  @SubCommand
  public void user(ICommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(new TextComponentString(getUsage(sender)));
    } else {
      Local lang = getCurrentLanguage(sender);
      if (args.length == 2 && args[1].equalsIgnoreCase("info")) {
        for (String format : getUserFormatting(UsernameResolver.getUUIDFromName(args[0]))) {
          sender.sendMessage(new TextComponentString(format));
        }
      } else if (args.length == 3 && args[1].equalsIgnoreCase("rank")) {
        Rank rank = UserManager.getRank(args[2]);
        if (UserManager.isValidRank(args[2]) && rank != null) {
          UUID uuid = UsernameResolver.getUUIDFromName(args[0]);
          GlobalUser selectedUser = forceUserFromUUID(uuid);
          if (selectedUser != null) {
            selectedUser.setRank(rank.getName());
            RequestHelper.UserResponses.overridePlayerData(selectedUser);
            sender.sendMessage(
                new TextComponentString(
                    TextFormatting.LIGHT_PURPLE
                        + lang.PLAYER_RANK_CHANGED
                            .replaceAll(
                                "%RANK%",
                                TextFormatting.RED
                                    + selectedUser.getRank()
                                    + TextFormatting.LIGHT_PURPLE)
                            .replaceAll(
                                "%PLAYER%",
                                TextFormatting.GOLD
                                    + UsernameCache.getLastKnownUsername(uuid)
                                    + TextFormatting.LIGHT_PURPLE)));
          } else {
            sender.sendMessage(
                new TextComponentString(
                    TextFormatting.RED
                        + lang.PLAYER_NOT_FOUND.replaceAll(
                            "%PLAYER%", TextFormatting.GOLD + args[0] + TextFormatting.RED)));
          }
        } else {
          sender.sendMessage(new TextComponentString(lang.RANK_NULL));
        }
      } else if (args.length >= 4 && args[1].equalsIgnoreCase("perm")) {
        UUID uuid = UsernameResolver.getUUIDFromName(args[0]);
        GlobalUser selectedUser = forceUserFromUUID(uuid);
        boolean added = args[2].equalsIgnoreCase("add");
        if (selectedUser != null) {
          for (int index = 3; index < args.length; index++) {
            if (added) {
              selectedUser.addPermission(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERM_ADDED
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + UsernameCache.getLastKnownUsername(uuid)
                                      + TextFormatting.LIGHT_PURPLE)));
              UserManager.playerData.put(
                  uuid,
                  new Object[] {
                    selectedUser,
                    UserManager.playerData
                        .getOrDefault(uuid, new Object[] {selectedUser, new LocalUser(uuid)})[1]
                  });
            } else {
              selectedUser.delPermission(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERM_DEL
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + UsernameCache.getLastKnownUsername(uuid)
                                      + TextFormatting.LIGHT_PURPLE)));
              UserManager.playerData.put(
                  uuid, new Object[] {selectedUser, UserManager.playerData.get(uuid)[1]});
            }
            RequestHelper.UserResponses.overridePlayerData(selectedUser);
          }
        } else {
          sender.sendMessage(
              new TextComponentString(
                  TextFormatting.RED
                      + lang.PLAYER_NOT_FOUND.replaceAll(
                          "%PLAYER%", TextFormatting.GOLD + args[0] + TextFormatting.RED)));
        }
      } else if (args.length >= 4 && args[1].equalsIgnoreCase("perk")) {
        UUID uuid = UsernameResolver.getUUIDFromName(args[0]);
        GlobalUser selectedUser = forceUserFromUUID(uuid);
        boolean added = args[2].equalsIgnoreCase("add");
        if (selectedUser != null) {
          for (int index = 3; index < args.length; index++) {
            if (added) {
              selectedUser.addPerk(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERK_ADDED
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + UsernameCache.getLastKnownUsername(uuid)
                                      + TextFormatting.LIGHT_PURPLE)));
              UserManager.playerData.put(
                  uuid,
                  new Object[] {
                    selectedUser,
                    UserManager.playerData
                        .getOrDefault(uuid, new Object[] {selectedUser, new LocalUser(uuid)})[1]
                  });
            } else {
              selectedUser.delPerk(args[index]);
              sender.sendMessage(
                  new TextComponentString(
                      TextFormatting.LIGHT_PURPLE
                          + lang.PERK_DEL
                              .replaceAll(
                                  "%PERM%",
                                  TextFormatting.RED + args[3] + TextFormatting.LIGHT_PURPLE)
                              .replaceAll(
                                  "%PLAYER%",
                                  TextFormatting.GOLD
                                      + UsernameCache.getLastKnownUsername(uuid)
                                      + TextFormatting.LIGHT_PURPLE)));
              UserManager.playerData.put(
                  uuid,
                  new Object[] {
                    selectedUser,
                    UserManager.playerData
                        .getOrDefault(uuid, new Object[] {selectedUser, new LocalUser(uuid)})[1]
                  });
            }
            RequestHelper.UserResponses.overridePlayerData(selectedUser);
          }
        } else {
          sender.sendMessage(
              new TextComponentString(
                  TextFormatting.RED
                      + lang.PLAYER_NOT_FOUND.replaceAll(
                          "%PLAYER%", TextFormatting.GOLD + args[0] + TextFormatting.RED)));
        }
      } else if (args.length == 2 && args[1].equalsIgnoreCase("sync")) {
        UUID uuid = UsernameResolver.getUUIDFromName(args[0]);
        GlobalUser user = RequestHelper.UserResponses.getPlayerData(uuid);
        LocalUser local = DataHelper.load(Keys.LOCAL_USER, new LocalUser(uuid));
        UserManager.playerData.put(uuid, new Object[] {user, local});
      }
    }
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }

  @Override
  public boolean hasSubCommand() {
    return true;
  }

  @Override
  public String getDescription(ICommandSender sender) {
    return getCurrentLanguage(sender).COMMAND_PERM.replaceAll("&", "\u00A7");
  }
}
