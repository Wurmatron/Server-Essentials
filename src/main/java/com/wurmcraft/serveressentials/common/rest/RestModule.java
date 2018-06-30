package com.wurmcraft.serveressentials.common.rest;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;

@Module(name = "Rest")
public class RestModule implements IModule {

  @Override
  public void setup() {
    if (ConfigHandler.restURL.startsWith("http://") || ConfigHandler.restURL
        .startsWith("https://")) {

    } else {
      ServerEssentialsServer.logger
          .warn("Rest API Unable to load due to invalid Endpoint '" + ConfigHandler.restURL + "'");
    }
  }
}
