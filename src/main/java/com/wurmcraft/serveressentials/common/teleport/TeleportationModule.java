package com.wurmcraft.serveressentials.common.teleport;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;

@Module(name = "Teleportation")
public class TeleportationModule implements IModule {

  public static HashMap<Long, EntityPlayer[]> activeRequests =  new HashMap<>();

  @Override
  public void setup() {}
}
