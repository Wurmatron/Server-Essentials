package com.wurmcraft.serveressentials.common.modules.autorank;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;

public class AutoRankUtils {

  public static boolean hasRequirments(EntityPlayer player, AutoRank auto) {
    return auto.getBalance() <= UserManager.getServerCurrency(player.getGameProfile().getId())
        && auto.getExp() <= player.experienceLevel
        && auto.getRank()
            .equals(UserManager.getUserRank(player.getGameProfile().getId().toString()).getID());
  }

  public static Rank getNextRank(EntityPlayer player) {
    return Arrays.stream(AutoRankModule.getAutoRanks())
        .filter(rank -> hasRequirments(player, rank))
        .findFirst()
        .map(rank -> ServerEssentialsAPI.rankManager.getRank(rank.getNextRank()))
        .orElse(null);
  }
}
