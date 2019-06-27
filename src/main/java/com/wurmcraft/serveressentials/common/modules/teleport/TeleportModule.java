package com.wurmcraft.serveressentials.common.modules.teleport;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.LocationWithName;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

@Module(name = "Teleportation")
public class TeleportModule {

  public static NonBlockingHashMap<UUID, EntityPlayer> tpaRequests = new NonBlockingHashMap<>();
  private static NonBlockingHashMap<Long, UUID> timeoutTimmer = new NonBlockingHashMap<>();

  public void setup() {
    MinecraftForge.EVENT_BUS.register(new TeleportModule());
    DataHelper.load(Storage.WARP, new LocationWithName[0], new LocationWithName());
  }

  public static void addRequest(UUID user, EntityPlayer sender) {
    tpaRequests.put(user, sender);
    timeoutTimmer.put(System.currentTimeMillis() + ConfigHandler.tpaTimeout, user);
  }

  @SubscribeEvent
  public void onWorldTick(WorldTickEvent e) {
    if ((e.world.getWorldTime() % (ConfigHandler.userDataSyncPeriod) / 5) == 0) {
      for (Long time : timeoutTimmer.keySet()) {
        if (time < System.currentTimeMillis()) {
          tpaRequests.remove(timeoutTimmer.get(time));
          timeoutTimmer.remove(time);
        }
      }
    }
  }
}
