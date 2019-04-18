package com.wurmcraft.serveressentials.common.track;

import static com.wurmcraft.serveressentials.common.rest.RestModule.EXECUTORs;

import com.wurmcraft.serveressentials.api.json.rest.ServerStatus;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import java.util.concurrent.TimeUnit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@Module(name = "track")
public class TrackModule implements IModule {

  @Override
  public void setup() {
    MinecraftForge.EVENT_BUS.register(new TrackModule());
  }

  public static ServerStatus createStatus(String status) {
    if (status.equalsIgnoreCase("Online")) {
      return new ServerStatus(
          ConfigHandler.serverName,
          status,
          calculateTPS(),
          FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames(),
          System.currentTimeMillis(),
          getModpackVersion());
    } else {
      return new ServerStatus(
          ConfigHandler.serverName,
          status,
          0,
          new String[0],
          System.currentTimeMillis(),
          getModpackVersion());
    }
  }

  public static void startStatusUpdater() {
    EXECUTORs.scheduleAtFixedRate(
        () -> RequestHelper.TrackResponces.syncServer(createStatus("Online")),
        0L,
        90,
        TimeUnit.SECONDS);
  }

  private static String getModpackVersion() {
    return ConfigHandler.modpackVersion;
  }

  private static double calculateTPS() {
    return getSum(
            FMLCommonHandler.instance()
                .getMinecraftServerInstance()
                .worldTickTimes
                .get(
                    FMLCommonHandler.instance()
                        .getMinecraftServerInstance()
                        .getWorld(0)
                        .provider
                        .getDimension()))
        * 1.0E-006D;
  }

  private static double getSum(long[] times) {
    long timesum = 0L;
    for (long time : times) {
      timesum += time;
    }
    if (times == null) {
      return 0;
    }
    return timesum / times.length;
  }

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    RequestHelper.TrackResponces.syncServer(createStatus("Online"));
  }

  @SubscribeEvent
  public void onPlayerLeave(PlayerLoggedOutEvent e) {
    RequestHelper.TrackResponces.syncServer(createStatus("Online"));
  }
}
