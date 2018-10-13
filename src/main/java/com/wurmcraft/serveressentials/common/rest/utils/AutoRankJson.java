package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.file.AutoRank;

public class AutoRankJson extends AutoRank {

  private String authKey;

  public AutoRankJson(AutoRank auto, String authKey) {
    super(auto);
    this.authKey = authKey;
  }
}
