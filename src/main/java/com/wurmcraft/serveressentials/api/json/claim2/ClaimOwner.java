package com.wurmcraft.serveressentials.api.json.claim2;

import java.util.UUID;

public class ClaimOwner {

  private String team;
  private UUID owner;
  private String[] claimSettings;

  public ClaimOwner(String team, UUID owner) {
    this.team = team;
    this.owner = owner;
    claimSettings = new String[0];
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
    for (ClaimPerm perm : getPerms()) {}

    return false;
  }

  public ClaimPerm[] getPerms() {
    return null;
  }
}
