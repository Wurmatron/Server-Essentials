package com.wurmcraft.serveressentials.common.modules.economy;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.Currency;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;

@Module(name = "Economy")
public class EcoModule {

  public Currency[] activeCurrency;

  public void setup() {
    activeCurrency = RequestGenerator.Economy.getAllCurrency();
    if (activeCurrency == null || activeCurrency.length == 0) {
      ServerEssentialsServer.LOGGER.warn("Unable to load / find and currency's");
    }
  }
}
