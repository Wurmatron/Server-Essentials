package com.wurmcraft.serveressentials.common.claim;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.claim.events.ClaimEvent;
import net.minecraftforge.common.MinecraftForge;

@Deprecated
@Module(name = "Claim")
public class ClaimModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new ClaimEvent());
    ChunkManager.loadAllClaims();
  }
}
