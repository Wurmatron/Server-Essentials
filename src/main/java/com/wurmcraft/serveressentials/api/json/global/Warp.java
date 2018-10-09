package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.DataType;
import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import net.minecraft.util.math.BlockPos;

public class Warp extends Home implements DataType {

  public Warp() {}

  public Warp(String name, BlockPos location, int dimension) {
    super(name, new LocationWrapper(location, dimension));
  }

  public Warp(String name, LocationWrapper location) {
    super(name, location);
  }

  public String getID() {
    return getName();
  }
}
