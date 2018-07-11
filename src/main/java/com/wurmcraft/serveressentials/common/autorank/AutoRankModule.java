package com.wurmcraft.serveressentials.common.autorank;

import static com.wurmcraft.serveressentials.common.ConfigHandler.saveLocation;
import static com.wurmcraft.serveressentials.common.reference.Keys.AUTO_RANK;

import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.reference.Keys;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Module(name = "AutoRank")
public class AutoRankModule implements IModule {

  public static void syncAutoRanks() {
    RestModule.executors.scheduleAtFixedRate(
        () -> {
          try {
            AutoRank[] allAutoRanks = RequestHelper.AutoRankResponses.getAllAutoRanks();
            UserManager.autoRankCache.clear();
            for (AutoRank rank : allAutoRanks) {
              UserManager.autoRankCache.put(rank.getID(), rank);
            }
            if (UserManager.rankCache.size() == 0) {
              ServerEssentialsServer.logger.debug("No AutoRank Found within the database");
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.logger.debug("Synced AutoRanks with REST API");
  }

  private static void loadAutoRanks() {
    File autoRankDir = new File(saveLocation + File.separator + AUTO_RANK.name());
    if (autoRankDir.exists()) {
      for (File file : Objects.requireNonNull(autoRankDir.listFiles())) {
        AutoRank rank = DataHelper.load(file, Keys.AUTO_RANK, new AutoRank());
        UserManager.autoRankCache.put(rank.getID(), rank);
      }
    } else {
      autoRankDir.mkdirs();
    }
  }

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("Rest")) {
      //      syncAutoRanks();
    } else if (ConfigHandler.storageType.equalsIgnoreCase("File")) {
      loadAutoRanks();
    }
  }
}
