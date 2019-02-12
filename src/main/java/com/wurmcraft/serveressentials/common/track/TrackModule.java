package com.wurmcraft.serveressentials.common.track;

import static com.wurmcraft.serveressentials.common.rest.RestModule.EXECUTORs;

import com.wurmcraft.serveressentials.api.json.rest.ServerStatus;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Module(name = "track")
public class TrackModule implements IModule {

  @Override
  public void setup() {
    EXECUTORs.scheduleAtFixedRate(
        () -> RequestHelper.TrackResponces.syncServer(createStatus("Online")),
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
  }

  public static ServerStatus createStatus(String status) {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    double tickSum = 0;
    for (int time = 0; time < server.tickTimeArray.length; ++time) {
      tickSum += server.tickTimeArray[time];
    }
    tickSum /= server.tickTimeArray.length;
    double tps = 1000000000 / tickSum;
    return new ServerStatus(
        ConfigHandler.serverName,
        status,
        tps,
        FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames(),
        System.currentTimeMillis());
  }
}
