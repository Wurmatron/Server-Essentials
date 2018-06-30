package com.wurmcraft.serveressentials.api.json.claim;

import java.util.UUID;

public class Claim {

  private String team;
  private UUID owner;

  //  public Claim(Team team, UUID owner) {
  //    if (team != null) {
  //      this.team = team.getName();
  //    }
  //    this.owner = owner;
  //  }

  //  public Team getTeam() {
  ////    if (team != null && team.length() > 0) {
  ////      return TeamManager.getTeamFromName(team);
  ////    }
  //    return null;
  //  }

  public UUID getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return "Claim{" + "team='" + team + '\'' + ", owner=" + owner + '}';
  }
}
