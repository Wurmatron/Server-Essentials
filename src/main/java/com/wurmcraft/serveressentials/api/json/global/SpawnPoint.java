package com.wurmcraft.serveressentials.api.json.global;

import net.minecraft.util.math.BlockPos;

/**
 * Used to store the value of the world spawn
 */
public class SpawnPoint {

  public BlockPos location;
  public int dimension;
  public float yaw;
  public float pitch;

  public SpawnPoint(BlockPos location, float yaw, float pitch) {
    this.location = location;
    this.yaw = yaw;
    this.pitch = pitch;
  }
}
