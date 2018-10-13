package com.wurmcraft.serveressentials.common.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.List;

public class RankUtils {

  private RankUtils() {}

  private static Rank[] getAllLower(Rank rank, List<Rank> ranks) {
    if (rank.getName().equalsIgnoreCase(ConfigHandler.defaultRank)
        || rank.getInheritance() == null) {
      return new Rank[0];
    }
    if (ranks == null) {
      ranks = new ArrayList<>();
    }
    for (String r : rank.getInheritance()) {
      if (UserManager.getRank(r) != null) {
        Rank sub = UserManager.getRank(r);
        ranks.add(sub);
        if (!sub.getName().equalsIgnoreCase(ConfigHandler.defaultRank)
            || sub.getInheritance() != null) {
          getAllLower(rank, ranks);
        }
      }
    }
    if (ranks.isEmpty()) {
      return ranks.toArray(new Rank[0]);
    }
    return new Rank[0];
  }

  public static boolean isMore(Rank higherRank, Rank lowerRank) {
    if (higherRank.getName().equalsIgnoreCase(ConfigHandler.defaultRank)
        || higherRank.getInheritance() == null) {
      return false;
    }
    Rank[] lower = getAllLower(higherRank, null);
    if (lower != null && lower.length > 0) {
      for (Rank rank : lower) {
        if (rank.getName().equalsIgnoreCase(lowerRank.getName())) {
          return true;
        }
      }
    }
    return false;
  }
}
