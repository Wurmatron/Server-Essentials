package com.wurmcraft.serveressentials.forge.modules.autorank.event;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.common.utils.ChatHelper;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RankupEvents {

  @SubscribeEvent
  public void onWorldUpdate(WorldTickEvent e) {
    if (e.side == Side.SERVER) {
      if (e.world.getWorldTime() % 24000 == 0) { // 1 in-game day (20m)
        for (EntityPlayer player : FMLCommonHandler.instance()
            .getMinecraftServerInstance().getPlayerList().getPlayers()) {
          AutoRank autoRank = getRankup(player);
          checkAndHandleRankup(player,autoRank);
        }
      }
    }
  }

  public static AutoRank getRankup(EntityPlayer player) {
    StoredPlayer playerData = PlayerUtils.getPlayer(player);
    for (AutoRank rank : SECore.dataHandler
        .getDataFromKey(DataKey.AUTO_RANK, new AutoRank()).values()) {
      if (playerData.global.rank.equalsIgnoreCase(rank.rank)) {
        return rank;
      }
    }
    return null;
  }

  private static boolean canRankup(AutoRank rank, StoredPlayer playerData,
      EntityPlayer player) {
    return EcoUtils.hasTheMoney(playerData.global.wallet, rank.balance)
        && rank.exp <= player.experienceLevel && rank.playTime <= PlayerUtils
        .getTotalPlaytime(player);
  }

  public static void checkAndHandleRankup(EntityPlayer player, AutoRank autoRank) {
    if (autoRank != null) {
      if (canRankup(autoRank, PlayerUtils.getPlayer(player), player)) {
        try {
          Rank nextRank = (Rank) SERegistry
              .getStoredData(DataKey.RANK, autoRank.nextRank);
          RankUtils.setRank(player, nextRank);
          ChatHelper.sendHoverMessage(player,
              PlayerUtils.getUserLanguage(player).AUTORANK_RANK_UP
                  .replaceAll("%RANK%", nextRank.getName()),
              nextRank.getPrefix() + " " + player.getDisplayNameString());
          for (EntityPlayer otherPlayer : FMLCommonHandler.instance()
              .getMinecraftServerInstance().getPlayerList().getPlayers()) {
            ChatHelper.sendMessage(otherPlayer,
                PlayerUtils.getUserLanguage(otherPlayer).AUTORANK_RANK_UP_OTHER
                    .replaceAll("%RANK%", nextRank.getName())
                    .replaceAll("%PLAYER%", player.getDisplayNameString()));
          }
          ServerEssentialsServer.logger.info(
              player.getDisplayNameString() + " has ranked up to " + nextRank
                  .getName());
        } catch (NoSuchElementException ignored) {
        }
      }
    }
  }
}