package wurmcraft.serveressentials.common.event;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.chat.ChatHelper;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.DataHelper;

public class PlayerChatEvent {

	@SubscribeEvent
	public void onChat (ServerChatEvent e) {
		e.setCanceled (true);
		if (!DataHelper.isMuted (e.getPlayer ().getGameProfile ().getId ()))
			ChatHelper.sendMessage (e.getUsername (),DataHelper.getPlayerData (e.getPlayer ().getGameProfile ().getId ()).getRank (),ChannelManager.getPlayerChannel (e.getPlayer ().getGameProfile ().getId ()),e.getPlayer ().dimension,e.getMessage ());
		else
			ChatHelper.sendMessageTo (e.getPlayer (),Local.NOTIFY_MUTED);
	}
}
