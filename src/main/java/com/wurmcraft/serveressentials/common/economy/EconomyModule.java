package com.wurmcraft.serveressentials.common.economy;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.economy.events.MarketEvent;
import java.util.HashMap;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Economy")
public class EconomyModule implements IModule {

  public static HashMap<String, Double> activeCurrency = new HashMap<>();

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new MarketEvent());
    for (String coin : ConfigHandler.activeCurrency) {
      if (coin.contains(":")) {
        activeCurrency.put(
            coin.substring(0, coin.indexOf(":")),
            Double.parseDouble(coin.substring(coin.indexOf(":") + 1, coin.length())));
      } else {
        activeCurrency.put(coin, 1.0);
      }
    }
  }

  public static boolean isValidCurrrency(String name) {
    for (String curr : activeCurrency.keySet()) {
      if (curr.equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  public static double exchangeCurrency(String currentCoin, int amount, String newCoin) {
    if (newCoin.equalsIgnoreCase(ConfigHandler.globalCurrency)) {
      return amount * getExchangeRate(ConfigHandler.globalCurrency);
    } else {
      double globalRate = exchangeCurrency(currentCoin, amount, ConfigHandler.globalCurrency);
      return (amount * globalRate) * getExchangeRate(newCoin);
    }
  }

  private static double getExchangeRate(String currency) {
    for (String coin : activeCurrency.keySet()) {
      if (currency.equalsIgnoreCase(coin)) {
        return activeCurrency.get(coin);
      }
    }
    return 1.0;
  }
}
