package com.wurmcraft.serveressentials.common.teleport;

import com.wurmcraft.serveressentials.api.module.DelayedTeleport;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.teleport.events.TeleportEvents;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Teleportation")
public class TeleportationModule implements IModule {

  public static final NonBlockingHashMap<Long, EntityPlayer[]> activeRequests =
      new NonBlockingHashMap<>();
  public static NonBlockingHashMap<Long, DelayedTeleport> teleportDelay =
      new NonBlockingHashMap<>();

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new TeleportEvents());
  }

  public static void cancelRequest(EntityPlayer player) {
    Iterator<Long> it = teleportDelay.keySet().iterator();
    while (it.hasNext()) {
      long time = it.next();
      DelayedTeleport tp = teleportDelay.get(time);
      if (tp.A.getGameProfile().getId().equals(player.getGameProfile().getId())) {
        teleportDelay.remove(time);
      }
    }
  }
}
