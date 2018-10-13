package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.Rank;

public class RankJson extends Rank {

  private final String authKey;

  public RankJson(Rank rank, String authKey) {
    super(rank);
    this.authKey = authKey;
  }
}
