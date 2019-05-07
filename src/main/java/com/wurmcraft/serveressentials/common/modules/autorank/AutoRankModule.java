package com.wurmcraft.serveressentials.common.modules.autorank;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.common.modules.autorank.events.AutoRankEvents;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "AutoRank")
public class AutoRankModule {

  private static NonBlockingHashMap<String, AutoRank> autoRankCache = new NonBlockingHashMap<>();

  public void setup() {
    AutoRank[] rank = loadAutoRanks();
    for (AutoRank autorank : rank) {
      autoRankCache.put(autorank.getRank(), autorank);
    }
    MinecraftForge.EVENT_BUS.register(new AutoRankEvents());
  }

  public static AutoRank[] loadAutoRanks() {
    if (ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      return getRestAutoRank();
    } else if (ServerEssentialsAPI.storageType.equalsIgnoreCase("File")) {
      return getFileAutoRank();
    }
    return new AutoRank[0];
  }

  private static AutoRank[] getFileAutoRank() {
    return DataHelper.load(Storage.AUTO_RANK, new AutoRank[0]);
  }

  private static AutoRank[] getRestAutoRank() {
    return RequestGenerator.AutoRankResponses.getAllAutoRanks();
  }

  public static AutoRank[] getAutoRanks() {
    return autoRankCache.values().toArray(new AutoRank[0]);
  }

  public static AutoRank getAutoRank(String id) {
    return autoRankCache.getOrDefault(id, null);
  }
}
