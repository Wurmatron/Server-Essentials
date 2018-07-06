package com.wurmcraft.serveressentials.common.rest.utils;

import com.wurmcraft.serveressentials.api.json.user.team.restOnly.GlobalTeam;

public class GlobalTeamJson extends GlobalTeam {

  public String authKey;

  public GlobalTeamJson(GlobalTeam team, String authKey) {
    super(team);
    this.authKey = authKey;
  }
}
