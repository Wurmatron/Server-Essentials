package com.wurmcraft.serveressentials.common.modules.autorank;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.api.user.file.FileUser;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.ServerTime;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.Optional;
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
    for (AutoRank autoRank : AutoRankModule.getAutoRanks()) {
      if (hasRequirments(player, autoRank)) {
        return Optional.of(autoRank)
            .map(rank -> ServerEssentialsAPI.rankManager.getRank(rank.getNextRank()))
            .orElse(null);
      }
    }
    return Optional.<AutoRank>empty()
        .map(rank -> ServerEssentialsAPI.rankManager.getRank(rank.getNextRank()))
        .orElse(null);
  }

  public static AutoRank getNextAutoRank(EntityPlayer player) {
    Rank rank = UserManager.getUserRank(player);
    for (AutoRank autoRank : AutoRankModule.getAutoRanks()) {
      if (autoRank.getRank().equalsIgnoreCase(rank.getID())) {
        return autoRank;
      }
    }
    return null;
  }
}
