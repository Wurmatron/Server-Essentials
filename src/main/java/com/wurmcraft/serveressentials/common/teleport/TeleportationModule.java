package com.wurmcraft.serveressentials.common.teleport;

import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.teleport.events.TeleportTimerEvents;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Teleportation")
public class TeleportationModule implements IModule {

  public static NonBlockingHashMap<Long, EntityPlayer[]> activeRequests = new NonBlockingHashMap<>();

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new TeleportTimerEvents());
  }
}
