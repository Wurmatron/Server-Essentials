package com.wurmcraft.serveressentials.api.json.user;

import net.minecraft.util.math.BlockPos;

public class LocationWrapper extends Location {

  private int y;
  private int dim;

  public LocationWrapper(int x, int y, int z, int dim) {
    super(x, z);
    this.y = y;
    this.dim = dim;
  }

  public LocationWrapper(BlockPos pos, int dim) {
    super(pos.getX(), pos.getZ());
    this.y = pos.getY();
    this.dim = dim;
  }

  public LocationWrapper(double x, double y, double z, int dim) {
    super((int) x, (int) z);
    this.y = (int) y;
    this.dim = dim;
  }

  public int getY() {
    return y;
  }

  public int getDim() {
    return dim;
  }
}
