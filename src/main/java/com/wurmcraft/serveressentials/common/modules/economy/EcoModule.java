package com.wurmcraft.serveressentials.common.modules.economy;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.Currency;
import com.wurmcraft.serveressentials.api.storage.json.Reward;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.economy.event.SignShopEvents;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Economy")
public class EcoModule {

  public Currency[] activeCurrency;

  public void setup() {
    activeCurrency = RequestGenerator.Economy.getAllCurrency();
    DataHelper.load(Storage.REWARD, new Reward());
    if (activeCurrency == null || activeCurrency.length == 0) {
      ServerEssentialsServer.LOGGER.warn("Unable to load / find and currency's");
    }
    MinecraftForge.EVENT_BUS.register(new SignShopEvents());
  }
}
