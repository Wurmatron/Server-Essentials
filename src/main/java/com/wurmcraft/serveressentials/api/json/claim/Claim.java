package com.wurmcraft.serveressentials.api.json.claim;

import com.wurmcraft.serveressentials.api.json.user.team.ITeam;
import com.wurmcraft.serveressentials.common.utils.UserManager;
import java.util.UUID;

public class Claim {

  private String team;
  private UUID owner;

    public Claim(ITeam team, UUID owner) {
      if (team != null) {
        this.team = team.getName();
      }
      this.owner = owner;
    }

    public ITeam getTeam() {
      if (team != null && team.length() > 0) {
        return (ITeam) UserManager.getTeam(team)[0];
      }
      return null;
    }

  public UUID getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    return "Claim{" + "team='" + team + '\'' + ", owner=" + owner + '}';
  }
}
