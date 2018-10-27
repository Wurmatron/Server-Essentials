package com.wurmcraft.serveressentials.common.rest;

import com.wurmcraft.serveressentials.api.json.global.Keys;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.optional.Bank;
import com.wurmcraft.serveressentials.api.json.user.optional.Coin;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.rest.LocalUser;
import com.wurmcraft.serveressentials.api.module.IModule;
import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.general.utils.DataHelper;
import com.wurmcraft.serveressentials.common.rest.events.WorldEvent;
import com.wurmcraft.serveressentials.common.rest.utils.RequestHelper;
import com.wurmcraft.serveressentials.common.team.TeamModule;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

@Module(name = "Rest")
public class RestModule implements IModule {

  public static final ScheduledExecutorService EXECUTORs = Executors.newScheduledThreadPool(1);
  public static volatile boolean isValid;

  public static void syncRanks() {
    EXECUTORs.scheduleAtFixedRate(
        () -> {
          try {
            Rank[] allRanks = RequestHelper.RankResponses.getAllRanks();
            UserManager.RANK_CACHE.clear();
            for (Rank rank : allRanks) {
              UserManager.RANK_CACHE.put(rank.getName(), rank);
            }
            if (UserManager.RANK_CACHE.size() == 0) {
              createDefaultRanks();
            }
            if (allRanks.length == 0) {
              isValid = false;
            } else {
              isValid = true;
            }
          } catch (Exception e) {
            ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
  }

  public static void syncPlayer(UUID uuid) {
    EXECUTORs.scheduleAtFixedRate(
        () -> {
          try {
            GlobalUser globalUser = RequestHelper.UserResponses.getPlayerData(uuid);
            if (globalUser == null) {
              createNewUser(uuid);
            } else {
              LocalUser user = loadLocalUser(uuid);
              if (user == null) {
                user = new LocalUser(uuid);
                DataHelper.forceSave(Keys.LOCAL_USER, user);
              }
              UserManager.PLAYER_DATA.put(
                  uuid,
                  new Object[] {
                    globalUser,
                    UserManager.PLAYER_DATA.getOrDefault(uuid, new Object[] {globalUser, user})[1]
                  });
              UserManager.USER_RANKS.put(uuid, UserManager.getRank(globalUser.rank));
              TeamModule.loadRestTeam(uuid);
            }
          } catch (Exception e) {
            createNewUser(uuid);
          }
        },
        0L,
        ConfigHandler.syncPeriod,
        TimeUnit.MINUTES);
    ServerEssentialsServer.LOGGER.debug(
        "Synced Player '" + UsernameCache.getLastKnownUsername(uuid) + "' with REST API");
  }

  public static void deletePlayerData(UUID uuid) {
    EXECUTORs.schedule(
        () -> {
          if (!isPlayerOnline(uuid)) {
            UserManager.USER_RANKS.remove(uuid);
            UserManager.PLAYER_DATA.remove(uuid);
          }
        },
        ConfigHandler.syncPeriod + (long) 5,
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
      GlobalUser globalUser = new GlobalUser(uuid.toString(), ConfigHandler.defaultRank);
      List<Coin> coins = new ArrayList<>();
      for (String name : ConfigHandler.activeCurrency) {
        coins.add(new Coin(name, 0));
      }
      Bank bank = new Bank(coins.toArray(new Coin[0]));
      globalUser.setBank(bank);
      LocalUser localUser = new LocalUser(uuid);
      DataHelper.createIfNonExist(Keys.LOCAL_USER, localUser);
      RequestHelper.UserResponses.addPlayerData(globalUser);
      UserManager.PLAYER_DATA.put(
          uuid,
          new Object[] {
            globalUser,
            UserManager.PLAYER_DATA.getOrDefault(uuid, new Object[] {globalUser, localUser})[1]
          });
      UserManager.USER_RANKS.put(uuid, UserManager.getRank(globalUser.rank));
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  public static void createDefaultRanks() {
    Rank defaultRank =
        new Rank(
            "Default", "&8[Default]", "", new String[] {}, new String[] {"info.*", "teleport.*"});
    Rank adminRank =
        new Rank("Admin", "&c[Admin]", "", new String[] {"default"}, new String[] {"*"});
    RequestHelper.RankResponses.addRank(defaultRank);
    RequestHelper.RankResponses.addRank(adminRank);
  }

  public static LocalUser loadLocalUser(UUID uuid) {
    return DataHelper.load(
        new File(
            ConfigHandler.saveLocation
                + File.separator
                + Keys.LOCAL_USER.name()
                + File.separator
                + uuid.toString()
                + ".json"),
        Keys.LOCAL_USER,
        new LocalUser(uuid));
  }

  @Override
  public void setup() {
    if (ConfigHandler.storageType.equalsIgnoreCase("rest")) {
      if (ConfigHandler.restURL.startsWith("http://")
          || ConfigHandler.restURL.startsWith("https://")) {
        MinecraftForge.EVENT_BUS.register(new WorldEvent());
        ServerEssentialsServer.LOGGER.info("Creating Default Ranks");
        syncRanks();
      } else {
        ServerEssentialsServer.LOGGER.warn(
            "Rest API Unable to load due to invalid Endpoint '" + ConfigHandler.restURL + "'");
      }
    } else {
      ServerEssentialsServer.LOGGER.debug(
          "Rest API is enabled but not used for UserData or Ranks'" + ConfigHandler.restURL + "'");
    }
  }
}
