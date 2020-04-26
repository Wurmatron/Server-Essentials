package com.wurmcraft.serveressentials.forge.modules.track;

import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.forge.modules.track.event.TrackEvents;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Track")
public class TrackModule {

  public void initSetup() {
    MinecraftForge.EVENT_BUS.register(new TrackEvents());
  }

  public void finalizeModule() {

  }

}
