package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.team.rest.GlobalTeam;

public class GlobalTeamJson extends GlobalTeam {

  private final String authKey;

  public GlobalTeamJson(GlobalTeam team, String authKey) {
    super(team);
    this.authKey = authKey;
  }
}
