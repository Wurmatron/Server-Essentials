package com.wurmcraft.serveressentials.core.utils;

import static com.wurmcraft.serveressentials.core.SECore.GSON;

import com.wurmcraft.serveressentials.core.SECore;
import com.wurmcraft.serveressentials.core.api.data.Token;
import com.wurmcraft.serveressentials.core.api.json.Validation;
import com.wurmcraft.serveressentials.core.api.player.GlobalPlayer;
import com.wurmcraft.serveressentials.core.api.track.TrackingStatus;
import com.wurmcraft.serveressentials.core.registry.SERegistry;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import javax.net.ssl.HttpsURLConnection;

public class RestRequestGenerator {

  public static final String USER_AGENT = "Mozilla/5.0";

  private String auth = createRestAuth(SERegistry.globalConfig.restAuth);
  private String baseURL = parseConfigURL(SERegistry.globalConfig.restURL);

  private static RestRequestGenerator INSTANCE = new RestRequestGenerator();

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
      String json = GSON.toJson(data).replaceAll("\n", "");
      connection.setRequestProperty("Content-Length", String.valueOf(json.length()));
      connection.getOutputStream().write(json.getBytes());
      int status = ((HttpsURLConnection) connection).getResponseCode();
      if (status == HttpsURLConnection.HTTP_UNAUTHORIZED) {
        SECore.logger.severe("Invalid Rest API Key, Unable to Put");
      }
      return status;
    } catch (Exception e) {
      SECore.logger.warning(e.getLocalizedMessage());
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
          return GSON.fromJson(response.toString(), type);
        }
      } catch (Exception e) {
        SECore.logger.warning(e.getMessage());
      }
    }
    return null;
  }

  public static class Rank {

    public static com.wurmcraft.serveressentials.core.api.json.rank.Rank getRank(String name) {
      return INSTANCE.get(
          "rank/" + name, com.wurmcraft.serveressentials.core.api.json.rank.Rank.class);
    }

    public static int addRank(com.wurmcraft.serveressentials.core.api.json.rank.Rank rank) {
      return INSTANCE.post("rank/add", rank);
    }

    public static int overrideRank(com.wurmcraft.serveressentials.core.api.json.rank.Rank rank) {
      return INSTANCE.post("rank/" + rank.getID() + "/override", rank);
    }

    public static int deleteRank(com.wurmcraft.serveressentials.core.api.json.rank.Rank rank) {
      return INSTANCE.post("rank/del", rank);
    }

    public static com.wurmcraft.serveressentials.core.api.json.rank.Rank[] getAllRanks() {
      return INSTANCE.get("rank/", com.wurmcraft.serveressentials.core.api.json.rank.Rank[].class);
    }
  }

  public static class User {

    public static GlobalPlayer getPlayer(String uuid) {
      return INSTANCE.get("user/" + uuid, GlobalPlayer.class);
    }

    public static int addPlayer(GlobalPlayer player) {
      return INSTANCE.post("user/add", player);
    }

    public static int overridePlayer(String uuid, GlobalPlayer player) {
      return INSTANCE.put("user/" + uuid + "/override", player);
    }
  }

  public static class Verify {
    public static Validation get() {
      return INSTANCE.get("validate", Validation.class);
    }
  }

  public static class Track {
    public static int updateTrack(TrackingStatus status) {
      return INSTANCE.post("status", status);
    }
  }

  public static class Discord {
    public static Token[] getTokens() {
      return INSTANCE.get("discord/list", Token[].class);
    }
  }
}
