package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.UUID;

public class RequestHelper {

  private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final String USER_AGENT = "Mozilla/5.0";

  private RequestHelper() {}

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
      http.setRequestProperty("authKey", ConfigHandler.restAuthKey);
      http.setDoOutput(true);
      String jsonData = GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));
      connection.getOutputStream().write(jsonData.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      ServerEssentialsServer.LOGGER.debug("Post Status: " + status);
      if (status == 401) {
        ServerEssentialsServer.LOGGER.error("Invalid Rest API Key, Unable to Post");
      }
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  private static void put(String URL, Object data) {
    try {
      URL postURL = new URL(getBaseURL() + URL);
      URLConnection connection = postURL.openConnection();
      HttpURLConnection http = (HttpURLConnection) connection;
      http.setRequestMethod("PUT");
      http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      http.setRequestProperty("authKey", ConfigHandler.restAuthKey);
      http.setDoOutput(true);
      String jsonData = GSON.toJson(data);
      connection.setRequestProperty("Content-Length", String.valueOf(jsonData.length()));
      connection.getOutputStream().write(jsonData.getBytes());
      int status = ((HttpURLConnection) connection).getResponseCode();
      ServerEssentialsServer.LOGGER.error("Put Status: " + status);
      if (status == 401) {
        ServerEssentialsServer.LOGGER.error("Invalid Rest API Key, Unable to Put");
      }
    } catch (Exception e) {
      ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
    }
  }

  public static class RankResponses {

    public static void addRank(Rank rank) {
      post("ranks/add", rank);
    }

    public static Rank getRank(String name) {
      try {
        URL obj = new URL(getBaseURL() + "ranks/find/" + name);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Rank.class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return null;
    }

    public static void overrideRank(Rank rank) {
      put("ranks" + rank.getName() + "/override", rank);
    }

    public static Rank[] getAllRanks() {
      try {
        URL obj = new URL(getBaseURL() + "ranks/find");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Rank[].class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return new Rank[0];
    }
  }

  public static class UserResponses {

    public static void addPlayerData(GlobalUser user) {
      post("users/add", user);
    }

    public static GlobalUser getPlayerData(UUID uuid) {
      try {
        URL obj = new URL(getBaseURL() + "users/find/" + uuid.toString());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), GlobalUser.class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return null;
    }

    public static void overridePlayerData(GlobalUser user) {
      put("users/find/" + user.getUuid() + "/override", user);
    }
  }

  public static class TeamResponses {

    public static void addTeam(GlobalTeam team) {
      post("teams/add", team);
    }

    public static GlobalTeam getTeam(String name) {
      if (!name.isEmpty()) {
        try {
          URL obj = new URL(getBaseURL() + "teams/find/" + name);
          HttpURLConnection con = (HttpURLConnection) obj.openConnection();
          con.setRequestMethod("GET");
          con.setRequestProperty("User-Agent", USER_AGENT);
          int responseCode = con.getResponseCode();
          if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
            }
            in.close();
            return GSON.fromJson(response.toString(), GlobalTeam.class);
          }
        } catch (Exception e) {
          ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
        }
      }
      return null;
    }

    public static void overrideTeam(GlobalTeam team) {
      put("teams" + team.getName() + "/override", team);
    }
  }

  public static class AutoRankResponses {

    public static void addAutoRank(AutoRank rank) {
      post("autoranks/add", rank);
    }

    public static AutoRank getAutoRank(String name) {
      try {
        URL obj = new URL(getBaseURL() + "autoranks/find/" + name);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), AutoRank.class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return null;
    }

    public static void overrideAutoRank(AutoRank rank) {
      put("ranks" + rank.getID() + "/override", rank);
    }

    public static AutoRank[] getAllAutoRanks() {
      try {
        URL obj = new URL(getBaseURL() + "autoranks/find");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), AutoRank[].class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return new AutoRank[0];
    }
  }

  public static class EcoResponses {

    public static void addEco(Currency coin) {
      post("eco/add", coin);
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
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Currency.class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return null;
    }

    public static void overrideEco(Currency currency) {
      put("ranks" + currency.getName() + "/override", currency);
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
          StringBuilder response = new StringBuilder();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          return GSON.fromJson(response.toString(), Currency[].class);
        }
      } catch (Exception e) {
        ServerEssentialsServer.LOGGER.warn(e.getLocalizedMessage());
      }
      return new Currency[0];
    }
  }
}
