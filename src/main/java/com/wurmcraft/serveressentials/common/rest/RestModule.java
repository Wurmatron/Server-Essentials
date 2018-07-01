package com.wurmcraft.serveressentials.common.rest;

import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.rest.events.WorldEvent;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Response;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Module(name = "Rest")
public class RestModule implements IModule {

  private static ScheduledExecutorService executors = Executors.newScheduledThreadPool(1);

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("rest")) {
      if (ConfigHandler.restURL.startsWith("http://")
          || ConfigHandler.restURL.startsWith("https://")) {
        MinecraftForge.EVENT_BUS.register(new WorldEvent());
        ServerEssentialsServer.logger.info("Creating Default Ranks");
        syncRanks();
      } else {
        ServerEssentialsServer.logger.warn(
            "Rest API Unable to load due to invalid Endpoint '" + ConfigHandler.restURL + "'");
      }
    } else {
      ServerEssentialsServer.logger.debug(
          "Rest API is enabled but not used for UserData or Ranks'" + ConfigHandler.restURL + "'");
    }
  }

  public static void syncRanks() {
    executors.scheduleAtFixedRate(
        () -> {
          try {
            Rank[] allRanks = RequestHelper.RankResponses.getAllRanks();
            UserManager.rankCache.clear();
            for (Rank rank : allRanks) {
              UserManager.rankCache.put(rank.getName(), rank);
            }
            if (UserManager.rankCache.size() == 0) {
              createDefaultRanks();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.logger.debug("Synced Ranks with REST API");
  }

  public static void syncPlayer(UUID uuid) {
    executors.scheduleAtFixedRate(
        () -> {
          try {
            GlobalUser globalUser = RequestHelper.UserResponses.getPlayerData(uuid);
            if (globalUser == null) {
              createNewUser(uuid);
            } else {
              UserManager.playerData.put(
                  uuid,
                  new Object[]{
                      globalUser,
                      UserManager.playerData.getOrDefault(uuid, new Object[]{globalUser, null})[1]
                  });
              UserManager.userRanks.put(uuid, UserManager.getRank(globalUser.rank));
            }
          } catch (Exception e) {
            e.printStackTrace();
            createNewUser(uuid);
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.logger.debug(
        "Synced Player '" + UsernameCache.getLastKnownUsername(uuid) + "' with REST API");
  }

  public static void deletePlayerData(UUID uuid) {
    executors.schedule(
        () -> {
          if (!isPlayerOnline(uuid)) {
            UserManager.userRanks.remove(uuid);
            UserManager.playerData.remove(uuid);
          }
        },
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
  }

  private static boolean isPlayerOnline(UUID uuid) {
    for (EntityPlayerMP playerMP :
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
      if (playerMP.getGameProfile().getId().equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  private static void createNewUser(UUID uuid) {
    try {
      GlobalUser globalUser = new GlobalUser(uuid.toString(), "Default");
      Response res = RequestHelper.UserResponses.addPlayerData(globalUser);
      System.out.println(res.getStatusInfo().toString());
      UserManager.playerData.put(
          uuid,
          new Object[]{
              globalUser,
              UserManager.playerData.getOrDefault(uuid, new Object[]{globalUser, null})[1]
          });
      UserManager.userRanks.put(uuid, UserManager.getRank(globalUser.rank));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void createDefaultRanks() {
    Rank defaultRank =
        new Rank(
            "Default", "&8[Default] ", "", new String[]{}, new String[]{"info.*", "teleport.*"});
    Rank adminRank =
        new Rank("Admin", "&c[Admin] ", "", new String[]{"default"}, new String[]{"admin.*"});
    RequestHelper.RankResponses.addRank(defaultRank);
    RequestHelper.RankResponses.addRank(adminRank);
  }
}
