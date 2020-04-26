package com.wurmcraft.serveressentials.forge.modules.track;

import com.wurmcraft.serveressentials.core.api.track.TrackingStatus;
import com.wurmcraft.serveressentials.core.api.track.TrackingStatus.Status;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.time.Instant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TrackUtils {

  public static TrackingStatus createStatus(Status status) {
    return new TrackingStatus(SERegistry.globalConfig.serverID, status,
        status == Status.ONLINE ? generatePlayerList(status) : new String[0], SERegistry.globalConfig.modpackVersion, status == Status.ONLINE ? calculateMS() : Integer.MAX_VALUE,
        Instant.now().getEpochSecond());
  }

  private static String[] generatePlayerList(Status status) {
    if (status == Status.ONLINE) {
      PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance()
          .getPlayerList();
      if (players.getCurrentPlayerCount() > 0 && !players.getPlayers().isEmpty()) {
        String[] playerArr = new String[players.getPlayers().size()];
        for (int i = 0; i < playerArr.length; i++) {
          playerArr[i] = formatPlayer(players.getPlayers().get(i));
        }
        return playerArr;
      }
    }
    return new String[]{};
  }

  private static String formatPlayer(EntityPlayer player) {
    return player.getDisplayNameString() + " (" + player.getGameProfile().getId() + ")";
  }

  private static double calculateMS() {
    return getSum(
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .worldTickTimes
            .get(
                FMLCommonHandler.instance()
                    .getMinecraftServerInstance()
                    .getWorld(0)
                    .provider
                    .getDimension()))
        * 1.0E-006D;
  }

  private static double getSum(long[] times) {
    long timesum = 0L;
    for (long time : times) {
      timesum += time;
    }
    if (times == null) {
      return 0;
    }
    return timesum / times.length;
  }

}
