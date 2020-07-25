package com.wurmcraft.serveressentials.forge.modules.autorank.command;

import com.wurmcraft.serveressentials.core.api.command.Command;
import com.wurmcraft.serveressentials.core.api.command.CommandArguments;
import com.wurmcraft.serveressentials.core.api.command.ModuleCommand;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.Language;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.ChatHelper;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.autorank.event.RankupEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.time.DurationFormatUtils;

@ModuleCommand(moduleName = "AutoRank", name = "AutoRank", aliases = {"AR"})
public class ARCommand {

  @Command(inputArguments = {})
  public void arBasic(ICommandSender sender) {
    check(sender, "check");
  }

  @Command(inputArguments = {CommandArguments.STRING}, inputNames = {"check"})
  public void check(ICommandSender sender, String arg) {
    if (arg.equalsIgnoreCase("check")) {
      if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        Language senderLanguage = PlayerUtils.getUserLanguage(player);
        if (RankUtils.hasPermission(player, "autorank.autorank.check")) {
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
            ChatHelper.sendMessage(player, senderLanguage.AUTORANK_AR_TIME
                .replaceAll("%TIME%", DurationFormatUtils.formatDuration(
                    (PlayerUtils.getTotalPlaytime(player, PlayerUtils.getPlayer(player))
                        - ar.playTime) * 60000, "d%:H$:m#:s@").replace('%', 'D')
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
          } catch (NoSuchElementException e) {
            ServerEssentialsServer.logger.error(
                "Rank '" + RankupEvents.checkForRankup(player)
                    + "' does not exist! (Rankup)");
          }
        } else {
          ChatHelper.sendHoverMessage(player, senderLanguage.ERROR_NO_PERMS,
              "&c'autorank.autorank.check'");
        }
      } else {
        ChatHelper.sendClickMessage(sender, "&6/ar &dcheck &b<username>", "/ar check ");
      }
    } else {
      ChatHelper.sendMessage(sender, "&6/ar &e<&dcheck, create, remove&e>");
    }
  }
}
