package com.wurmcraft.serveressentials.forge.modules.economy;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.eco.Currency;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.api.module.config.EconomyConfig;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.modules.economy.event.AdminSignEvents;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Economy")
public class EconomyModule {

  public void initSetup() {
    loadEconomyCurrencies();
    MinecraftForge.EVENT_BUS.register(new AdminSignEvents());
  }

  public void finalizeModule() {

  }

  private static Currency[] loadEconomyCurrencies() {
    try {
      NonBlockingHashMap<String, Currency> currencies = SECore.dataHandler
          .getDataFromKey(DataKey.CURRENCY, new Currency());
      String defaultRank = ((EconomyConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Economy")).defaultServerCurrency.name;
      if (currencies.size() == 0 || currencies.getOrDefault(defaultRank, null) == null) {
        registerDefaultCurrency();
        if (currencies.size() == 0) {
          ServerEssentialsServer.logger.fatal("No Currencies are loading / detected!");
          return new Currency[]{new Currency()};
        }
      }
      return currencies.values().toArray(new Currency[0]);
    } catch (Exception e) {
      // No Currency in the database
      registerDefaultCurrency();
    }
    return new Currency[]{new Currency()};
  }

  private static void registerDefaultCurrency() {
    Currency defaultCurrency = new Currency("Default", 1.0);
    SECore.dataHandler.registerData(DataKey.CURRENCY, defaultCurrency);
    if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
      RestRequestGenerator.Economy.addCurrency(defaultCurrency);
    }
  }
}
