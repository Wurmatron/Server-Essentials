package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;

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