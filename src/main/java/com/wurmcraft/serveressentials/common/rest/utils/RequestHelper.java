package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.optional.Currency;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import com.wurmcraft.serveressentials.common.ServerEssentialsServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class RequestHelper {

  private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private static final String USER_AGENT = "Mozilla/5.0";

  private static String getBaseURL() {
    if (ConfigHandler.restURL.endsWith("/")) {
      return ConfigHandler.restURL;
    } else {
      return ConfigHandler.restURL + "/";
    }
  }

  private static void post(String URL, Object data) {
    try {
      URL postURL = new URL(getBaseURL() + URL);
      URLConnection connection = postURL.openConnection();
      HttpURLConnection http = (HttpURLConnection) connection;
      http.setRequestMethod("POST");
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.setDoOutput(true);
      String jsonData = GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));
      connection.getOutputStream().write(jsonData.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      ServerEssentialsServer.logger.debug("Post Status: " + status);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void put(String URL, Object data) {
    try {
      URL postURL = new URL(getBaseURL() + URL);
      URLConnection connection = postURL.openConnection();
      HttpURLConnection http = (HttpURLConnection) connection;
      http.setRequestMethod("PUT");
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.setDoOutput(true);
      String jsonData = GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));
      connection.getOutputStream().write(jsonData.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      ServerEssentialsServer.logger.debug("Put Status: " + status);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class RankResponses {

    public static void addRank(Rank rank) {
      post("rank/add", new RankJson(rank, ConfigHandler.restAuthKey));
    }

    public static Rank getRank(String name) {
      try {
        URL obj = new URL(getBaseURL() + "rank/find/" + name);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Rank.class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static void overrideRank(Rank rank) {
      put("rank/override", new RankJson(rank, ConfigHandler.restAuthKey));
    }

    public static Rank[] getAllRanks() {
      try {
        URL obj = new URL(getBaseURL() + "rank/find");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Rank[].class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return new Rank[0];
    }
  }

  public static class UserResponses {

    public static void addPlayerData(GlobalUser user) {
      post("user/add", new GlobalUserJson(user, ConfigHandler.restAuthKey));
    }

    public static GlobalUser getPlayerData(UUID uuid) {
      try {
        URL obj = new URL(getBaseURL() + "user/find/" + uuid.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), GlobalUser.class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static void overridePlayerData(GlobalUser user) {
      put("user/override", new GlobalUserJson(user, ConfigHandler.restAuthKey));
    }
  }

  public static class TeamResponses {

    public static void addTeam(GlobalTeam team) {
      post("team/add", new GlobalTeamJson(team, ConfigHandler.restAuthKey));
    }

    public static GlobalTeam getTeam(String name) {
      if (!name.isEmpty()) {
        try {
          URL obj = new URL(getBaseURL() + "team/find/" + name);
          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
          con.setRequestMethod("GET");
          con.setRequestProperty("User-Agent", USER_AGENT);
          int responseCode = con.getResponseCode();
          if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
            }
            in.close();
            return GSON.fromJson(response.toString(), GlobalTeam.class);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    public static void overrideTeam(GlobalTeam team) {
      post("team/override", new GlobalTeamJson(team, ConfigHandler.restAuthKey));
    }
  }

  public static class AutoRankResponses {

    public static void addAutoRank(AutoRank rank) {
      post("autorank/add", new AutoRankJson(rank, ConfigHandler.restAuthKey));
    }

    public static AutoRank getAutoRank(String name) {
      try {
        URL obj = new URL(getBaseURL() + "autorank/find/" + name);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), AutoRank.class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static void overrideAutoRank(AutoRank rank) {
      put("autorank/override", new AutoRankJson(rank, ConfigHandler.restAuthKey));
    }

    public static AutoRank[] getAllAutoRanks() {
      try {
        URL obj = new URL(getBaseURL() + "autorank/find");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), AutoRank[].class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return new AutoRank[0];
    }
  }

  public static class EcoResponses {

    public static void addEco(Currency coin) {
      post("eco/add", new CurrencyJson(coin, ConfigHandler.restAuthKey));
    }

    public static Currency getEco(String name) {
      try {
        URL obj = new URL(getBaseURL() + "eco/find/" + name);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Currency.class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    public static void overrideEco(Currency currency) {
      put("eco/override", new CurrencyJson(currency, ConfigHandler.restAuthKey));
    }

    public static Currency[] getAllCurrency() {
      try {
        URL obj = new URL(getBaseURL() + "eco/find");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Currency[].class);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return new Currency[0];
    }
  }
}
