package com.wurmcraft.serveressentials.common.modules.transfer;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;

@Module(name = "Transfer")
public class TransferModule {

  public void setup() {
    if (ConfigHandler.transferID.isEmpty()) {
      ServerEssentialsServer.LOGGER.warn("Transfer Module cannot work without an transfer ID");
    }
  }
}
