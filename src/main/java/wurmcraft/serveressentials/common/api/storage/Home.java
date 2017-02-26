package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;

public class Home {

    private String name;
    private BlockPos pos;
    private float yaw;
    private float pitch;

    public Home(String name, BlockPos location, float yaw, float pitch) {
        this.name = setName(name);
        this.pos = setPos(location);
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

    public float setYaw(float yaw) {
        this.yaw = yaw;
        return this.yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float setPitch(float pitch) {
        this.pitch = pitch;
        return this.pitch;
    }
}
