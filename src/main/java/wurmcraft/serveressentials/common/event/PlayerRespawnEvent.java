package wurmcraft.serveressentials.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.api.storage.SpawnPoint;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerRespawnEvent {

    @SubscribeEvent
    public void constructEvent(PlayerEvent.Clone e) {
        if (e.isWasDeath() && !e.isCanceled()) {
            if (Settings.respawn_point.equalsIgnoreCase("spawn")) {
                teleportToSpawn(e.getEntityPlayer());
            } else if (Settings.respawn_point.equalsIgnoreCase("home")) {
                PlayerData playerData = DataHelper.getPlayerData(e.getEntityPlayer().getGameProfile().getId());
                Home defaultHome = playerData.getHome(Settings.home_name);
                if (defaultHome != null) {
                    e.getEntityPlayer().setLocationAndAngles(defaultHome.getPos().getX(), defaultHome.getPos().getY(), defaultHome.getPos().getZ(), defaultHome.getYaw(), defaultHome.getPitch());
                    e.getEntityPlayer().dimension = defaultHome.getDimension();
                }
            }
        }
    }

    private void teleportToSpawn(EntityPlayer player) {
        SpawnPoint spawn = DataHelper.globalSettings.getSpawn();
        if (spawn != null) {
            player.setLocationAndAngles(spawn.location.getX(), spawn.location.getY(), spawn.location.getZ(), spawn.yaw, spawn.pitch);
            player.dimension = spawn.dimension;
        }
    }
}
