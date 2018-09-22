package com.wurmcraft.serveressentials.api.json.claim2;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;

public class Claim3D {

  private ClaimOwner owner;
  private LocationWrapper cornerLow;
  private LocationWrapper cornerHigh;

  public Claim3D(ClaimOwner owner, LocationWrapper cornerLow, LocationWrapper cornerHigh) {
    this.owner = owner;
    this.cornerLow = cornerLow;
    this.cornerHigh = cornerHigh;
  }

  public boolean isWithin(LocationWrapper loc) {
    if (loc != null && loc.getDim() == cornerLow.getDim()) {
      if (loc.getX() >= cornerLow.getX()
          && loc.getY() >= cornerLow.getY()
          && loc.getZ() >= loc.getZ()) {
        if (loc.getX() <= cornerHigh.getX()
            && loc.getY() <= cornerHigh.getY()
            && loc.getZ() <= cornerHigh.getZ()) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isWithin(Claim3D claim3D, LocationWrapper loc) {
    return claim3D.isWithin(loc);
  }
}
