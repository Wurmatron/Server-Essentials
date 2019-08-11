package com.wurmcraft.serveressentials.common.modules.discord;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.module.Module;

@Module(name = "Discord")
public class DiscordModule {

  public void setup() {
    if (!ServerEssentialsAPI.storageType.equalsIgnoreCase("Rest")) {
      throw new NullPointerException("Unable to function without the Rest Database!");
    }
  }
}
