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
    if (rankCache.containsKey(rank.getID())) {
      return false;
    }
    ServerEssentialsServer.LOGGER.debug("Registering rank '" + rank.getID() + "'");
    rankCache.put(rank.getID(), rank);
    return true;
  }

  @Override
  public boolean remove(Rank rank) {
    if (rankCache.containsKey(rank.getID())) {
      rankCache.remove(rank.getID());
      return true;
    }
    return false;
  }

  @Override
  public boolean exists(Rank rank) {
    return exists(rank.getID()) || exists(rank.getName());
  }

  @Override
  public boolean exists(String name) {
    return rankCache.keySet().stream().anyMatch(key -> key.equals(name));
  }

  @Override
  public Rank getRank(String rank) {
    return rankCache.getOrDefault(rank, null);
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

  @Override
  public Rank[] getRanks() {
    return rankCache.values().toArray(new Rank[0]);
  }
}
