package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;
import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;
import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import java.util.ArrayList;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RequestHelper {

  private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static Client client = ClientBuilder.newClient();

  public static class RankResponses {

    public static Response addRank(Rank rank) {
      return client
          .target(getBaseURL() + "rank/add")
          .request(MediaType.APPLICATION_JSON)
          .post(
              Entity.entity(
                  new RankJson(rank, ConfigHandler.restAuthKey), MediaType.APPLICATION_JSON));
    }

    public static Rank getRank(String name) {
      try {
        return client
            .target(getBaseURL() + "rank/find/" + name)
            .request(MediaType.APPLICATION_JSON)
            .get(Rank.class);
      } catch (Exception e) {
        return null;
      }
    }

    public static Response overrideRank(Rank rank) {
      return client
          .target(getBaseURL() + "rank/override")
          .request(MediaType.APPLICATION_JSON)
          .put(
              Entity.entity(
                  new RankJson(rank, ConfigHandler.restAuthKey), MediaType.APPLICATION_JSON));
    }

    public static Rank[] getAllRanks() {
      return client
          .target(getBaseURL() + "rank/find/")
          .request(MediaType.APPLICATION_JSON)
          .get(new GenericType<ArrayList<Rank>>() {})
          .toArray(new Rank[0]);
    }
  }

  public static class UserResponses {

    public static Response addPlayerData(GlobalUser user) {
      return client
          .target(getBaseURL() + "user/add")
          .request(MediaType.APPLICATION_JSON)
          .post(
              Entity.entity(
                  GSON.toJson(new GlobalUserJson(user, ConfigHandler.restAuthKey)),
                  MediaType.APPLICATION_JSON));
    }

    public static GlobalUser getPlayerData(UUID name) {
      try {
        return client
            .target(getBaseURL() + "user/find/" + name)
            .request(MediaType.APPLICATION_JSON)
            .get(GlobalUser.class);
      } catch (Exception e) {
        return null;
      }
    }

    public static Response overridePlayerData(GlobalUser user) {
      return client
          .target(getBaseURL() + "user/override")
          .request(MediaType.APPLICATION_JSON)
          .put(
              Entity.entity(
                  GSON.toJson(new GlobalUserJson(user, ConfigHandler.restAuthKey)),
                  MediaType.APPLICATION_JSON));
    }
  }

  public static class TeamResponses {

    public static Response addTeam(GlobalTeam team) {
      return client
          .target(getBaseURL() + "team/add")
          .request(MediaType.APPLICATION_JSON)
          .post(
              Entity.entity(
                  GSON.toJson(new GlobalTeamJson(team, ConfigHandler.restAuthKey)),
                  MediaType.APPLICATION_JSON));
    }

    public static GlobalTeam getTeam(String name) {
      try {
        return client
            .target(getBaseURL() + "team/find/" + name)
            .request(MediaType.APPLICATION_JSON)
            .get(GlobalTeam.class);
      } catch (Exception e) {
        return null;
      }
    }

    public static Response overrideTeam(GlobalTeam team) {
      return client
          .target(getBaseURL() + "team/override")
          .request(MediaType.APPLICATION_JSON)
          .put(
              Entity.entity(
                  GSON.toJson(new GlobalTeamJson(team, ConfigHandler.restAuthKey)),
                  MediaType.APPLICATION_JSON));
    }
  }

  public static class AutoRankResponses {

    public static Response addAutoRank(AutoRank rank) {
      return client
          .target(getBaseURL() + "autorank/add")
          .request(MediaType.APPLICATION_JSON)
          .post(Entity.entity(GSON.toJson(rank), MediaType.APPLICATION_JSON));
    }

    public static Rank getAutoRank(String name) {
      return client
          .target(getBaseURL() + "autorank/find/" + name)
          .request(MediaType.APPLICATION_JSON)
          .get(Rank.class);
    }

    public static Response overrideAutoRank(AutoRank rank) {
      return client
          .target(getBaseURL() + "autorank/override")
          .request(MediaType.APPLICATION_JSON)
          .put(Entity.entity(rank, MediaType.APPLICATION_JSON));
    }

    public static AutoRank[] getAllAutoRanks() {
      return client
          .target(getBaseURL() + "autorank/find/")
          .request(MediaType.APPLICATION_JSON)
          .get(new GenericType<ArrayList<AutoRank>>() {})
          .toArray(new AutoRank[0]);
    }
  }

  private static String getBaseURL() {
    if (ConfigHandler.restURL.endsWith("/")) {
      return ConfigHandler.restURL;
    } else {
      return ConfigHandler.restURL + "/";
    }
  }
}
