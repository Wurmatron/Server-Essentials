package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;

public class Warp extends Home implements IDataType {

  public Warp() {

  }

  public Warp(String name, BlockPos location, int dimension, float yaw, float pitch) {
    super(name, location, dimension, yaw, pitch);
  }

  @Override
  public String getID() {
    return getName();
  }
}
