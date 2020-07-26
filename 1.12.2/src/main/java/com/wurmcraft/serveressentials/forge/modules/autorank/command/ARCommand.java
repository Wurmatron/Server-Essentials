package com.wurmcraft.serveressentials.forge.modules.autorank.command;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.IDataHandler;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.data.RestDataHandler;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.api.command.SECommand;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.ChatHelper;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.autorank.event.RankupEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.NoteBlockEvent.Play;
import org.apache.commons.lang3.time.DurationFormatUtils;

@ModuleCommand(moduleName = "AutoRank", name = "AutoRank", aliases = {"AR"})
public class ARCommand {

  @Command(inputArguments = {})
  public void arBasic(ICommandSender sender) {
    check(sender, "check");
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.PLAYER}, inputNames = {"check", "player"})
  public void check(ICommandSender sender, String arg, EntityPlayer player) {
    if (arg.equalsIgnoreCase("check")) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        Language senderLanguage = PlayerUtils.getUserLanguage(sender);
        if (RankUtils.hasPermission(sender, "autorank.check.other")) {
          try {
            AutoRank ar = RankupEvents.getRankup(player);
            if (ar == null) {
              throw new NoSuchElementException();
            }
            Rank nextRank = (Rank) SERegistry
                .getStoredData(DataKey.RANK, ar.nextRank);
            ChatHelper.sendSpacerWithMessage(sender, senderLanguage.SPACER,
                senderLanguage.AUTORANK_AR_HEADER
                    .replaceAll("%PLAYER%",
                        player.getDisplayName().getUnformattedText()));
            ChatHelper.sendHoverMessage(sender,
                senderLanguage.AUTORANK_AR_NEXT.replaceAll("%RANK%", nextRank.getName()),
                nextRank.getPrefix() + " " + player.getDisplayNameString());
            long timeLeft = ar.playTime - PlayerUtils.getTotalPlaytime(player);
            if (timeLeft < 0) {
              timeLeft = 0;
            }
            ChatHelper.sendMessage(sender, senderLanguage.AUTORANK_AR_TIME
                .replaceAll("%TIME%",
                    DurationFormatUtils.formatDuration(timeLeft * 60000, "d%:H$:m#:s@")
                        .replace('%', 'D')
                        .replace('$', 'H').replace('#', 'M').replace('@', 'S')
                        .replaceAll(":", ", ")));
            if (ar.exp > 0) {
              ChatHelper.sendMessage(sender,
                  senderLanguage.AUTORANK_AR_XP.replaceAll("%XP%", ar.exp + ""));
            }
            if (ar.balance > 0) {
              ChatHelper.sendMessage(sender,
                  senderLanguage.AUTORANK_AR_MONEY.replaceAll("%AMOUNT%", ar.balance + "")
                      .replaceAll("%NAME%", ((EconomyConfig) SERegistry
                          .getStoredData(DataKey.MODULE_CONFIG,
                              "Economy")).defaultServerCurrency.name));
            }
            ChatHelper.sendMessage(sender, senderLanguage.SPACER);
            RankupEvents.checkAndHandleRankup(player, RankupEvents.getRankup(player));
          } catch (NoSuchElementException e) {
            ChatHelper.sendMessage(sender, senderLanguage.AUTORANK_AR_MAX);
          }
        } else {
          ChatHelper.sendHoverMessage(sender, senderLanguage.ERROR_NO_PERMS,
              "&c'autorank.check.other'");
        }
      } else {
        ChatHelper.sendClickMessage(sender, "&6/ar &dcheck &b<username>", "/ar check ");
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &e<&dcheck, create, remove, reload&e>");
    }
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"check, reload"})
  public void check(ICommandSender sender, String arg) {
    if (arg.equalsIgnoreCase("check")) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        Language senderLanguage = PlayerUtils.getUserLanguage(player);
        if (RankUtils.hasPermission(player, "autorank.check")) {
          try {
            AutoRank ar = RankupEvents.getRankup(player);
            if (ar == null) {
              throw new NoSuchElementException();
            }
            Rank nextRank = (Rank) SERegistry
                .getStoredData(DataKey.RANK, ar.nextRank);
            ChatHelper.sendSpacerWithMessage(player, senderLanguage.SPACER,
                senderLanguage.AUTORANK_AR_HEADER
                    .replaceAll("%PLAYER%",
                        player.getDisplayName().getUnformattedText()));
            ChatHelper.sendHoverMessage(player,
                senderLanguage.AUTORANK_AR_NEXT.replaceAll("%RANK%", nextRank.getName()),
                nextRank.getPrefix() + " " + player.getDisplayNameString());
            long timeLeft = ar.playTime - PlayerUtils.getTotalPlaytime(player);
            if (timeLeft < 0) {
              timeLeft = 0;
            }
            ChatHelper.sendMessage(player, senderLanguage.AUTORANK_AR_TIME
                .replaceAll("%TIME%",
                    DurationFormatUtils.formatDuration(timeLeft * 60000, "d%:H$:m#:s@")
                        .replace('%', 'D')
                        .replace('$', 'H').replace('#', 'M').replace('@', 'S')
                        .replaceAll(":", ", ")));
            if (ar.exp > 0) {
              ChatHelper.sendMessage(player,
                  senderLanguage.AUTORANK_AR_XP.replaceAll("%XP%", ar.exp + ""));
            }
            if (ar.balance > 0) {
              ChatHelper.sendMessage(player,
                  senderLanguage.AUTORANK_AR_MONEY.replaceAll("%AMOUNT%", ar.balance + "")
                      .replaceAll("%NAME%", ((EconomyConfig) SERegistry
                          .getStoredData(DataKey.MODULE_CONFIG,
                              "Economy")).defaultServerCurrency.name));
            }
            ChatHelper.sendMessage(player, senderLanguage.SPACER);
            RankupEvents.checkAndHandleRankup(player, RankupEvents.getRankup(player));
          } catch (NoSuchElementException e) {
            ChatHelper.sendMessage(sender, senderLanguage.AUTORANK_AR_MAX);
          }
        } else {
          ChatHelper.sendHoverMessage(player, senderLanguage.ERROR_NO_PERMS,
              "&c'autorank.check'");
        }
      } else {
        ChatHelper.sendClickMessage(sender, "&6/ar &dcheck &b<username>", "/ar check ");
      }
    } else if (arg.equalsIgnoreCase("reload")) {
      Language senderLanguage = PlayerUtils.getUserLanguage(sender);
      if (RankUtils.hasPermission(sender, "autorank.reload")) {
        SERegistry.removeAllDataFromKey(DataKey.AUTO_RANK);
        SECore.dataHandler.getDataFromKey(DataKey.AUTO_RANK, new AutoRank());
        ChatHelper.sendMessage(sender, senderLanguage.AUTORANK_AR_RELOAD);
      } else {
        ChatHelper.sendHoverMessage(sender, senderLanguage.ERROR_NO_PERMS,
            "&c'autorank.reload'");
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &e<&dcheck, create, remove, reload&e>");
    }
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.RANK,
      CommandArguments.RANK, CommandArguments.INTEGER}, inputNames = {"create",
      "currentRank", "nextRank", "timeInMinutes"})
  public void createTimeOnly(ICommandSender sender, String arg, Rank currentRank,
      Rank nextRank, int time) {
    createFull(sender, arg, currentRank, nextRank, time, 0, 0);
  }

  @Command(inputArguments = {CommandArguments.STRING, CommandArguments.RANK,
      CommandArguments.RANK, CommandArguments.INTEGER, CommandArguments.INTEGER,
      CommandArguments.INTEGER}, inputNames = {"create", "currentRank",
      "nextRank", "timeInMinutes", "requiredCurrencyAmount", "expLvl's"})
  public void createFull(ICommandSender sender, String arg, Rank currentRank,
      Rank nextRank, int time, int balance, int xp) {
    Language senderLanguage = PlayerUtils.getUserLanguage(sender);
    if (arg.equalsIgnoreCase("create") || arg.equalsIgnoreCase("add")) {
      if (RankUtils.hasPermission(sender, "autorank.create")) {
        AutoRank autoRank = new AutoRank(time, balance, xp, currentRank.getID(),
            nextRank.getID());
        SERegistry.register(DataKey.AUTO_RANK, autoRank);
        if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
          RestRequestGenerator.AutoRank.addAutoRank(autoRank);
        }
        ChatHelper.sendMessage(sender,
            senderLanguage.AUTORANK_AR_CREATE.replaceAll("%NAME%", autoRank.rank));
        ChatHelper.sendSpacerWithMessage(sender, senderLanguage.SPACER,
            senderLanguage.AUTORANK_AR_HEADER
                .replaceAll("%PLAYER%",
                    autoRank.rank));
        ChatHelper.sendMessage(sender, senderLanguage.AUTORANK_AR_TIME
            .replaceAll("%TIME%",
                DurationFormatUtils
                    .formatDuration(autoRank.playTime * 60000, "d%:H$:m#:s@")
                    .replace('%', 'D')
                    .replace('$', 'H').replace('#', 'M').replace('@', 'S')
                    .replaceAll(":", ", ")));
        if (autoRank.exp > 0) {
          ChatHelper.sendMessage(sender,
              senderLanguage.AUTORANK_AR_XP.replaceAll("%XP%", autoRank.exp + ""));
        }
        if (autoRank.balance > 0) {
          ChatHelper.sendMessage(sender,
              senderLanguage.AUTORANK_AR_MONEY
                  .replaceAll("%AMOUNT%", autoRank.balance + "")
                  .replaceAll("%NAME%", ((EconomyConfig) SERegistry
                      .getStoredData(DataKey.MODULE_CONFIG,
                          "Economy")).defaultServerCurrency.name));
        }
        ChatHelper.sendMessage(sender, senderLanguage.SPACER);
      } else {
        ChatHelper.sendHoverMessage(sender, senderLanguage.ERROR_NO_PERMS,
            "&c'autorank.create'");
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &e<&dcheck, create, remove&e>");
    }
  }

  @Command(inputArguments = {CommandArguments.STRING,
      CommandArguments.RANK}, inputNames = {"delete", "rank"})
  public void delete(ICommandSender sender, String arg, Rank rank) {
    if (arg.equalsIgnoreCase("delete") || arg.equalsIgnoreCase("del")
        || arg.equalsIgnoreCase("remove") && arg.equalsIgnoreCase("rem")) {
      Language senderLanguage = PlayerUtils.getUserLanguage(sender);
      if (RankUtils.hasPermission(sender, "autorank.delete")) {
        try {
          SERegistry.delStoredData(DataKey.AUTO_RANK, rank.getID());
          ChatHelper.sendMessage(sender,
              senderLanguage.AUTORANK_AR_DELETE.replaceAll("%NAME%", rank.getName()));
        } catch (NoSuchElementException e) {
        }
      } else {
        ChatHelper.sendHoverMessage(sender, senderLanguage.ERROR_NO_PERMS,
            "&c'autorank.delete'");
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &e<&dcheck, create, remove&e>");
    }
  }
}
