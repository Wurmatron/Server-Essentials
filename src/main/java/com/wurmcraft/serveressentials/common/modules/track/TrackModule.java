package com.wurmcraft.serveressentials.common.modules.track;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.api.storage.json.ServerStatus;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import java.util.*;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

@Module(name = "Track")
public class TrackModule {

  public static ServerStatus[] networkStatus = new ServerStatus[0];

  public void setup() {
    MinecraftForge.EVENT_BUS.register(new TrackModule());
  }

  public static ServerStatus createStatus(String status) {
    if (status.equalsIgnoreCase("Online")) {
      return new ServerStatus(
          ConfigHandler.serverName,
          status,
          calculateTPS(),
          getPlayers(),
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

  private static String[] getPlayers() {
    List<String> players = new ArrayList<>();
    for (EntityPlayer player :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
      players.add(formatName(player));
    return players.toArray(new String[0]);
  }

  private static String formatName(EntityPlayer player) {
    return UserManager.getUserRank(player) != null
        ? UserManager.getUserRank(player).getPrefix() + " " + player.getName()
        : player.getName();
  }

  public static void startStatusUpdater() {
    ServerEssentialsServer.instance.executors.scheduleAtFixedRate(
        () -> RequestGenerator.Status.syncServer(createStatus("Online")), 0L, 90, TimeUnit.SECONDS);
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
    RequestGenerator.Status.syncServer(createStatus("Online"));
  }

  @SubscribeEvent
  public void onPlayerLeave(PlayerLoggedOutEvent e) {
    RequestGenerator.Status.syncServer(createStatus("Online"));
  }
}
