package com.wurmcraft.serveressentials.forge.modules.general;

import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "General")
public class GeneralConfig implements StoredDataType {

  public int tpaTimeout;
  public String defaultHome;
  public int startingHomeAmount;
  public String[] motd;
  public String[] rules;
  public LocationWrapper spawn;

  public GeneralConfig() {
    this.tpaTimeout = 150;
    this.defaultHome = "home";
    this.startingHomeAmount = 1;
    motd = new String[] {};
    rules = new String[] {};
    spawn = new LocationWrapper(0,64,0);
  }

  public GeneralConfig(int tpaTimeout, String defaultHome, int startingHomeAmount,
      String[] motd, String[] rules,
      LocationWrapper spawn) {
    this.tpaTimeout = tpaTimeout;
    this.defaultHome = defaultHome;
    this.startingHomeAmount = startingHomeAmount;
    this.motd = motd;
    this.rules = rules;
    this.spawn = spawn;
  }

  @Override
  public String getID() {
    return "General";
  }
}
