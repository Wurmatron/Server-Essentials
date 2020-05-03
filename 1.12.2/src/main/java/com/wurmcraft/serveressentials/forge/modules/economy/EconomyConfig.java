package com.wurmcraft.serveressentials.forge.modules.economy;

import com.wurmcraft.serveressentials.core.api.data.StoredDataType;
import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.module.ConfigModule;

@ConfigModule(moduleName = "Economy")
public class EconomyConfig implements StoredDataType {

  public Coin defaultServerCurrency;
  public double homeLevelCost;
  public double claimLevelCost;
  public int claimAmountPerLevel;
  public int echestPerkCost;

  public EconomyConfig() {
    defaultServerCurrency = new Coin("Default", 100);
    this.homeLevelCost = 50000;
    this.claimLevelCost = 1000;
    this.claimAmountPerLevel = 1;
    this.echestPerkCost = 4000;
  }

  public EconomyConfig(
      Coin defaultServerCurrency, double homeLevelCost, double claimLevelCost,
      int claimAmountPerLevel, int echestPerkCost) {
    this.defaultServerCurrency = defaultServerCurrency;
    this.homeLevelCost = homeLevelCost;
    this.claimLevelCost = claimLevelCost;
    this.claimAmountPerLevel = claimAmountPerLevel;
    this.echestPerkCost = echestPerkCost;
  }

  @Override
  public String getID() {
    return "Economy";
  }
}
