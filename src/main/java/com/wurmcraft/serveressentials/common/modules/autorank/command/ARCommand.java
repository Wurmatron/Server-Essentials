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
import com.wurmcraft.serveressentials.common.modules.autorank.AutoRankUtils;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
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
    return "/autoRank <check, force, admin> ";
  }

  @Override
  public String getDescription(Lang lang) {
    return lang.local.DESCRIPTION_AUTORANK;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) {}

  @SubCommand(aliases = "Check")
  public void check(MinecraftServer server, ICommandSender sender, String[] args) {
    if (args.length == 0) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        displayCurrentAutoRankStatus((EntityPlayer) sender.getCommandSenderEntity());
      } else {
        ChatHelper.sendMessage(sender, getUsage(getUserLanguage(sender)));
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
      ChatHelper.sendMessage(sender, getUsage(getUserLanguage(sender)));
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
      System.out.println("Name: " + getUserLanguage(player).local.AUTORANK_MAX_RANK);
      ChatHelper.sendMessage(player, getUserLanguage(player).local.AUTORANK_MAX_RANK);
      //          .replaceAll("%PLAYER%", player.getDisplayNameString()));
    }
  }

  @SubCommand
  public void force(MinecraftServer server, ICommandSender sender, String[] args) {}

  @SubCommand
  public void admin(MinecraftServer server, ICommandSender sender, String[] args) {}

  @Override
  public List<String> getAliases(List<String> aliases) {
    return super.getAliases(aliases);
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
      if (args.length == 1) {
        return adminAutoCompletion;
      } else if (args.length == 2) {
        return predictRank(args, 2);
      } else if (args.length == 3) {
        return predictRank(args, 3);
      } else if (args.length > 3 && args.length <= 5) {
        return Arrays.asList("1");
      }
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  @Override
  public boolean canConsoleRun() {
    return true;
  }
}
