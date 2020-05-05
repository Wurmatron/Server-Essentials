package com.wurmcraft.serveressentials.core.api.data;

import com.wurmcraft.serveressentials.core.api.player.Home;

public class Warp extends Home implements StoredDataType {

  public Warp() {
    super("", 0, 0, 0, 0);
  }

  public Warp(String name, double x, double y, double z, int dim) {
    super(name, x, y, z, dim);
  }

  @Override
  public String getID() {
    return name;
  }
}
