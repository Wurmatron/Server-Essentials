package com.wurmcraft.serveressentials.forge.modules.general;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "General")
public class GeneralConfig implements StoredDataType {

  public int tpaTimeout;

  public GeneralConfig() {
    this.tpaTimeout = 150;
  }

  public GeneralConfig(int tpaTimeout) {
    this.tpaTimeout = tpaTimeout;
  }

  @Override
  public String getID() {
    return "General";
  }
}
