package com.wurmcraft.serveressentials.api.json.user;

import net.minecraft.util.math.BlockPos;

/** Holds all the values for a home */
public class Home {

  private String name;
  private BlockPos pos;
  private float yaw;
  private float pitch;
  private int dimension;

  public Home() {}

  public Home(String name, BlockPos location, int dimension, float yaw, float pitch) {
    this.name = setName(name);
    this.pos = setPos(location);
    this.dimension = dimension;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public String getName() {
    return name;
  }

  public String setName(String name) {
    this.name = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    return getName();
  }

  public BlockPos getPos() {
    return pos;
  }

  public BlockPos setPos(BlockPos pos) {
    this.pos = pos;
    return getPos();
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public int getDimension() {
    return dimension;
  }
}
