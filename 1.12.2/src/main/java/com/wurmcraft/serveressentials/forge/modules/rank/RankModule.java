package com.wurmcraft.serveressentials.forge.modules.rank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
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
      String defaultRank = ((RankConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Rank")).defaultRank;
      if (ranks.size() == 0 || ranks.getOrDefault(defaultRank, null) == null) {
        registerDefaultRanks();
        if (ranks.size() == 0) {
          ServerEssentialsServer.logger.fatal("No Ranks are loading / detected!");
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
        new String[]{
            "economy.pay",
            "economy.perk.buy",
            "economy.perk.list",
            "general.back",
            "general.back.death",
            "general.delhome",
            "general.home",
            "general.sethome",
            "general.tpaccept",
            "general.tpa",
            "language.lang.change",
            "discord.verify",
            "ftbutils.claim.25",
            "eco.adminsign.buy",
            "eco.adminsign.sell"});
    SECore.dataHandler.registerData(DataKey.RANK, defaultRank);
    Rank memberRank = new Rank("Member", "&7[&aMember&7]", "&7", new String[]{"Default"},
        new String[]{});
    SECore.dataHandler.registerData(DataKey.RANK, memberRank);
    Rank adminRank = new Rank("Admin", "&c[&4Admin&c]", "&d", new String[]{"Default"},
        new String[]{"*", "ftbutils.claim.50"});
    SECore.dataHandler.registerData(DataKey.RANK, adminRank);
    if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Rank.addRank(defaultRank);
      RestRequestGenerator.Rank.addRank(memberRank);
      RestRequestGenerator.Rank.addRank(adminRank);
    }
  }
}
