package com.wurmcraft.serveressentials.forge.modules.core.event;

import static com.wurmcraft.serveressentials.core.SECore.GSON;
import static com.wurmcraft.serveressentials.core.SECore.SAVE_DIR;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.eco.Coin;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.player.Home;
import com.wurmcraft.serveressentials.core.api.player.ServerPlayer;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.api.player.Vault;
import com.wurmcraft.serveressentials.core.api.player.Wallet;
import com.wurmcraft.serveressentials.core.api.track.NetworkTime;
import com.wurmcraft.serveressentials.core.api.track.ServerTime;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.core.utils.RestRequestGenerator;
import com.wurmcraft.serveressentials.forge.api.event.NewPlayerJoin;
import com.wurmcraft.serveressentials.forge.api.event.PlayerDataSyncEvent;
import com.wurmcraft.serveressentials.forge.api.event.RankChangeEvent;
import com.wurmcraft.serveressentials.forge.modules.economy.EconomyConfig;
import com.wurmcraft.serveressentials.forge.modules.language.LanguageConfig;
import com.wurmcraft.serveressentials.forge.modules.rank.RankConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

public class PlayerDataEvents {

  public static NonBlockingHashSet<String> newPlayers = new NonBlockingHashSet<>();

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    handlePlayer(e.player);
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onPlayerLeave(PlayerLoggedOutEvent e) {
    savePlayer(e.player);
  }

  private void handlePlayer(EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if (playerData.global == null && playerData.server == null) {
        SECore.logger.info(player.getDisplayNameString() + " is a new player!");
        StoredPlayer data = createNew(player);
        SERegistry.register(DataKey.PLAYER, data);
        MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(player, playerData));
      }
    } catch (NoSuchElementException e) {
      SECore.logger.info(player.getDisplayNameString() + " is a new player!");
      newPlayers.add(player.getGameProfile().getId().toString());
      StoredPlayer playerData = createNew(player);
      SERegistry.register(DataKey.PLAYER, playerData);
      MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(player, playerData));
    }
    if (SERegistry.globalConfig.dataStorgeType.equals("Rest")) {
      SECore.executors.scheduleAtFixedRate(() -> {
        savePlayer(player);
        SECore.logger
            .fine("User '" + player.getDisplayNameString() + "' has been synced!");
      }, 0, 90, TimeUnit.SECONDS);
    }
  }

  public static StoredPlayer createNew(EntityPlayer player) {
    GlobalPlayer global = createNewGlobal(player);
    ServerPlayer server = createNewServer(player);
    return new StoredPlayer(player.getGameProfile().getId().toString(), server, global);
  }

  public static GlobalPlayer createNewGlobal(EntityPlayer player) {
    GlobalPlayer global = new GlobalPlayer();
    global.uuid = player.getGameProfile().getId().toString();
    global.firstJoin = Instant.now().getEpochSecond();
    global.lastSeen = Instant.now().getEpochSecond();
    if (SERegistry.isModuleLoaded("Language")) {
      global.language = ((LanguageConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Language")).defaultLang;
    } else {
      global.language = "en_us";
    }
    global.muted = false;
    if (SERegistry.isModuleLoaded("Economy")) {
      Coin defaultCoins = ((EconomyConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Economy")).defaultServerCurrency;
      global.wallet = new Wallet(new Coin[]{defaultCoins});
    } else {
      global.wallet = new Wallet(new Coin[0]);
    }
    if (SERegistry.isModuleLoaded("Rank")) {
      global.rank = ((RankConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Rank")).defaultRank;
    }
    global.playtime = new NetworkTime(new ServerTime[]{});
    global.discordID = "";
    global.rewardPoints = 0;
    global.extraPerms = new String[0];
    global.perks = new String[0];
    return global;
  }

  public static ServerPlayer createNewServer(EntityPlayer player) {
    ServerPlayer server = new ServerPlayer();
    server.firstJoin = Instant.now().getEpochSecond();
    server.lastSeen = Instant.now().getEpochSecond();
    server.homes = new Home[0];
    server.vaults = new Vault[0];
    server.lastLocation = new LocationWrapper(player.posX, player.posY, player.posZ,
        player.dimension);
    server.teleportTimer = System.currentTimeMillis();
    server.kitUsage = new HashMap<>();
    if (SERegistry.isModuleLoaded("Language")) {
      server.channel = ((LanguageConfig) SERegistry
          .getStoredData(DataKey.MODULE_CONFIG, "Language")).defaultChannel;
    } else {
      server.channel = "global";
    }
    return server;
  }

  public static void savePlayer(EntityPlayer player) {
    SECore.executors.schedule(() -> {
      try {
        StoredPlayer playerData = (StoredPlayer) SERegistry
            .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
        MinecraftForge.EVENT_BUS.post(new PlayerDataSyncEvent(player, playerData));
        if (SERegistry.globalConfig.dataStorgeType.equalsIgnoreCase("Rest")) {
          GlobalPlayer restData = RestRequestGenerator.User
              .getPlayer(player.getGameProfile().getId().toString());
          if (newPlayers.contains(player.getGameProfile().getId().toString())) {
            newPlayers.remove(player.getGameProfile().getId().toString());
            playerData.global = restData;
            SERegistry.register(DataKey.PLAYER, playerData);
          } else {
            restData.lastSeen = Instant.now().getEpochSecond();
            RestRequestGenerator.User
                .overridePlayer(player.getGameProfile().getId().toString(), restData);
            playerData.global = restData;
          }
          if (!restData.rank.equals(playerData.global.rank)) {
            MinecraftForge.EVENT_BUS.post(new RankChangeEvent(player, playerData,
                (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank),
                (Rank) SERegistry.getStoredData(DataKey.RANK, restData.rank)));
          }
        }
        Files.write(new File(
            SAVE_DIR + File.separator + DataKey.PLAYER.getName() + File.separator + player
                .getGameProfile().getId().toString()
                + ".json").toPath(), GSON.toJson(playerData).getBytes());
        MinecraftForge.EVENT_BUS.post(new PlayerDataSyncEvent(player, playerData));
      } catch (NoSuchElementException e) {
        SECore.logger
            .warning("Unable to locate / save data for '" + player.getName() + "'!");
      } catch (IOException e) {
        e.printStackTrace();
        SECore.logger.warning("Unable to save playerfile '" + player.getName() + "'!");
      }
    }, 1, TimeUnit.SECONDS);
  }

  public static void handAndCheckForErrors(EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if(playerData == null) {
        playerData = createNew(player);
        if(playerData.global == null) {
          GlobalPlayer global = RestRequestGenerator.User.getPlayer(player.getGameProfile().getId().toString());
          if(global == null) {
            global = createNewGlobal(player);
            RestRequestGenerator.User.overridePlayer(player.getGameProfile().getId().toString(),global);
          }
        }
      }
    } catch (Exception e) {
      createNew(player);
    }
  }
}
