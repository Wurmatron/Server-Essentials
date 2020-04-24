package com.wurmcraft.serveressentials.forge.modules.rank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Rank")
public class RankModule {

  public void initSetup() {
    loadRanks();
  }

  public void finalizeModule() {
  }

  private static Rank[] loadRanks() {
    try {
      NonBlockingHashMap<String, Rank> ranks = SECore.dataHandler
          .getDataFromKey(DataKey.RANK, new Rank());
      String defaultRank = ((RankConfig) SERegistry.getStoredData(DataKey.MODULE_CONFIG, "Rank")).defaultRank;
      if (ranks.size() == 0 || ranks.getOrDefault(defaultRank, null) == null) {
        registerDefaultRanks();
        if (ranks.size() == 0) {
          SECore.logger.severe("No Ranks are loading / detected!");
          return new Rank[]{new Rank()};
        }
      }
      return ranks.values().toArray(new Rank[0]);
    } catch (Exception e) {
      // No Ranks in the database
      registerDefaultRanks();
    }
    return new Rank[]{new Rank()};
  }

  private static void registerDefaultRanks() {
    Rank defaultRank = new Rank("Default", "&7[&8Default&7]", "&7", new String[]{},
        new String[]{});
    SECore.dataHandler.registerData(DataKey.RANK, defaultRank);
    Rank adminRank = new Rank("Admin", "&c[&4Admin&c]", "&d", new String[]{"Default"},
        new String[]{"*"});
    SECore.dataHandler.registerData(DataKey.RANK, adminRank);
  }
}
