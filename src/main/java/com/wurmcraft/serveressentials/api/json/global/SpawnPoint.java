package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import net.minecraft.util.math.BlockPos;

/**
 * Used to store the value of the world spawn
 */
public class SpawnPoint {


  public LocationWrapper location;
  public float yaw;
  public float pitch;

  public SpawnPoint(LocationWrapper location, float yaw, float pitch) {
    this.location = location;
    this.yaw = yaw;
    this.pitch = pitch;
  }
}
