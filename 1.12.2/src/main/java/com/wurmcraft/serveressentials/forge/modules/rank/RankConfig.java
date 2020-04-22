package com.wurmcraft.serveressentials.forge.modules.rank;


import static com.wurmcraft.serveressentials.forge.modules.rank.RankUtils.scheduleRankUpdates;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Rank")
public class RankConfig implements StoredDataType {

  public int rankSyncPeriod;
  public String defaultRank;

  public RankConfig() {
    this.rankSyncPeriod = 90;
    this.defaultRank = "Default";
    scheduleRankUpdates(rankSyncPeriod);
  }

  public RankConfig(int rankSyncPeriod, String defaultRank) {
    this.rankSyncPeriod = rankSyncPeriod;
    this.defaultRank = defaultRank;
    scheduleRankUpdates(rankSyncPeriod);
  }

  @Override
  public String getID() {
    return "Rank";
  }
}
