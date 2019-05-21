package com.wurmcraft.serveressentials.common.modules.autorank.event;

import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.autorank.AutoRankUtils;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class AutoRankEvents {

  @SubscribeEvent
  public void onWorldTick(WorldTickEvent e) {
    if (e.side == Side.SERVER && e.world.getWorldTime() % ConfigHandler.autoRankCheckPeriod == 0) {
      FMLCommonHandler.instance()
          .getMinecraftServerInstance()
          .getPlayerList()
          .getPlayers()
          .forEach(
              player -> {
                Rank rank = AutoRankUtils.getNextRank(player);
                if (rank != null) {
                  UserManager.rankupUser(player, rank);
                }
              });
    }
  }
}
