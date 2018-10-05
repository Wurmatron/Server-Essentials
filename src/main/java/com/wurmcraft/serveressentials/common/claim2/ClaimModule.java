package com.wurmcraft.serveressentials.common.claim2;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.claim2.events.ClaimEvent;
import com.wurmcraft.serveressentials.common.claim2.events.DisplayCreateClaimEvent;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "Claim2")
public class ClaimModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new ClaimEvent());
    MinecraftForge.EVENT_BUS.register(new DisplayCreateClaimEvent());
  }
}
