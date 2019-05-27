package com.wurmcraft.serveressentials.common.modules.autorank.command;

import static com.wurmcraft.serveressentials.common.modules.language.LanguageModule.getUserLanguage;
import static com.wurmcraft.serveressentials.common.utils.command.CommandUtils.getPlayerForName;
import static com.wurmcraft.serveressentials.common.utils.command.CommandUtils.predictRank;
import static com.wurmcraft.serveressentials.common.utils.command.CommandUtils.predictUsernames;

import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.common.modules.autorank.AutoRankModule;
import com.wurmcraft.serveressentials.common.modules.autorank.AutoRankUtils;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

@ModuleCommand(moduleName = "AutoRank")
public class ARCommand extends Command {

  private static List<String> emptyAutoCompletion = Arrays.asList("check", "force", "admin");
  private static List<String> adminAutoCompletion = Arrays.asList("add", "remove");

  @Override
  public String getName() {
    return "autoRank";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/autoRank <check, force, admin> <user> | <user> | <add, remove> <rank> <newRank> <playTime> <exp> <balance>";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_AUTORANK;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand()
  public void check(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        displayCurrentAutoRankStatus((EntityPlayer) sender.getCommandSenderEntity());
      } else {
        ChatHelper.sendMessage(sender, getUsage(senderLang));
      }
    } else if (args.length == 1) {
      EntityPlayer player = getPlayerForName(args[0]);
      if (player != null) {
        displayCurrentAutoRankStatus(player);
      } else {
        ChatHelper.sendMessage(
            sender,
            getUserLanguage(sender).local.PLAYER_NOT_FOUND.replaceAll("%PLAYER% ", args[0]));
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private static void displayCurrentAutoRankStatus(EntityPlayer player) {
    AutoRank rank = AutoRankUtils.getNextAutoRank(player);
    if (rank != null) {
      ChatHelper.sendMessage(player, getUserLanguage(player).local.CHAT_SPACER);
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .CHAT_PLAYER
              .replaceAll("%PLAYER%", player.getDisplayNameString()));
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .CHAT_TIME
              .replaceAll("%TIME%", Integer.toString(rank.getPlayTime())));
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .CHAT_EXPERIENCE
              .replaceAll("%EXPERIENCE%", Integer.toString(rank.getExp())));
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .CHAT_BALANCE
              .replaceAll("%BALANCE%", Integer.toString(rank.getBalance())));
      ChatHelper.sendMessage(player, getUserLanguage(player).local.CHAT_SPACER);
    } else {
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .AUTORANK_MAX_RANK
              .replaceAll("%PLAYER%", player.getDisplayNameString()));
    }
  }

  @SubCommand
  public void force(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length == 0 && sender.getCommandSenderEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
      AutoRank rank = processPlayer(player);
      if (rank != null) {
        ChatHelper.sendMessage(
            player,
            getUserLanguage(player).local.AUTORANK_CHECK.replaceAll("%RANK%", rank.getNextRank()));
      } else {
        ChatHelper.sendMessage(
            sender,
            getUserLanguage(player)
                .local
                .AUTORANK_MAX_RANK
                .replaceAll("%PLAYER%", player.getDisplayNameString()));
      }
    } else if (args.length == 1) {
      EntityPlayer player = getPlayerForName(args[0]);
      if (player != null) {
        AutoRank rank = processPlayer(player);
        if (rank != null) {
          ChatHelper.sendMessage(
              player,
              getUserLanguage(player)
                  .local
                  .AUTORANK_CHECK
                  .replaceAll("%RANK%", rank.getNextRank()));
        } else {
          ChatHelper.sendMessage(
              sender,
              getUserLanguage(player)
                  .local
                  .AUTORANK_MAX_RANK
                  .replaceAll("%PLAYER%", player.getDisplayNameString()));
        }
      } else {
        ChatHelper.sendMessage(
            sender,
            getUserLanguage(sender).local.PLAYER_NOT_FOUND.replaceAll("%PLAYER% ", args[0]));
      }
    }
  }

  public static AutoRank processPlayer(EntityPlayer player) {
    AutoRank rank = AutoRankUtils.getNextAutoRank(player);
    if (rank != null) {
      UserManager.setUserCurrency(player, rank.getBalance());
      player.addExperience(rank.getExp());
      UserManager.addServerTime(player, rank.getPlayTime());
    } else {
      ChatHelper.sendMessage(
          player,
          getUserLanguage(player)
              .local
              .AUTORANK_MAX_RANK
              .replaceAll("%PLAYER%", player.getDisplayNameString()));
    }
    return rank;
  }

  @SubCommand
  public void admin(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    if (args.length > 0) {
      if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {
        add(sender, args, senderLang);
      } else if (args[0].equalsIgnoreCase("remove")
          || args[0].equalsIgnoreCase("rem")
          || args[0].equalsIgnoreCase("del")
          || args[0].equalsIgnoreCase("delete")) {
        del(sender, args, senderLang);
      }
    } else {
      ChatHelper.sendMessage(sender, getUsage(senderLang));
    }
  }

  private static void add(ICommandSender sender, String[] args, Lang senderLang) {
    try {
      AutoRank rank =
          new AutoRank(
              Integer.parseInt(args[3]),
              Integer.parseInt(args[4]),
              Integer.parseInt(args[5]),
              args[1],
              args[2]);
      AutoRankModule.createAutoRank(rank);
      ChatHelper.sendMessage(
          sender, senderLang.local.AUTORANK_CREATED.replaceAll("%RANK%", rank.getNextRank()));
    } catch (NumberFormatException e) {
      ChatHelper.sendMessage(sender, senderLang.local.CHAT_INVALID_NUMBER);
    }
  }

  private static void del(ICommandSender sender, String[] args, Lang senderLang) {
    AutoRank rank = AutoRankModule.getAutoRank(args[1]);
    if (rank != null) {
      AutoRankModule.deleteAutoRank(rank);
      ChatHelper.sendMessage(
          sender, senderLang.local.AUTORANK_DELETED.replaceAll("%RANK%", rank.getNextRank()));
    } else {
      ChatHelper.sendMessage(
          sender, senderLang.local.CHAT_INVALID_AUTORANK.replaceAll("%RANK%", args[1]));
    }
  }

  @Override
  public List<String> getAliases(List<String> aliases) {
    aliases.add("ar");
    return aliases;
  }

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0 || args.length == 1 && args[0].isEmpty()) {
      return emptyAutoCompletion;
    } else if (args.length >= 1 && args[0].equalsIgnoreCase("check")
        || args.length >= 1 && args[0].equalsIgnoreCase("force")) {
      return predictUsernames(args, 1);
    } else if (args.length > 0 && args[0].equalsIgnoreCase("admin")) {
      if (args.length == 2) {
        return adminAutoCompletion;
      } else {
        if (args[1].equalsIgnoreCase("add")) {
          if (args.length == 3) {
            return predictRank(args, 2);
          } else if (args.length == 4) {
            return predictRank(args, 3);
          } else if (args.length > 5 && args.length <= 6) {
            return Arrays.asList("1");
          }
          return predictRank(args, 2);
        } else if ((args[0].equalsIgnoreCase("remove")
            || args[0].equalsIgnoreCase("rem")
            || args[0].equalsIgnoreCase("del")
            || args[0].equalsIgnoreCase("delete"))) {
          List<String> ranks = new ArrayList<>();
          for (AutoRank rank : AutoRankModule.getAutoRanks()) {
            ranks.add(rank.getNextRank());
          }
          return ranks;
        }
      }
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
