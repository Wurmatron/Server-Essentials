package com.wurmcraft.serveressentials.common.storage.rest;

import static com.wurmcraft.serveressentials.common.storage.StorageUtils.triggerLogoutTimeout;

import com.wurmcraft.serveressentials.api.ServerEssentialsAPI;
import com.wurmcraft.serveressentials.api.user.event.NewPlayerJoin;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.LocalRestUser;
import com.wurmcraft.serveressentials.api.user.rest.ServerTime;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.utils.user.UserManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class RestWorldEvents {

  public static NonBlockingHashMap<String, String> rankChangeCache;
  public static NonBlockingHashMap<String, String[]> permChangeChache;
  public static NonBlockingHashMap<String, String[]> perkChangeCache;
  public static NonBlockingHashMap<String, Long> playerJoinTime;

  public RestWorldEvents() {
    rankChangeCache = new NonBlockingHashMap<>();
    permChangeChache = new NonBlockingHashMap<>();
    perkChangeCache = new NonBlockingHashMap<>();
    playerJoinTime = new NonBlockingHashMap<>();
  }

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    GlobalRestUser user =
        RequestGenerator.User.getUser(e.player.getGameProfile().getId().toString());
    LocalRestUser local =
        DataHelper.load(Storage.LOCAL_USER, new LocalRestUser(e.player.getGameProfile().getId()));
    checkForAndCorrectErrors(e.player, user, local);
    if (user != null) {
      if (local == null) { // New to this server but not the network
        local = new LocalRestUser(e.player.getGameProfile().getId());
        DataHelper.save(Storage.LOCAL_USER, local);
        local = DataHelper.load(Storage.LOCAL_USER, local);
        user.addServerData(
            new ServerTime(
                ConfigHandler.serverName,
                0,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                0));
        MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(e.player, true));
      }
      UserManager.setUserData(e.player.getGameProfile().getId(), new Object[] {user, local});
      MinecraftForge.EVENT_BUS.post(new UserSyncEvent(user, user, Type.LOGIN));
      ServerEssentialsServer.LOGGER.debug(
          "Loaded User '"
              + e.player.getGameProfile().getId().toString()
              + "' ("
              + e.player.getDisplayName().getUnformattedText()
              + ")");
    } else { // New Player
      GlobalRestUser newUser =
          new GlobalRestUser(
              e.player.getGameProfile().getId().toString(), ConfigHandler.defaultRank);
      RequestGenerator.User.addNewPlayer(newUser);
      DataHelper.save(Storage.LOCAL_USER, local);
      local = DataHelper.load(Storage.LOCAL_USER, local);
      MinecraftForge.EVENT_BUS.post(new UserSyncEvent(newUser, user, Type.LOGIN));
      MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(e.player, false));
      newUser.addServerData(
          new ServerTime(
              ConfigHandler.serverName,
              0,
              System.currentTimeMillis(),
              System.currentTimeMillis(),
              0));
      UserManager.setUserData(e.player.getGameProfile().getId(), new Object[] {newUser, local});
      ServerEssentialsServer.LOGGER.debug(
          "Loaded User '"
              + e.player.getGameProfile().getId().toString()
              + "' ("
              + e.player.getDisplayName().getUnformattedText()
              + ")");
    }
    playerJoinTime.put(e.player.getGameProfile().getId().toString(), System.currentTimeMillis());
  }

  @SubscribeEvent
  public void onPlayerLogout(PlayerLoggedOutEvent e) {
    Object[] userData = UserManager.getUserData(e.player);
    if (userData != null && userData.length == 2) {
      if (!DataHelper.save(Storage.LOCAL_USER, (LocalRestUser) userData[1])) {
        ServerEssentialsServer.LOGGER.info(
            "Unable to load '" + e.player.getGameProfile().getId().toString() + "'");
      }
      RequestGenerator.User.overridePlayer((GlobalRestUser) userData[0], Type.LOGOUT);
    }
    triggerLogoutTimeout(e.player);
  }

  @SubscribeEvent
  public void onUserSync(UserSyncEvent e) {
    if (e.type == Type.LOGOUT || e.type == Type.STANDARD) {
      if (!e.localServerUser.rank.equalsIgnoreCase(e.restUser.rank)) { // Correct Rank
        e.restUser.setRank(rankChangeCache.get(e.localServerUser.getUuid()));
        rankChangeCache.remove(e.localServerUser.getUuid());
      }
      if (e.localServerUser.getPerks().length != e.restUser.getPerks().length) { // Correct Perks
        e.restUser.addPerk(perkChangeCache.get(e.localServerUser.getUuid()));
        perkChangeCache.remove(e.localServerUser.getUuid());
      }
      if (e.localServerUser.getPermission().length
          != e.restUser.getPermission().length) { // Correct Perms
        e.restUser.addPermission(permChangeChache.get(e.localServerUser.getUuid()));
        permChangeChache.remove(e.localServerUser.getUuid());
      }
      e.restUser.setMuted(e.localServerUser.isMuted());
      long gainedTime =
          +(System.currentTimeMillis() - playerJoinTime.get(e.restUser.getUuid())) / 1000;
      playerJoinTime.put(e.restUser.getUuid(), System.currentTimeMillis());
      e.restUser.setServerData(updatePlayTime(e.restUser.getServerData(), gainedTime));
    }
  }

  private static ServerTime[] updatePlayTime(ServerTime[] current, long gainedTime) {
    boolean notAdded = true;
    for (ServerTime server : current) {
      if (server.getServerID().equalsIgnoreCase(ConfigHandler.serverName)) {
        server.setOnlineTime(server.getOnlineTime() + gainedTime);
        server.setLastSeen(System.currentTimeMillis());
        notAdded = false;
      }
    }
    if (notAdded) {
      ServerTime time =
          new ServerTime(
              ConfigHandler.serverName,
              gainedTime,
              System.currentTimeMillis(),
              System.currentTimeMillis(),
              0);
      ServerTime[] newTime = new ServerTime[current.length + 1];
      for (int index = 0; index < current.length; index++) {
        newTime[index] = current[index];
      }
      newTime[newTime.length - 1] = time;
      return newTime;
    }
    return current;
  }

  // Correct any issues that may arise from dev-testing / running older / newer versions together
  private static void checkForAndCorrectErrors(
      EntityPlayer player, GlobalRestUser global, LocalRestUser local) {
    if (global != null && global.rank != null && global.rank.isEmpty()
        || !global.rank.isEmpty() && ServerEssentialsAPI.rankManager.getRank(global.rank) == null) {
      global.rank = ConfigHandler.defaultRank;
    } else {
      global.rank = ConfigHandler.defaultRank;
    }
  }
}
