package com.wurmcraft.serveressentials.forge.modules.matterlink.event;

import static com.wurmcraft.serveressentials.core.SECore.GSON;
import static com.wurmcraft.serveressentials.forge.modules.matterlink.utils.MatterBridgeUtils.USER_AGENT;
import static com.wurmcraft.serveressentials.forge.modules.matterlink.utils.MatterBridgeUtils.getLinkConnectURL;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.api.json.rank.Rank;
import com.wurmcraft.serveressentials.core.api.player.StoredPlayer;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.forge.modules.core.event.PlayerDataEvents;
import com.wurmcraft.serveressentials.forge.modules.language.event.ChatEvents;
import com.wurmcraft.serveressentials.core.api.module.config.MatterLinkConfig;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.MatterBridgeUtils;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.json.RestMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class MatterLinkTickEvent {

  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
      "yyyy-MM-dd'T'hh:mm:ss.SSSSSSSSS'Z'");
  public static MatterLinkConfig config = (MatterLinkConfig) SERegistry
      .getStoredData(DataKey.MODULE_CONFIG, "MatterLink");

  private boolean running = false;

  private void getAndProcessBridge() {
    if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
        .getCurrentPlayerCount() > 0) {
      RestMessage[] messages = MatterBridgeUtils.getMessages();
      for (RestMessage msg : messages) {
        displayMessage(msg);
      }
    }
  }

  private void displayMessage(RestMessage msg) {
    if (!msg.event.equals("api_connected")) {
      if (isAnotherServer(msg)) {
        String rank = msg.username.substring(0, msg.username.indexOf("]"));
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
            .sendMessage(new TextComponentString(
                TextFormatting.GOLD + "[" + msg.protocol.substring(0, 1).toUpperCase()
                    + msg.protocol.substring(1).toLowerCase() + "] "
                    + TextFormatting.LIGHT_PURPLE + msg.username + " \u00BB "
                    + TextFormatting.GRAY + msg.text));
      } else {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
            .sendMessage(
                new TextComponentString(
                    TextFormatting.GREEN + "[" + msg.protocol.substring(0, 1)
                        .toUpperCase()
                        + msg.protocol.substring(1).toLowerCase() + "] "
                        + TextFormatting.GRAY + msg.username + " \u00BB " + msg.text));
      }
    }
  }

  private boolean isAnotherServer(RestMessage msg) {
    if (msg.username.contains("] ")) {
      return true;
    }
    return false;
  }

  @SubscribeEvent
  public void onChat(ServerChatEvent e) {
    RestMessage message = new RestMessage(
        "https://crafatar.com/avatars/" + e.getPlayer().getGameProfile().getId()
            .toString().replaceAll("-", ""), "", config.gateway, e.getMessage(),
        formatName(e.getPlayer()), "", SERegistry.globalConfig.serverID, "", "",
        config.protocol, DATE_FORMAT.format(new Date()),
        e.getPlayer().getGameProfile().getId().toString(), null);
    if (MatterBridgeUtils.sendMessage(message) != 200) {
      ServerEssentialsServer.logger.warn("Failed to send message to bridge");
    }
  }

  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent e) {
    if (e.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) e.getEntityLiving();
      RestMessage msg = new RestMessage(
          "https://crafatar.com/avatars/" + player.getGameProfile().getId()
              .toString().replaceAll("-", ""), "death", config.gateway,
          e.getSource().getDeathMessage(player).getUnformattedText(),
          "", SERegistry.globalConfig.serverID,
          SERegistry.globalConfig.serverID, "", "", config.protocol,
          DATE_FORMAT.format(new Date()), player.getGameProfile().getId().toString(),
          null);
      if (MatterBridgeUtils.sendMessage(msg) != 200) {
        ServerEssentialsServer.logger.warn("Failed to send message to bridge");
      }
    }
  }

  public String formatName(EntityPlayer player) {
    try {
      StoredPlayer playerData = (StoredPlayer) SERegistry
          .getStoredData(DataKey.PLAYER, player.getGameProfile().getId().toString());
      Rank rank = (Rank) SERegistry.getStoredData(DataKey.RANK, playerData.global.rank);
      return TextFormatting
          .getTextWithoutFormattingCodes(rank.getPrefix().replaceAll("&", "\u00a7")) + " "
          + ChatEvents.handleUsername(player, playerData);
    } catch (NoSuchElementException e) {
      PlayerDataEvents.handAndCheckForErrors(player);
    }
    return player.getDisplayNameString();
  }


  @SubscribeEvent(priority = EventPriority.LOW)
  public void onJoin(PlayerLoggedInEvent e) {
    if (!running) {
      running = true;
      if (config.dataCollectionType.equalsIgnoreCase("query")) {
        SECore.executors
            .scheduleAtFixedRate(this::getAndProcessBridge, 0, 1, TimeUnit.SECONDS);
      } else if (config.dataCollectionType.equalsIgnoreCase("stream")) {
        startHandlingStream();
      }
    }
    if (config.displayLoginLogoutMessages) {
      RestMessage msg = new RestMessage(
          "https://crafatar.com/avatars/" + e.player.getGameProfile().getId()
              .toString().replaceAll("-", ""), "login", config.gateway,
          " has joined the game", formatName(e.player),
          SERegistry.globalConfig.serverID, SERegistry.globalConfig.serverID, "", "",
          config.protocol, DATE_FORMAT.format(new Date()),
          e.player.getGameProfile().getId().toString(), null);
      if (MatterBridgeUtils.sendMessage(msg) != 200) {
        ServerEssentialsServer.logger.warn("Failed to send message to bridge");
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGH)
  public void onLogout(PlayerLoggedOutEvent e) {
    if (config.displayLoginLogoutMessages) {
      RestMessage msg = new RestMessage(
          "https://crafatar.com/avatars/" + e.player.getGameProfile().getId()
              .toString().replaceAll("-", ""), "logout", config.gateway,
          " has left the game", formatName(e.player),
          SERegistry.globalConfig.serverID, SERegistry.globalConfig.serverID, "", "",
          config.protocol, DATE_FORMAT.format(new Date()),
          e.player.getGameProfile().getId().toString(), null);
      if (MatterBridgeUtils.sendMessage(msg) != 200) {
        ServerEssentialsServer.logger.warn("Failed to send message to bridge");
      }
    }
  }

  private void startHandlingStream() {
    Thread thread = new Thread(() -> {
      try {
        URL url = new URL(getLinkConnectURL() + "stream");
        URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (!MatterLinkTickEvent.config.token.isEmpty()) {
          con.setRequestProperty("Authorization",
              "Bearer " + MatterLinkTickEvent.config.token);
        }
        ServerEssentialsServer.logger.info("Bridge Streaming is enabled");
        BufferedReader buff = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = buff.readLine()) != null) {
          RestMessage msg = GSON.fromJson(line, RestMessage.class);
          displayMessage(msg);
        }
        ServerEssentialsServer.logger.info("Bridge Streaming is disabled");
      } catch (Exception e) {
        ServerEssentialsServer.logger.info("Attempting to reconnect to bridge....");
        try {
          Thread.sleep(1000);
          startHandlingStream();
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        }
      }
    }, "Chat-Stream");
    thread.start();
  }
}
