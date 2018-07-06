package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.fileOnly.AutoRank;

public class AutoRankJson extends AutoRank {

  public String authKey;

  public AutoRankJson(AutoRank auto, String authKey) {
    super(auto);
    this.authKey = authKey;
  }
}
