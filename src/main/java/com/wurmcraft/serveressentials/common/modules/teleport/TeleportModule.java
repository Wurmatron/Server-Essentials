package com.wurmcraft.serveressentials.common.modules.teleport;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.LocationWithName;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;

@Module(name = "Teleportation")
public class TeleportModule {

  public void setup() {
    DataHelper.load(Storage.WARP, new LocationWithName[0], new LocationWithName());
  }
}
