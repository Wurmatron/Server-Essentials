package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;

/** Used to store the value of the world spawn */
public class SpawnPoint {

  public final LocationWrapper location;
  public final float yaw;
  public final float pitch;

  public SpawnPoint(LocationWrapper location, float yaw, float pitch) {
    this.location = location;
    this.yaw = yaw;
    this.pitch = pitch;
  }
}
