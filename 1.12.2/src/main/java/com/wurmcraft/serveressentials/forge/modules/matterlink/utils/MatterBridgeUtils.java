package com.wurmcraft.serveressentials.forge.modules.matterlink.utils;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.DataKey;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import com.wurmcraft.serveressentials.forge.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.core.api.module.config.MatterLinkConfig;
import com.wurmcraft.serveressentials.forge.modules.matterlink.event.MatterLinkTickEvent;
import com.wurmcraft.serveressentials.forge.modules.matterlink.utils.json.RestMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MatterBridgeUtils {

  public static MatterLinkConfig config = (MatterLinkConfig) SERegistry.getStoredData(DataKey.MODULE_CONFIG,"MatterLink");
  public static final String USER_AGENT = "";

  public static String getLinkConnectURL() {
    if (config.url.endsWith("api")) {
      return config.url.trim() + "/";
    } else if (config.url.endsWith("api/")) {
      return config.url;
    } else if (config.url.endsWith("/") && !config.url.endsWith("/api/")) {
      return config + "api/";
    } else if (!config.url.endsWith("/") && !config.url.contains("api/")) {
      return config.url + "/api/";
    }
    return config.url;
  }

  public static RestMessage[] getMessages() {
    if (getHealth() == 200) {
      try {
        URL obj = new URL(getLinkConnectURL() + "messages");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        if (!MatterLinkTickEvent.config.token.isEmpty()) {
          con.setRequestProperty("Authorization",
              "Bearer " + MatterLinkTickEvent.config.token);
        }
        con.setReadTimeout(300000);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK
            || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), RestMessage[].class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.logger.warn(e.getMessage());
      }
    }
    return new RestMessage[0];
  }



  public static int sendMessage(RestMessage msg) {
    if (getHealth() == 200) {
      try {
        URL sendURL = new URL(getLinkConnectURL() + "message");
        URLConnection connection = sendURL.openConnection();
        HttpURLConnection https = (HttpURLConnection) connection;
        https.setRequestMethod("POST");
        https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        https.setDoOutput(true);
        String json = SECore.GSON.toJson(msg);
        connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
        if (!MatterLinkTickEvent.config.token.isEmpty()) {
          connection.setRequestProperty("Authorization",
              "Bearer " + MatterLinkTickEvent.config.token);
        }
        connection.getOutputStream().write(json.getBytes());
        return https.getResponseCode();
      } catch (Exception e) {
        return 503;
      }
    } else {
      ServerEssentialsServer.logger.error("Failed to send msg! (" + msg.username + " " + msg.text + ")");
    }
    return 418;
  }

  public static int getHealth() {
    try {
      URL obj = new URL(getLinkConnectURL() + "health");
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", USER_AGENT);
      if (!MatterLinkTickEvent.config.token.isEmpty()) {
        con.setRequestProperty("Authorization",
            "Bearer " + MatterLinkTickEvent.config.token);
      }
      return con.getResponseCode();
    } catch (Exception e) {
    }
    return 404;
  }

}
