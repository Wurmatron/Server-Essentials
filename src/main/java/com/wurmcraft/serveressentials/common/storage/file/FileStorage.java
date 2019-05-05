package com.wurmcraft.serveressentials.common.storage.file;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.storage.Storage;
import net.minecraftforge.common.MinecraftForge;

public class FileStorage implements Storage {

  @Override
  public void setup() {
    ServerEssentialsAPI.rankManager = new FileRankManager();
    MinecraftForge.EVENT_BUS.register(new FileWorldEvents());
  }
}
