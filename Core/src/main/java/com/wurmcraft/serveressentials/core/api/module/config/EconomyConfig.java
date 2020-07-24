package com.wurmcraft.serveressentials.core.api.module.config;

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

  public CommandCost[] commandCost;

  public EconomyConfig() {
    defaultServerCurrency = new Coin("Default", 100);
    this.homeLevelCost = 50000;
    this.claimLevelCost = 1000;
    this.claimAmountPerLevel = 1;
    this.echestPerkCost = 4000;
    this.commandCost = new CommandCost[] {new CommandCost("skull", 5)};
  }

  public EconomyConfig(
      Coin defaultServerCurrency,
      double homeLevelCost,
      double claimLevelCost,
      int claimAmountPerLevel,
      int echestPerkCost,
      CommandCost[] commandCost) {
    this.defaultServerCurrency = defaultServerCurrency;
    this.homeLevelCost = homeLevelCost;
    this.claimLevelCost = claimLevelCost;
    this.claimAmountPerLevel = claimAmountPerLevel;
    this.echestPerkCost = echestPerkCost;
    this.commandCost = commandCost;
  }

  @Override
  public String getID() {
    return "Economy";
  }
}
