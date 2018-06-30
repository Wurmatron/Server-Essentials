package com.wurmcraft.serveressentials.common.rest.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wurmcraft.serveressentials.api.json.user.Rank;
import com.wurmcraft.serveressentials.common.ConfigHandler;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RequestHelper {

  private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static Client client = ClientBuilder.newClient();

  public static class RankResponses {

    public static Response addRank(Rank rank) {
      return client.target(getBaseURL() + "rank/add").request(MediaType.APPLICATION_JSON).post(
          Entity.entity(GSON.toJson(rank), MediaType.APPLICATION_JSON));
    }

    public static Rank getRank(String name) {
      return client.target(getBaseURL() + "rank/find" + name).request(MediaType.APPLICATION_JSON)
          .get(Rank.class);
    }

    public static Response overrideRank(Rank rank) {
      return client.target(getBaseURL() + "rank/override").request(MediaType.APPLICATION_JSON).put(
          Entity.entity(GSON.toJson(rank), MediaType.APPLICATION_JSON));
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
