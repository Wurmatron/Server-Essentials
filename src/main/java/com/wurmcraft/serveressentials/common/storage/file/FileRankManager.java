package com.wurmcraft.serveressentials.common.storage.file;

import com.wurmcraft.serveressentials.api.interfaces.IRankManager;
import com.wurmcraft.serveressentials.api.user.rank.Rank;

public class FileRankManager implements IRankManager {

  public FileRankManager() {
    loadRanks();
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

  private void loadRanks() {
    // TODO Implement Rank Loading
  }
}
