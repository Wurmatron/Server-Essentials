package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerQuitEvent {

	@SubscribeEvent
	public void leaveServer (PlayerEvent.PlayerLoggedOutEvent e) {
		DataHelper.updateLastseen (e.player.getGameProfile ().getId ());
		DataHelper.unloadPlayerData (e.player.getGameProfile ().getId ());
		ChannelManager.removePlayerChannel (e.player.getGameProfile ().getId (), ChannelManager.getPlayerChannel (e.player.getGameProfile ().getId ()));
	}
}
