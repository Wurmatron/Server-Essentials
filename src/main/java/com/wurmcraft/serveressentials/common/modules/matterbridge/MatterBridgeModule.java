package com.wurmcraft.serveressentials.common.modules.matterbridge;

import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.LOGGER;
import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.instance;

import com.wurmcraft.serveressentials.api.module.Module;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import com.wurmcraft.serveressentials.common.modules.matterbridge.api.MatterBridgeConfig;
import com.wurmcraft.serveressentials.common.modules.matterbridge.api.json.MBMessage;
import com.wurmcraft.serveressentials.common.modules.matterbridge.event.ServerTickEvent;
import com.wurmcraft.serveressentials.common.reference.Storage;
import com.wurmcraft.serveressentials.common.storage.file.DataHelper;
import com.wurmcraft.serveressentials.common.storage.rest.RequestGenerator;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import javax.net.ssl.HttpsURLConnection;
import net.minecraftforge.common.MinecraftForge;

@Module(name = "MatterBridge")
public class MatterBridgeModule {

  public static MatterBridgeConfig config;
  public static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSSSSS'Z'");

  private static boolean validConnection = true;

  public void setup() {
    setupAndLoadConfig();
    if (!checkConnection()) {
      LOGGER.error("Unable to connect to MatterBridge");
    }
    MinecraftForge.EVENT_BUS.register(new ServerTickEvent());
  }

  public static void setupAndLoadConfig() {
    File mbConfig = new File(ConfigHandler.saveLocation + File.separator + "MatterBridge.json");
    if (mbConfig.exists()) {
      config = DataHelper.load(mbConfig, Storage.MATTERBRIDGE, new MatterBridgeConfig());
    } else {
      try {
        mbConfig.createNewFile();
        Files.write(
            mbConfig.toPath(),
            ServerEssentialsServer.instance.GSON.toJson(new MatterBridgeConfig()).getBytes(),
            StandardOpenOption.CREATE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static String getMBURL() {
    if (config.url.endsWith("/")) {
      return config.url + "api/";
    } else {
      return config.url + "/api/";
    }
  }

  public static boolean checkConnection() {
    try {
      URL obj = new URL(getMBURL() + "messages");
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", RequestGenerator.USER_AGENT);
      if (con.getResponseCode() == 200) {
        return true;
      }
    } catch (Exception e) {
      LOGGER.warn(e.getLocalizedMessage());
    }
    return false;
  }

  public static MBMessage[] getMessages() {
    if (checkConnection()) {
      validConnection = true;
      try {
        URL obj = new URL(getMBURL() + "messages");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", RequestGenerator.USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK
            || responseCode == HttpsURLConnection.HTTP_ACCEPTED) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return instance.GSON.fromJson(response.toString(), MBMessage[].class);
        }
      } catch (Exception e) {
        LOGGER.warn(e.getLocalizedMessage());
      }
    } else {
      if (validConnection) {
        LOGGER.error(
            "Connection to MatterBridge via "
                + config.gateway
                + "@"
                + config.url
                + " has been lost!");
      }
      validConnection = false;
    }
    return new MBMessage[0];
  }

  public static void sendMessage(MBMessage msg) {
    try {
      URL sendURL = new URL(getMBURL() + "message");
      URLConnection connection = sendURL.openConnection();
      HttpURLConnection https = (HttpURLConnection) connection;
      https.setRequestMethod("POST");
      https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      https.setDoOutput(true);
      String json = instance.GSON.toJson(msg).replaceAll("\n", "");
      connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
      connection.getOutputStream().write(json.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      if (status != 200) {
        LOGGER.error("Error sending message '" + instance.GSON.toJson(msg) + "'");
      }
    } catch (Exception e) {
      LOGGER.warn(e.getLocalizedMessage());
    }
  }
}
