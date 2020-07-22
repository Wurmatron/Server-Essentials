package com.wurmcraft.serveressentials.forge.modules.autorank.event;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_COLOR;
import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.utils.PlayerUtils;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.economy.EcoUtils;
import com.wurmcraft.serveressentials.forge.modules.rank.RankUtils;
import java.util.NoSuchElementException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RankupEvents {

  @SubscribeEvent
  public void onWorldUpdate(WorldTickEvent e) {
    if (e.side == Side.SERVER && e.phase == Phase.END) {
      if (e.world.getWorldTime() % 24000 == 0) { // 1 in-game day (20m)
        for (EntityPlayer player : FMLCommonHandler.instance()
            .getMinecraftServerInstance().getPlayerList().getPlayers()) {
          String rank = checkForRankup(player);
          if (!rank.isEmpty()) {
            Rank nextRank = (Rank) SERegistry.getStoredData(DataKey.RANK, rank);
            if (nextRank != null) {
              RankUtils.setRank(player, nextRank);
              player.sendMessage(new TextComponentString(COMMAND_COLOR +
                  PlayerUtils.getUserLanguage(player).AUTORANK_RANK_UP
                      .replaceAll("%RANK%",
                          COMMAND_INFO_COLOR + nextRank.getName() + COMMAND_COLOR)));
            }
          }
        }
      }
    }
  }

  public static String checkForRankup(EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      PlayerDataEvents.handAndCheckForErrors(player);
      for (AutoRank rank : SECore.dataHandler.getDataFromKey(DataKey.AUTO_RANK, new AutoRank()).values()) {
        if (playerData.global.rank.equalsIgnoreCase(rank.rank) && canRankup(rank,
            playerData,
            player)) {
          return rank.nextRank;
        }
      }
    } catch (NoSuchElementException e) {
      PlayerDataEvents.handAndCheckForErrors(player);
    }
    return "";
  }


  private static boolean canRankup(AutoRank rank, StoredPlayer playerData,
      EntityPlayer player) {
    return EcoUtils.hasTheMoney(playerData.global.wallet, rank.balance)
        && rank.exp <= player.experienceLevel && rank.playTime <= PlayerUtils
        .getTotalPlaytime(player, playerData);
  }
}