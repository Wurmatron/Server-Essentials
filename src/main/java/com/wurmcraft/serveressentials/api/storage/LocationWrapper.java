package com.wurmcraft.serveressentials.api.storage;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LocationWrapper extends Location {

  private int y;
  private int dim;

  public LocationWrapper() {
    super();
    y = 0;
    dim = 0;
  }

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

  public LocationWrapper(Vec3d vector, int dimension) {
    super((int) vector.x, (int) vector.z);
    this.y = (int) vector.y;
    this.dim = dimension;
  }

  public int getY() {
    return y;
  }

  public int getDim() {
    return dim;
  }

  public BlockPos getPos() {
    return new BlockPos(getX(), y, getZ());
  }
}
