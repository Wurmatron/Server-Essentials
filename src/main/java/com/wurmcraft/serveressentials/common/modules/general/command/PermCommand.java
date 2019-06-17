package com.wurmcraft.serveressentials.common.modules.general.command;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.command.Command;
import com.wurmcraft.serveressentials.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.api.command.SubCommand;
import com.wurmcraft.serveressentials.api.lang.Lang;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.Replacment;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.command.CommandUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

@ModuleCommand(moduleName = "General")
public class PermCommand extends Command {

  private static final List<String> emptyAutoCompletion = Arrays.asList("user", "group");
  private static final List<String> userAutoCompletion =
      Arrays.asList("info", "rank", "perm", "perk", "addPerm", "delPerm", "addPerk", "delPerk");
  private static final List<String> groupAutoCompletion = Arrays.asList("info", "add");

  @Override
  public List<String> getAutoCompletion(
      MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
    if (args.length == 0) {
      return emptyAutoCompletion;
    } else if (args.length > 1) {
      if (args[0].equalsIgnoreCase("user") && args.length == 1) {
        return CommandUtils.predictUsernames(args, 0);
      } else if (args[0].equalsIgnoreCase("user") && args.length == 2) {
        return userAutoCompletion;
      } else if (args[0].equalsIgnoreCase("group") && args.length == 1) {
        return CommandUtils.predictRank(args, 0);
      } else if (args[0].equalsIgnoreCase("group") && args.length == 2) {
        return groupAutoCompletion;
      }
    }
    return super.getAutoCompletion(server, sender, args, pos);
  }

  @Override
  public String getName() {
    return "Perm";
  }

  @Override
  public String getUsage(Lang lang) {
    return "/perm <user, group> <info, rank, perm, add,del,group> <info>";
  }

  @Override
  public String getDescription(Lang lang) {

    return null;
  }

