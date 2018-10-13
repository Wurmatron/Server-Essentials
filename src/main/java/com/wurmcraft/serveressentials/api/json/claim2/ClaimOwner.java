package com.wurmcraft.serveressentials.api.json.claim2;

import java.util.UUID;

public class ClaimOwner {

  private String team;
  private UUID owner;
  private ClaimPerm[] claimSettings;

  public ClaimOwner(String team, UUID owner) {
    this.team = team;
    this.owner = owner;
    claimSettings = new ClaimPerm[0];
  }

  public boolean isOwner(UUID uuid) {
    return owner.equals(uuid);
  }

  public static boolean isOwner(ClaimOwner owner, UUID uuid) {
    return owner.isOwner(uuid);
  }

  public UUID getOwner() {
    return owner;
  }

  public boolean hasPermission(UUID uuid, ClaimPerm permToCheck) {
    if (uuid == owner) {
      for (ClaimPerm perm : getPerms()) {
        if (perm == permToCheck) {
          return true;
        }
      }
    }
    return false;
  }

  public ClaimPerm[] getPerms() {
    return claimSettings;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }
}
