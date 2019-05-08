package com.wurmcraft.serveressentials.common.modules.autorank;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.ServerTime;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;

public class AutoRankUtils {

  public static boolean hasRequirments(EntityPlayer player, AutoRank auto) {
    return auto.getBalance() <= UserManager.getServerCurrency(player.getGameProfile().getId())
        && auto.getExp() <= player.experienceLevel
        && auto.getRank()
            .equals(UserManager.getUserRank(player.getGameProfile().getId().toString()).getID())
        && getTotalPlayTime(player) >= auto.getPlayTime();
  }

  private static int getTotalPlayTime(EntityPlayer player) {
    int totalTime = 0;
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      GlobalRestUser user = (GlobalRestUser) UserManager.getUserData(player)[0];
      for (ServerTime time : user.getServerData()) {
        totalTime += time.getOnlineTime();
      }
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      FileUser user = (FileUser) UserManager.getUserData(player)[0];
      return user.getOnlineTime();
    }
    return totalTime;
  }

  public static Rank getNextRank(EntityPlayer player) {
    return Arrays.stream(AutoRankModule.getAutoRanks())
        .filter(rank -> hasRequirments(player, rank))
        .findFirst()
        .map(rank -> ServerEssentialsAPI.rankManager.getRank(rank.getNextRank()))
        .orElse(null);
  }
}
