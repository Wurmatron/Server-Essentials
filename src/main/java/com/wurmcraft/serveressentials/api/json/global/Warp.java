package com.wurmcraft.serveressentials.api.json.global;

import com.wurmcraft.serveressentials.api.json.user.Home;
import com.wurmcraft.serveressentials.api.json.user.IDataType;
import net.minecraft.util.math.BlockPos;

public class Warp extends Home implements IDataType {

  public Warp() {}

  public Warp(String name, BlockPos location, int dimension, float yaw, float pitch) {
    super(name, location, dimension, yaw, pitch);
  }

  public String getID() {
    return getName();
  }
}
