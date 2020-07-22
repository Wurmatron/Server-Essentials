package com.wurmcraft.serveressentials.forge.modules.discord;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;

@Module(name = "Discord")
public class DiscordModule {

  public void initSetup() {
    if (!SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      ServerEssentialsServer.logger.warn("Discord module requires Rest storage to function");
    }
  }

  public void finalizeModule() {

  }
}
