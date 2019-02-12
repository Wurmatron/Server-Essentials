package com.wurmcraft.serveressentials.common.economy;

import com.wurmcraft.serveressentials.api.json.user.optional.Currency;
import com.wurmcraft.serveressentials.api.json.user.optional.PlayerBank;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.economy.events.MarketEvent;
import com.wurmcraft.serveressentials.common.rest.RestModule;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import java.util.*;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Economy")
public class EconomyModule implements IModule {

  public static final NonBlockingHashMap<String, Currency> currency = new NonBlockingHashMap<>();
  public static List<PlayerBank> playerBanks = new ArrayList<>();

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new MarketEvent());
    syncCurrency();
    //    syncPlayerBalances();
  }

  public static void syncCurrency() {
    RestModule.EXECUTORs.scheduleAtFixedRate(
        () -> {
          try {
            Currency[] autoCurrency = RequestHelper.EcoResponses.getAllCurrency();
            currency.clear();
            for (Currency c : autoCurrency) {
              currency.put(c.getName(), c);
            }
            if (currency.size() == 0) {
              ServerEssentialsServer.LOGGER.debug("No Currency Found within the database");
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.LOGGER.debug("Synced AutoRanks with REST API");
  }

  //  public static void syncPlayerBalances() {
  //    RestModule.EXECUTORs.scheduleAtFixedRate(
  //        () -> {
  //          try {
  //            PlayerBank[] allPlayerBanks = RequestHelper.EcoResponses.getAllPlayerBanks();
  //            playerBanks = Arrays.asList(allPlayerBanks);
  //            if (currency.size() == 0) {
  //              ServerEssentialsServer.LOGGER.debug("No Player Bank's Found within the database");
  //            }
  //          } catch (Exception e) {
  //            e.printStackTrace();
  //          }
  //        },
  //        0L,
  //        ConfigHandler.syncPeriod,
  //        TimeUnit.MINUTES);
  //    ServerEssentialsServer.LOGGER.debug("Synced AutoRanks with REST API");
  //  }

  public static Currency getCurrency(String name) {
    for (Currency currency : currency.values()) {
      if (currency.getName().equalsIgnoreCase(name)) {
        return currency;
      }
    }
    return null;
  }
}
