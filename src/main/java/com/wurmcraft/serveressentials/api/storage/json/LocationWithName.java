package com.wurmcraft.serveressentials.api.storage.json;

import com.wurmcraft.serveressentials.api.storage.FileType;
import com.wurmcraft.serveressentials.api.storage.LocationWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LocationWithName extends LocationWrapper implements FileType {

  public String name;

  public LocationWithName() {
    name = "Invalid";
  }

  public LocationWithName(String name, int x, int y, int z, int dim) {
    super(x, y, z, dim);
    this.name = name;
  }

  public LocationWithName(String name, BlockPos pos, int dim) {
    super(pos, dim);
    this.name = name;
  }

  public LocationWithName(String name, double x, double y, double z, int dim) {
    super(x, y, z, dim);
    this.name = name;
  }

  public LocationWithName(String name, Vec3d vector, int dimension) {
    super(vector, dimension);
    this.name = name;
  }

  @Override
  public String getID() {
    return name;
  }
}
