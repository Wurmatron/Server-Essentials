package com.wurmcraft.serveressentials.common.economy;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.economy.events.MarketEvent;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Economy")
public class EconomyModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new MarketEvent());
  }
}
