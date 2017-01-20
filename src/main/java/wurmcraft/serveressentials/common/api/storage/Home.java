package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;

public class Home {

    private String name;
    private BlockPos pos;

    public Home(String name, BlockPos location) {
        this.name = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
        this.pos = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
