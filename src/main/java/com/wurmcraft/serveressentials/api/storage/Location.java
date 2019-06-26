package com.wurmcraft.serveressentials.api.storage;

public class Location {

  private int x;
  private int z;

  public Location() {
    x = 0;
    z = 0;
  }

  public Location(int x, int z) {
    this.x = x;
    this.z = z;
  }

  public int getX() {
    return x;
  }

  public int getZ() {
    return z;
  }

  @Override
  public String toString() {
    return "Location{X:" + x + " Z:" + z + "}";
  }
}
