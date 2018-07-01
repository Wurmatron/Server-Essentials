package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.user.Rank;
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
          .post(Entity.entity(GSON.toJson(rank), MediaType.APPLICATION_JSON));
    }

    public static Rank getRank(String name) {
      return client
          .target(getBaseURL() + "rank/find/" + name)
          .request(MediaType.APPLICATION_JSON)
          .get(Rank.class);
    }

    public static Response overrideRank(Rank rank) {
      return client
          .target(getBaseURL() + "rank/override")
          .request(MediaType.APPLICATION_JSON)
          .put(Entity.entity(rank, MediaType.APPLICATION_JSON));
    }

    public static Rank[] getAllRanks() {
      return client
          .target("http://localhost:8080/rank/find/")
          .request(MediaType.APPLICATION_JSON)
          .get(new GenericType<ArrayList<Rank>>() {
          })
          .toArray(new Rank[0]);
    }
  }

  public static class UserResponses {

    public static Response addPlayerData(GlobalUser user) {
      return client
          .target(getBaseURL() + "user/add")
          .request(MediaType.APPLICATION_JSON)
          .post(Entity.entity(GSON.toJson(user), MediaType.APPLICATION_JSON));
    }

    public static GlobalUser getPlayerData(UUID name) {
      return client
          .target(getBaseURL() + "user/find/" + name)
          .request(MediaType.APPLICATION_JSON)
          .get(GlobalUser.class);
    }

    public static Response overridePlayerData(GlobalUser user) {
      return client
          .target(getBaseURL() + "user/override")
          .request(MediaType.APPLICATION_JSON)
          .put(Entity.entity(GSON.toJson(user), MediaType.APPLICATION_JSON));
    }
  }

  public static class TeamResponses {

    public static Response addTeam(GlobalTeam team) {
      return client
          .target(getBaseURL() + "team/add")
          .request(MediaType.APPLICATION_JSON)
          .post(Entity.entity(GSON.toJson(team), MediaType.APPLICATION_JSON));
    }

    public static GlobalTeam getTeam(String name) {
      return client
          .target(getBaseURL() + "team/find/" + name)
          .request(MediaType.APPLICATION_JSON)
          .get(GlobalTeam.class);
    }

    public static Response overrideTeam(GlobalTeam team) {
      return client
          .target(getBaseURL() + "team/override")
          .request(MediaType.APPLICATION_JSON)
          .put(Entity.entity(GSON.toJson(team), MediaType.APPLICATION_JSON));
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
