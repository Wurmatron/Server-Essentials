package com.wurmcraft.serveressentials.forge.modules.rank;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import java.util.concurrent.TimeUnit;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class RankUtils {

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
