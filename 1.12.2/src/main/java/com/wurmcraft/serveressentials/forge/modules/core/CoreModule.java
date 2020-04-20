package com.wurmcraft.serveressentials.forge.modules.core;


import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Core", shouldAllaysBeLoaded = true)
public class CoreModule {

  public void initSetup() {
    MinecraftForge.EVENT_BUS.register(new PlayerDataEvents());
  }

  public void finalizeModule() {}

}
