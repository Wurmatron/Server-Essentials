package com.wurmcraft.serveressentials.common.storage.file;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.storage.Storage;

public class FileStorage implements Storage {

  @Override
  public void setup() {
    ServerEssentialsAPI.rankManager = new FileRankManager();
  }
}
