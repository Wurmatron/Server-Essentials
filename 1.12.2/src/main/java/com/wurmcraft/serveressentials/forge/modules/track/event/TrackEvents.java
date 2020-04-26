package com.wurmcraft.serveressentials.forge.modules.track.event;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.api.track.NetworkTime;
import com.wurmcraft.serveressentials.core.api.track.ServerTime;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class TrackEvents {

  public static NonBlockingHashMap<String, Long> playerJoinTimes = new NonBlockingHashMap<>();

  @SubscribeEvent
  public void playerLogin(PlayerLoggedInEvent e) {
    playerJoinTimes
        .put(e.player.getGameProfile().getId().toString(), System.currentTimeMillis());
  }

  @SubscribeEvent
  public void playerLogout(PlayerLoggedOutEvent e) {
    updatePlayerTracking(e.player);
    playerJoinTimes.remove(e.player.getGameProfile().getId().toString());
  }

  public static void updatePlayerTracking(EntityPlayer player) {
    SECore.executors.schedule(() -> {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        GlobalPlayer globalData = RestRequestGenerator.User
            .getPlayer(player.getGameProfile().getId().toString());
        if (globalData != null) {
          if (globalData.playtime == null || globalData.playtime.serverTime == null) {
            globalData.playtime = new NetworkTime(new ServerTime[0]);
          }
          int foundIndex = -1;
          for (int i = 0; i < globalData.playtime.serverTime.length; i++) {
            if (globalData.playtime.serverTime[i].serverID
                .equalsIgnoreCase(SERegistry.globalConfig.serverID)) {
              foundIndex = i;
            }
          }
          if (foundIndex >= 0) { // Override existing ServerTime
            globalData.playtime.serverTime[foundIndex].time +=
                ((System.currentTimeMillis() - playerJoinTimes
                    .get(player.getGameProfile().getId().toString())) / 60) / 1000;
          } else { // Create new ServerTime
            globalData.playtime.serverTime = Arrays
                .copyOf(globalData.playtime.serverTime,
                    globalData.playtime.serverTime.length + 1);
            globalData.playtime.serverTime[globalData.playtime.serverTime.length
                - 1] = new ServerTime(SERegistry.globalConfig.serverID,
                ((System.currentTimeMillis() - playerJoinTimes
                    .get(player.getGameProfile().getId().toString())) / 60) / 1000);
          }
          playerJoinTimes.remove(player.getGameProfile().getId().toString());
          if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
            RestRequestGenerator.User
                .overridePlayer(player.getGameProfile().getId().toString(), globalData);
          }
          playerData.global = globalData;
          SERegistry.register(DataKey.PLAYER, playerData);
          playerJoinTimes.put(player.getGameProfile().getId().toString(),
              System.currentTimeMillis());
        }
      } catch (NoSuchElementException ignored) {
      }
    }, 0, TimeUnit.MILLISECONDS);
  }

}
