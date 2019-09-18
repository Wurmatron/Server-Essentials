package com.wurmcraft.serveressentials.common.storage.rest;

import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.LOGGER;
import static com.wurmcraft.serveressentials.common.ServerEssentialsServer.instance;

import com.wurmcraft.serveressentials.api.Validation;
import com.wurmcraft.serveressentials.api.storage.json.Currency;
import com.wurmcraft.serveressentials.api.storage.json.ServerStatus;
import com.wurmcraft.serveressentials.api.storage.json.Token;
import com.wurmcraft.serveressentials.api.storage.json.UUIDCache;
import com.wurmcraft.serveressentials.api.user.autorank.AutoRank;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent;
import com.wurmcraft.serveressentials.api.user.event.UserSyncEvent.Type;
import com.wurmcraft.serveressentials.api.user.rest.GlobalRestUser;
import com.wurmcraft.serveressentials.api.user.transfer.TransferBin;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.modules.matterbridge.MatterBridgeModule;
import com.wurmcraft.serveressentials.common.modules.matterbridge.api.json.MBMessage;
import com.wurmcraft.serveressentials.common.modules.track.TrackModule;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RequestGenerator {

  public static final String USER_AGENT = "Mozilla/5.0";

  private String auth = createRestAuth(ConfigHandler.restAuth);
  private String baseURL = parseConfigURL(ConfigHandler.restURL);

  private static RequestGenerator INSTANCE = new RequestGenerator();

  private boolean isBase64(String data) {
    try {
      Base64.getDecoder().decode(data.getBytes());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private String createRestAuth(String auth) {
    if (isBase64(auth)) {
      return "Basic " + auth.getBytes();
    } else {
      return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
  }

  public static String parseConfigURL(String url) {
    if (url.endsWith("/")) {
      return url;
    } else {
      return url + "/";
    }
  }

  /**
   * Send some data to a given URL as a json format
   *
   * @param type type of request (PUT, POST)
   * @param url additional info in the url
   * @param data object to send to the url
   * @return https status code for https connection, if its a teapot then its not getting a response
   *     back
   */
  private int send(String type, String url, Object data) {
    try {
      URL sendURL = new URL(baseURL + url);
      URLConnection connection = sendURL.openConnection();
      HttpsURLConnection https = (HttpsURLConnection) connection;
      https.setRequestMethod(type.toUpperCase());
      https.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      https.setDoOutput(true);
      https.setRequestProperty("Authorization", auth);
      String json = instance.GSON.toJson(data).replaceAll("\n", "");
      connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
      connection.getOutputStream().write(json.getBytes());
      int status = ((HttpsURLConnection) connection).getResponseCode();
      if (status == HttpsURLConnection.HTTP_UNAUTHORIZED) {
        LOGGER.error("Invalid Rest API Key, Unable to Put");
      }
      return status;
    } catch (Exception e) {
      LOGGER.warn(e.getLocalizedMessage());
    }
    return 418; //  I'm a teapot
  }

  public int post(String url, Object data) {
    return send("post", url, data);
  }

  public int put(String url, Object data) {
    return send("put", url, data);
  }

  /**
   * Get the given data from the url and
   *
   * @param type type of object to cast the url data to
   * @param data additional info about the url
   * @return The url as the given object
   */
  public <T extends Object> T get(String data, Class<T> type) {
    if (!data.isEmpty()) {
      try {
        URL obj = new URL(baseURL + data);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Authorization", auth);
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
          return instance.GSON.fromJson(response.toString(), type);
        }
      } catch (Exception e) {
        LOGGER.warn(e.getMessage());
      }
    }
    return null;
  }

  public static class User {

    public static GlobalRestUser getUser(String uuid) {
      return INSTANCE.get("user/" + uuid, GlobalRestUser.class);
    }

    public static int addNewPlayer(GlobalRestUser globalUser) {
      return INSTANCE.post("user/add", globalUser);
    }

    public static int overridePlayer(GlobalRestUser globalUser, Type type, boolean bypass) {
      synchronized (FMLCommonHandler.instance().getMinecraftServerInstance().getServerThread()) {
        GlobalRestUser currentRestUser = getUser(globalUser.getUuid());
        UserSyncEvent sync = new UserSyncEvent(globalUser, currentRestUser, type);
        sync.bypass = bypass;
        MinecraftForge.EVENT_BUS.post(sync);
        if (!sync.isCanceled()) {
          return INSTANCE.put("user/" + globalUser.getUuid() + "/override", currentRestUser);
        }
      }
      return 418; //  I'm a teapot
    }

    public static int overridePlayer(GlobalRestUser globalUser, Type type) {
      return overridePlayer(globalUser, type, false);
    }
  }

  public static class Rank {

    public static com.wurmcraft.serveressentials.api.user.rank.Rank getRank(String name) {
      return INSTANCE.get("rank/" + name, com.wurmcraft.serveressentials.api.user.rank.Rank.class);
    }

    public static int addRank(com.wurmcraft.serveressentials.api.user.rank.Rank rank) {
      return INSTANCE.post("rank/add", rank);
    }

    public static int overrideRank(com.wurmcraft.serveressentials.api.user.rank.Rank rank) {
      return INSTANCE.post("rank/" + rank.getID() + "/override", rank);
    }

    public static int deleteRank(com.wurmcraft.serveressentials.api.user.rank.Rank rank) {
      return INSTANCE.post("rank/del", rank);
    }

    public static com.wurmcraft.serveressentials.api.user.rank.Rank[] getAllRanks() {
      return INSTANCE.get("rank/", com.wurmcraft.serveressentials.api.user.rank.Rank[].class);
    }
  }

  public static class AutoRankResponses {

    public static int addAutoRank(AutoRank rank) {
      return INSTANCE.post("autorank/add", rank);
    }

    public static AutoRank getAutoRank(String name) {
      return INSTANCE.get("autorank/" + name, AutoRank.class);
    }

    public static int overrideAutoRank(AutoRank rank) {
      return INSTANCE.post("autorank/" + rank.getID() + "/override", rank);
    }

    public static int deleteAutoRank(AutoRank rank) {
      return INSTANCE.put("autorank/" + rank.getID() + "/del", rank);
    }

    public static AutoRank[] getAllAutoRanks() {
      return INSTANCE.get("autorank/", AutoRank[].class);
    }
  }

  public static class Status {

    public static Validation getValidation() {
      Validation val = INSTANCE.get("/validate", Validation.class);
      return val;
    }

    public static void syncServer(ServerStatus status) {
      INSTANCE.post("/status", status);
      TrackModule.networkStatus = INSTANCE.get("/status", ServerStatus[].class);
    }

    public static void addUUID(UUIDCache cache) {
      INSTANCE.post("/uuid/add", cache);
    }

    public static UUIDCache[] getUUIDCache() {
      return INSTANCE.get("/uuid", UUIDCache[].class);
    }
  }

  public static class Economy {

    public static void addEco(Currency coin) {
      INSTANCE.post("eco/add", coin);
    }

    public static Currency getEco(String name) {
      return INSTANCE.get("eco/" + name, Currency.class);
    }

    public static void overrideEco(Currency currency) {
      INSTANCE.put("eco/" + currency.name + "/override", currency);
    }

    public static Currency[] getAllCurrency() {
      return INSTANCE.get("eco", Currency[].class);
    }
  }

  public static class MatterBridge {

    public static void sendMessage(MBMessage msg) {
      INSTANCE.post(MatterBridgeModule.getMBURL() + "message", msg);
    }

    public static MBMessage[] getMessages() {
      return INSTANCE.get(MatterBridgeModule.getMBURL() + "messages", MBMessage[].class);
    }
  }

  public static class Transfer {

    public static void addTransfer(TransferBin bin) {
      INSTANCE.post("transfer/add", bin);
    }

    public static void overrideTransfer(TransferBin bin) {
      INSTANCE.put("transfer/" + bin.uuid + "/override", bin);
    }

    public static TransferBin getTransfer(UUID uuid) {
      return INSTANCE.get("transfer/" + uuid.toString(), TransferBin.class);
    }
  }

  public static class Discord {

    public static Token[] getDiscordCodes() {
      return INSTANCE.get("discord/list", Token[].class);
    }
  }
}
