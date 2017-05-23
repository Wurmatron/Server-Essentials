package wurmcraft.serveressentials.common.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.utils.DataHelper;

import java.util.UUID;

public class PlayerQuitEvent {

	@SubscribeEvent
	public void leaveServer (PlayerEvent.PlayerLoggedOutEvent e) {
		DataHelper.updateLastseen (e.player.getGameProfile ().getId ());
		DataHelper.unloadPlayerData (e.player.getGameProfile ().getId ());
		ChannelManager.removePlayerChannel (e.player.getGameProfile ().getId (),ChannelManager.getPlayerChannel (e.player.getGameProfile ().getId ()));
		DataHelper.playerVaults.remove (e.player.getGameProfile ().getId ());
		if (DataHelper.lastMessage.containsKey (e.player.getGameProfile ()))
			for (UUID uuid : DataHelper.lastMessage.keySet ())
				if (DataHelper.lastMessage.get (uuid).equals (e.player.getGameProfile ().getId ()))
					DataHelper.lastMessage.remove (uuid);
		DataHelper.spys.remove (e.player.getGameProfile ().getId ());
	}
}
