package com.wurmcraft.serveressentials.forge.modules.rank;


import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.ModuleConfig;
import java.util.concurrent.TimeUnit;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@ModuleConfig(moduleName = "Rank")
public class RankConfig implements StoredDataType {

  public int rankSyncPeriod;

  public RankConfig() {
    this.rankSyncPeriod = 90;
    scheduleRankUpdates(rankSyncPeriod);
  }

  public RankConfig(int rankSyncPeriod) {
    this.rankSyncPeriod = rankSyncPeriod;
    scheduleRankUpdates(rankSyncPeriod);
  }

  @Override
  public String getID() {
    return "Rank";
  }

  public static void scheduleRankUpdates(int delayPeriod) {
    SECore.executors.scheduleAtFixedRate(() -> {
      NonBlockingHashMap<String, Rank> data = SECore.dataHandler
          .getDataFromKey(DataKey.RANK, new Rank());
      if (data.isEmpty()) {
        SECore.logger.warning("Connection to rank database is broken!");
      }
    }, 300, delayPeriod, TimeUnit.SECONDS);
  }
}
