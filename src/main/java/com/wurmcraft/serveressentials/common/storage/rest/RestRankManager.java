package com.wurmcraft.serveressentials.common.storage.rest;

import com.wurmcraft.serveressentials.api.interfaces.IRankManager;
import com.wurmcraft.serveressentials.api.user.rank.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class RestRankManager implements IRankManager {

  private static NonBlockingHashMap<String, Rank> rankCache;

  public RestRankManager() {
    rankCache = new NonBlockingHashMap<>();
    loadRestRanks();
  }

  @Override
  public boolean register(Rank rank) {
    return false;
  }

  @Override
  public boolean remove(Rank rank) {
    return false;
  }

  @Override
  public boolean exists(Rank rank) {
    return false;
  }

  @Override
  public boolean exists(String name) {
    return false;
  }

  @Override
  public Rank getRank(String rank) {
    return null;
  }

  private void loadRestRanks() {
    ServerEssentialsServer.instance.executors.scheduleAtFixedRate(
        () -> {
          Rank[] allRanks = RequestGenerator.Rank.getAllRanks();
          for (Rank cache : rankCache.values()) {
            boolean found = false;
            for (Rank rank : allRanks) {
              if (rank.getID().equalsIgnoreCase(cache.getID())) {
                found = true;
              }
            }
            if (!found) {
              ServerEssentialsServer.LOGGER.info(
                  "Rank '" + cache.getID() + " has been removed from the database");
            }
          }
          rankCache.clear();
          Arrays.stream(allRanks)
              .filter(rank -> !register(rank))
              .map(rank -> "Failed to register rank '" + rank.getID() + "'")
              .forEach(ServerEssentialsServer.LOGGER::warn);
        },
        0,
        ConfigHandler.rankSyncPeriod,
        TimeUnit.SECONDS);
  }
}
