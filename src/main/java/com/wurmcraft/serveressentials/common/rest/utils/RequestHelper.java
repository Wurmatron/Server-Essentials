package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.rest.ServerStatus;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.optional.Currency;
import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.rest.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;

public class RequestHelper {

  private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final String USER_AGENT = "Mozilla/5.0";
  private static final String AUTH =
      "Basic " + Base64.getEncoder().encodeToString(ConfigHandler.restLogin.getBytes());

  private RequestHelper() {
  }

  private static String getBaseURL() {
    if (ConfigHandler.restURL.endsWith("/")) {
      return ConfigHandler.restURL;
    } else {
      return ConfigHandler.restURL + "/";
    }
  }

  private static void post(String type, String URL, Object data) {
    try {
      URL postURL = new URL(getBaseURL() + URL);
      URLConnection connection = postURL.openConnection();
      HttpURLConnection http = (HttpURLConnection) connection;
      http.setRequestMethod(type.toUpperCase());
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.setRequestProperty("authKey", ConfigHandler.restLogin);
      http.setDoOutput(true);
      http.setRequestProperty("Authorization", AUTH);
      String jsonData = GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));
      connection.getOutputStream().write(jsonData.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      if (status == HttpsURLConnection.HTTP_UNAUTHORIZED) {
        ServerEssentialsServer.LOGGER.error("Invalid Rest API Key, Unable to Put");
      }
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  private static void post(String URL, Object data) {
    post("post", URL, data);
  }

  private static <T extends Object> T get(String URL, String dataName, Class<T> type) {
    if (!URL.isEmpty()) {
      try {
        URL obj = new URL(getBaseURL() + URL + dataName);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
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
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
    }
    return null;
  }

  public static class RankResponses {

    public static void addRank(Rank rank) {
      post("rank/add", rank);
    }

    public static Rank getRank(String name) {
      return get("rank/", name, Rank.class);
    }

    public static void overrideRank(Rank rank) {
      post("put", "rank" + rank.getName() + "/override", rank);
    }

    public static Rank[] getAllRanks() {
      return get("rank", "", Rank[].class);
    }
  }

  public static class UserResponses {

    public static void addPlayerData(GlobalUser user) {
      post("user/add", user);
    }

    public static GlobalUser getPlayerData(UUID uuid) {
      return get("user/", uuid.toString(), GlobalUser.class);
    }

    public static void overridePlayerData(GlobalUser user) {
      post("put", "user/" + user.getUuid() + "/override", user);
    }
  }

  public static class TeamResponses {

    public static void addTeam(GlobalTeam team) {
      post("team/add", team);
    }

    public static GlobalTeam getTeam(String name) {
      if (!name.isEmpty()) {
        return get("team/", name, GlobalTeam.class);
      }
      return null;
    }

    public static void overrideTeam(GlobalTeam team) {
      post("put", "team/" + team.getName() + "/override", team);
    }
  }

  public static class AutoRankResponses {

    public static void addAutoRank(AutoRank rank) {
      post("autorank/add", rank);
    }

    public static AutoRank getAutoRank(String name) {
      return get("autorank/", name, AutoRank.class);
    }

    public static void overrideAutoRank(AutoRank rank) {
      post("put", "autorank/" + rank.getID() + "/override", rank);
    }

    public static AutoRank[] getAllAutoRanks() {
      return get("autorank/", "", AutoRank[].class);
    }
  }

  public static class EcoResponses {

    public static void addEco(Currency coin) {
      post("eco/add", coin);
    }

    public static Currency getEco(String name) {
      return get("eco/", name, Currency.class);
    }

    public static void overrideEco(Currency currency) {
      post("put", "eco/" + currency.getName() + "/override", currency);
    }

    public static Currency[] getAllCurrency() {
      return get("eco", "", Currency[].class);
    }
  }

  public static class TrackResponces {

    public static void syncServer(ServerStatus status) {
      post("/status", status);
    }
  }
}
