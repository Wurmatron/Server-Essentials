package com.wurmcraft.serveressentials.forge.modules.general;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.Warp;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.modules.general.event.GeneralEvents;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "General", moduleDependencies = {"Rank", "Language"})
public class GeneralModule {

  public void initSetup(){
    MinecraftForge.EVENT_BUS.register(new GeneralEvents());
    SECore.dataHandler.getDataFromKey(DataKey.WARP,new Warp());
  }

  public void finalizeModule() {

  }
}
