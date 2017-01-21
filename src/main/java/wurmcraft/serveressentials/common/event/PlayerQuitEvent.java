package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerQuitEvent {

    @SubscribeEvent
    public void leaveServer(PlayerEvent.PlayerLoggedOutEvent e) {
        DataHelper.unloadPlayerData(e.player.getGameProfile().getId());
    }
}
