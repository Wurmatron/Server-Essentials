package com.wurmcraft.serveressentials.api.protection;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;

public class ClaimedArea {

  public LocationWrapper locationA;
  public LocationWrapper locationB;

  public ClaimedArea(LocationWrapper locationA, LocationWrapper locationB) {
    this.locationA = locationA;
    this.locationB = locationB;
  }
}
