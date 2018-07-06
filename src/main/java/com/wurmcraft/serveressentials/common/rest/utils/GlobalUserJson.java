package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.restOnly.GlobalUser;

public class GlobalUserJson extends GlobalUser {

  public String authKey;

  public GlobalUserJson(GlobalUser user, String authKey) {
    super(user);
    this.authKey = authKey;
  }
}