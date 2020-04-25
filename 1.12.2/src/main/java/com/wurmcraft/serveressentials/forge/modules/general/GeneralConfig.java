package com.wurmcraft.serveressentials.forge.modules.general;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "General")
public class GeneralConfig implements StoredDataType {

  public int tpaTimeout;
  public String defaultHome;
  public int startingHomeAmount;

  public GeneralConfig() {
    this.tpaTimeout = 150;
    this.defaultHome = "home";
    this.startingHomeAmount = 1;
  }

  public GeneralConfig(int tpaTimeout, String defaultHome, int maxHomes) {
    this.tpaTimeout = tpaTimeout;
    this.defaultHome = defaultHome;
    this.startingHomeAmount = maxHomes;
  }

  @Override
  public String getID() {
    return "General";
  }
}
