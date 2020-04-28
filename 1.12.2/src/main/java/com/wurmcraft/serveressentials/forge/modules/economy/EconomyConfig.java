package com.wurmcraft.serveressentials.forge.modules.economy;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Economy")
public class EconomyConfig implements StoredDataType {

  public Coin defaultServerCurrency;

  public EconomyConfig() {
    defaultServerCurrency = new Coin("Default", 100);
  }

  public EconomyConfig(
      Coin defaultServerCurrency) {
    this.defaultServerCurrency = defaultServerCurrency;
  }

  @Override
  public String getID() {
    return "Economy";
  }
}
