package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.rest.GlobalUser;

public class GlobalUserJson extends GlobalUser {

  private final String authKey;

  public GlobalUserJson(GlobalUser user, String authKey) {
    super(user);
    this.authKey = authKey;
  }
}
