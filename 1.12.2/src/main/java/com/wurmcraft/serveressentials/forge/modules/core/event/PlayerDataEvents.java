package com.wurmcraft.serveressentials.forge.modules.core.event;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.data.LocationWrapper;
import com.wurmcraft.serveressentials.core.api.eco.Currency;
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
import com.wurmcraft.serveressentials.forge.modules.language.LanguageConfig;
import java.time.Instant;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerDataEvents {

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent e) {
    handlePlayer(e.player);
  }

  private void handlePlayer(EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      if (playerData.global == null && playerData.server == null) {
        SERegistry.register(DataKey.PLAYER, createNew(player));
        MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(player, playerData));
      }
    } catch (NoSuchElementException e) {
      StoredPlayer playerData = createNew(player);
      SERegistry.register(DataKey.PLAYER, playerData);
      MinecraftForge.EVENT_BUS.post(new NewPlayerJoin(player, playerData));
    }
    if (SERegistry.globalConfig.dataStorgeType.equals("Rest")) {
      SECore.executors.scheduleAtFixedRate(() -> {
        GlobalPlayer global = RestRequestGenerator.User
            .getPlayer(player.getGameProfile().getId().toString());
        if (global != null) {
          StoredPlayer playerData = ((StoredPlayer) SERegistry
              .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString()));
          RestPlayerSyncEvent e = new RestPlayerSyncEvent(player, playerData, global);
          playerData.global = e.restData;
          SECore.logger
              .fine("User '" + player.getDisplayNameString() + "' has been synced!");
        } else {
          SECore.logger
              .warning("Unable to find '" + player.getDisplayNameString() + "' on rest!");
        }
      }, 300, 90, TimeUnit.SECONDS);
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
      // TODO Set default currency
    } else {
      global.wallet = new Wallet(new Currency[0]);
    }
    global.playtime = new NetworkTime(new ServerTime[]{});
    global.discordID = "";
    global.rewardPoints = 0;
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
}
