package com.wurmcraft.serveressentials.forge.modules.matterlink;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.module.Module;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.modules.matterlink.event.MatterLinkTickEvent;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.MatterBridgeUtils;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "MatterLink")
public class MatterLinkModule {

  public void initSetup() {
    int health = MatterBridgeUtils.getHealth();
    ServerEssentialsServer.logger.info("Bridge Status: " + health);
    MinecraftForge.EVENT_BUS.register(new MatterLinkTickEvent());
  }

  public void finalizeModule() {

  }
}