  @Override
  public void execute(
      MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {}

  @SubCommand
  public void user(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    EntityPlayer player = CommandUtils.getPlayerForName(args[0]);
    if (player != null) {
      if (args.length > 1) {
        if (args[1].equalsIgnoreCase("Info")) {
          displayUserInfo(player);
        } else if (args[1].equalsIgnoreCase("Rank") || args[1].equalsIgnoreCase("Group")) {
          Rank rank = ServerEssentialsAPI.rankManager.getRank(args[2]);
          if (rank != null) {
            UserManager.setRank(player, rank);
            ChatHelper.sendMessage(
                sender,
                senderLang
                    .local
                    .REST_RANK_CHANGED_OTHER
                    .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                    .replaceAll(Replacment.RANK, rank.getName()));
            ChatHelper.sendMessage(
                player, LanguageModule.getUserLanguage(player).local.REST_RANK_CHANGED);
          } else {
            ChatHelper.sendMessage(
                sender, senderLang.local.RANK_NOT_FOUND.replaceAll(Replacment.RANK, args[2]));
          }
        } else if (args[1].equalsIgnoreCase("Perm")
            || args[1].equalsIgnoreCase("Permission")
            || args[1].equalsIgnoreCase("Perms")) {
        } else if (args[1].equalsIgnoreCase("Perk") || args[1].equalsIgnoreCase("Perks")) {
          displayPerkPerm(player, UserManager.getPerks(player));
        } else if (args[1].equalsIgnoreCase("addPerm") || args[1].equalsIgnoreCase("addPerms")) {
          UserManager.addPerm(player, args[2]);
          ChatHelper.sendMessage(
              sender,
              senderLang
                  .local
                  .PERM_UPDATED_OTHER
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(Replacment.PERM, args[2]));
          ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.PERM_UPDATED);
        } else if (args[1].equalsIgnoreCase("addPerk") || args[1].equalsIgnoreCase("addPerks")) {
          UserManager.addPerk(player, args[2]);
          ChatHelper.sendMessage(
              sender,
              senderLang
                  .local
                  .PERK_UPDATED_OTHER
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(Replacment.PERK, args[2]));
          ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.PERK_UPDATED);
        } else if (args[1].equalsIgnoreCase("delPerm") || args[1].equalsIgnoreCase("delPerms")) {
          UserManager.delPerm(player, args[2]);
          ChatHelper.sendMessage(
              sender,
              senderLang
                  .local
                  .PERM_UPDATED_OTHER
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(Replacment.PERM, args[2]));
          ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.PERM_UPDATED);
        } else if (args[1].equalsIgnoreCase("delPerk") || args[1].equalsIgnoreCase("delPerks")) {
          UserManager.delPerk(player, args[2]);
          ChatHelper.sendMessage(
              sender,
              senderLang
                  .local
                  .PERK_UPDATED_OTHER
                  .replaceAll(Replacment.PLAYER, player.getDisplayNameString())
                  .replaceAll(Replacment.PERK, args[2]));
          ChatHelper.sendMessage(player, LanguageModule.getUserLanguage(player).local.PERK_UPDATED);
        }
      } else {
        ChatHelper.sendMessage(sender, getUsage(senderLang));
      }
    } else {
      ChatHelper.sendMessage(
          sender, senderLang.local.PLAYER_NOT_FOUND.replaceAll(Replacment.PLAYER, args[0]));
    }
  }

  @SubCommand(aliases = "rank")
  public void group(MinecraftServer server, ICommandSender sender, String[] args, Lang senderLang) {
    Rank rank = ServerEssentialsAPI.rankManager.getRank(args[0]);
    if (rank != null) {
      if (args.length > 1) {
        if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("create")) {
          if (args.length >= 4) {
            String prefix = args[2];
            String suffix = args[3];
            Rank newRank = new Rank(args[0], prefix, suffix, new String[0], new String[0]);
            ServerEssentialsAPI.rankManager.register(newRank);
            saveRank(newRank);
            ChatHelper.sendMessage(
                sender, senderLang.local.RANK_CREATED.replaceAll(Replacment.RANK, args[0]));
          }
        } else if (args[1].equalsIgnoreCase("info")) {
          displayRankInfo(sender, rank);
        }
      } else {
        ChatHelper.sendMessage(sender, getUsage(senderLang));
      }
    } else if (args.length > 1 && !args[1].equalsIgnoreCase("add")) {
      ChatHelper.sendMessage(
          sender, senderLang.local.RANK_NOT_FOUND.replaceAll(Replacment.RANK, args[0]));
    } else {
      ChatHelper.sendMessage(
          sender, senderLang.local.RANK_NOT_FOUND.replaceAll(Replacment.RANK, args[0]));
    }
  }

  private static void displayUserInfo(EntityPlayer player) {
    Lang lang = LanguageModule.getUserLanguage(player);
    ChatHelper.sendMessage(player, lang.local.CHAT_SPACER);
    ChatHelper.sendMessage(
        player,
        lang.local.CHAT_UUID.replaceAll(
            Replacment.UUID, player.getGameProfile().getId().toString()));
    ChatHelper.sendMessage(
        player,
        lang.local.CHAT_RANK.replaceAll(Replacment.RANK, UserManager.getUserRank(player).getID()));
    ChatHelper.sendMessage(
        player,
        lang.local.CHAT_MUTED.replaceAll(
            Replacment.NUMBER, "" + UserManager.isUserMuted(player.getGameProfile().getId())));
    ChatHelper.sendMessage(player, lang.local.CHAT_SPACER);
  }

  private static void displayPerkPerm(ICommandSender sender, String[] data) {
    Lang lang = LanguageModule.getUserLanguage(sender);
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
    for (String p : data) {
      ChatHelper.sendMessage(sender, TextFormatting.AQUA + p);
    }
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
  }

  private static void displayRankInfo(ICommandSender sender, Rank rank) {
    Lang lang = LanguageModule.getUserLanguage(sender);
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
    ChatHelper.sendMessage(
        sender, lang.local.CHAT_RANK.replaceAll(Replacment.RANK, rank.getName()));
    ChatHelper.sendMessage(
        sender, lang.local.CHAT_RANK_PREFIX.replaceAll(Replacment.RANK, rank.getName()));
    ChatHelper.sendMessage(
        sender, lang.local.CHAT_RANK_SUFFIX.replaceAll(Replacment.RANK, rank.getName()));
    ChatHelper.sendMessage(
        sender, lang.local.CHAT_RANK_INHERITANCE.replaceAll(Replacment.RANK, rank.getName()));
    ChatHelper.sendMessage(sender, lang.local.CHAT_SPACER);
  }

  private static void saveRank(Rank rank) {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      RequestGenerator.Rank.addRank(rank);
    } else {
      DataHelper.save(Storage.RANK, rank);
    }
  }
}
