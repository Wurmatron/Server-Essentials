package wurmcraft.serveressentials.common.api.storage;

import wurmcraft.serveressentials.common.utils.DataHelper;

public class Global {

    private SpawnPoint spawn;

    public Global(SpawnPoint spawn) {
        this.spawn = spawn;
    }

    public SpawnPoint getSpawn() {
        return spawn;
    }

    public void setSpawn(SpawnPoint spawn) {
        this.spawn = spawn;
        DataHelper.overrideGlobal(this);
    }
}
