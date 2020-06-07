package com.wurmcraft.serveressentials.forge.modules.autorank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.AutoRank;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.modules.autorank.event.RankupEvents;
import com.wurmcraft.serveressentials.forge.modules.rank.RankConfig;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "AutoRank", moduleDependencies = {"Rank"})
public class AutoRankModule {

  public void initSetup() {
    loadAutoRanks();
    MinecraftForge.EVENT_BUS.register(new RankupEvents());
  }

  public void finalizeModule() {

  }

  private static Rank[] loadAutoRanks() {
    try {
      NonBlockingHashMap<String, AutoRank> ranks = SECore.dataHandler
          .getDataFromKey(DataKey.AUTO_RANK, new AutoRank());
      String defaultRank = ((RankConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Rank")).defaultRank;
      if (ranks.size() == 0 || ranks.getOrDefault(defaultRank, null) == null) {
        createDefaultRankUps();
        if (ranks.size() == 0) {
          SECore.logger.severe("No Ranks are loading / detected!");
          return new Rank[]{new Rank()};
        }
      }
      return ranks.values().toArray(new Rank[0]);
    } catch (Exception e) {
      // No Auto-Ranks in the database
      createDefaultRankUps();
    }
    return new Rank[]{new Rank()};
  }

  private static void createDefaultRankUps() {
    AutoRank memberRankup = new AutoRank(300,0,0,"Default","Member");
    SECore.dataHandler.registerData(DataKey.AUTO_RANK, memberRankup);
    if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      RestRequestGenerator.AutoRank.addAutoRank(memberRankup);
    }
  }
}
